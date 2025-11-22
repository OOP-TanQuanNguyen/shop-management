package edu.ptithcm.benchmark.protocols;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.gson.Gson;

public class DTTP {

    private static final String SHARED_KEY = "1234567890123456";
    private static final int COMPRESS_THRESHOLD = 4096;

    private final Gson gson = new Gson();
    private final DTTPconnection conn;
    private final DTTPBandwidthMonitor monitor;

    private final ExecutorService handlerPool = Executors.newVirtualThreadPerTaskExecutor();
    private final ConcurrentHashMap<String, Consumer<DTTPArgs>> routes = new ConcurrentHashMap<>();

    private volatile boolean running = false;
    private Runnable onDisconnect = null;

    // ============================================================
    // CLIENT CONSTRUCTOR
    // ============================================================
    public DTTP(String host, int port) throws IOException {
        this.monitor = new DTTPBandwidthMonitor();
        this.conn = new DTTPconnection(new Socket(host, port), monitor);
    }

    public static class DTTPArgs {

        public final String type;
        public final String status;
        public final String message;
        public final Map<String,Object> data;

        private final DTTPconnection conn;

        public DTTPArgs(DTTPconnection conn,
                        String type,
                        String status,
                        String message,
                        Map<String,Object> data) {

            this.conn = conn;
            this.type = type;
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public DTTPconnection getConnection() { return conn; }
    }

    // ============================================================
    // Register event handler
    // ============================================================
    public void on(String type, Consumer<DTTPArgs> handler) {
        routes.put(type, handler);
    }

    // ============================================================
    // Send request
    // ============================================================
    public void send(String type, Map<String,Object> data, String status, String message) throws IOException {
        DTTPmsg msg = new DTTPmsg(type, data, status, message);
        String json = gson.toJson(msg);

        byte[] raw = json.getBytes(StandardCharsets.UTF_8);
        boolean compressed = false;

        if (raw.length > COMPRESS_THRESHOLD) {
            try {
                raw = GzipCompression.compress(raw);
                compressed = true;
            } catch (Exception ignored) {}
        }

        DTTPCompressedMsg wrapper = new DTTPCompressedMsg(
                compressed,
                Base64.getEncoder().encodeToString(raw)
        );

        String wrapperJson = gson.toJson(wrapper);
        String encrypted = DTTPEncryptor.encrypt(wrapperJson, SHARED_KEY);

        conn.send(encrypted);
    }

    public void listen() {
        if (running) return;

        running = true;

        Thread.ofVirtual()
                .name("DTTP-Client-Listener-" + conn.getAddress())
                .start(this::listenLoop);
    }

    private void listenLoop() {
        while (running) {
            try {
                String encrypted = conn.readJson();
                if (encrypted == null) continue;

                DTTPmsg msg = decode(encrypted);
                if (msg != null) dispatch(msg);

            } catch (IOException e) {
                stop();
                if (onDisconnect != null) onDisconnect.run();
                break;
            }
        }
    }

    // ============================================================
    // Decode incoming message
    // ============================================================
    private DTTPmsg decode(String encrypted) {
        try {
            String decrypted = DTTPEncryptor.decrypt(encrypted, SHARED_KEY);
            if (decrypted == null) return null;

            DTTPCompressedMsg wrapper = gson.fromJson(decrypted, DTTPCompressedMsg.class);

            byte[] raw = Base64.getDecoder().decode(wrapper.payload);
            if (wrapper.compressed) {
                try { raw = GzipCompression.decompress(raw); }
                catch (Exception ignored) { }
            }

            String json = new String(raw, StandardCharsets.UTF_8);
            return gson.fromJson(json, DTTPmsg.class);

        } catch (Exception ex) {
            System.err.println("[DTTP-Client] Decode error: " + ex.getMessage());
            return null;
        }
    }

    // ============================================================
    // Dispatch to registered handler
    // ============================================================
    private void dispatch(DTTPmsg msg) {
        Consumer<DTTPArgs> handler = routes.get(msg.getType());
        if (handler != null) {

            handlerPool.submit(() -> {
                DTTPArgs args = new DTTPArgs(
                        conn,
                        msg.getType(),
                        msg.getStatus(),
                        msg.getMessage(),
                        msg.getData()
                );

                try { handler.accept(args); }
                catch (Exception e) {
                    System.err.println("[DTTP-Client] Handler error: " + e.getMessage());
                }
            });
        }
    }

    public void stop() {
        running = false;
        conn.close();
    }

    public void setOnDisconnect(Runnable cb) {
        this.onDisconnect = cb;
    }

    public DTTPconnection getConnection() {
        return conn;
    }

    public DTTPBandwidthMonitor getMonitor() {
        return monitor;
    }
}
