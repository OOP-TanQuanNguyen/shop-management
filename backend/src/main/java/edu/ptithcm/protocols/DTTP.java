package edu.ptithcm.protocols;

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
    private final DTTPBandwidthMonitor monitor = new DTTPBandwidthMonitor();

    // Virtual thread pool for handlers
    private final ExecutorService handlerPool = Executors.newVirtualThreadPerTaskExecutor();

    private volatile boolean running = false;
    private Runnable onDisconnect = null;

    // Routing table
    private final ConcurrentHashMap<String, Consumer<DTTPArgs>> routes = new ConcurrentHashMap<>();


    /** Client mode */
    public DTTP(String host, int port) throws IOException {
        this.conn = new DTTPconnection(new Socket(host, port), monitor);
    }

    /** Server mode */
    public DTTP(Socket socket) throws IOException {
        this.conn = new DTTPconnection(socket, monitor);
    }


    public static class DTTPArgs {
        public final String type;
        public final String status;
        public final String message;
        public final Map<String, Object> data;
        private final DTTPconnection conn;

        public DTTPArgs(DTTPconnection conn, String type, String status, String message, Map<String, Object> data) {
            this.conn = conn;
            this.type = type;
            this.status = status;
            this.message = message;
            this.data = data;
        }

        public void reply(String type, Map<String, Object> data, String status, String message) throws IOException {
            DTTPmsg msg = new DTTPmsg(type, data, status, message);
            String json = new Gson().toJson(msg);

            byte[] raw = json.getBytes(StandardCharsets.UTF_8);
            boolean compressed = false;

            if (raw.length > 4096) {
                try {
                    raw = GzipCompression.compress(raw);
                    compressed = true;
                } catch (Exception ignored) {}
            }

            DTTPCompressedMsg wrapper = new DTTPCompressedMsg(compressed,
                    Base64.getEncoder().encodeToString(raw));

            String wrapperJson = new Gson().toJson(wrapper);
            String enc = DTTPEncryptor.encrypt(wrapperJson, SHARED_KEY);

            conn.send(enc);
        }
    }

    public void on(String type, Consumer<DTTPArgs> handler) {
        routes.put(type, handler);
    }

    public void send(String type, Map<String, Object> data, String status, String message) throws IOException {

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
        String enc = DTTPEncryptor.encrypt(wrapperJson, SHARED_KEY);

        conn.send(enc);
    }

    public void listen() {
        running = true;

        Thread.ofVirtual()
                .name("DTTP-Listener-" + conn.getAddress())
                .start(this::listenLoop);
    }


    private void listenLoop() {
        while (running) {
            try {
                String encrypted = conn.readJson();
                if (encrypted == null) continue;

                String decrypted = DTTPEncryptor.decrypt(encrypted, SHARED_KEY);
                if (decrypted == null) continue;

                DTTPCompressedMsg wrapper = gson.fromJson(decrypted, DTTPCompressedMsg.class);

                byte[] raw = Base64.getDecoder().decode(wrapper.payload);
                if (wrapper.compressed) {
                    try {
                        raw = GzipCompression.decompress(raw);
                    } catch (Exception ignored) {}
                }

                String json = new String(raw, StandardCharsets.UTF_8);

                DTTPmsg msg = gson.fromJson(json, DTTPmsg.class);

                dispatch(msg); //excecute function was registered from route

            } catch (IOException e) {
                stop();
                if (onDisconnect != null) onDisconnect.run();
                break;
            }
        }
    }


    private void dispatch(DTTPmsg msg) {
        Consumer<DTTPArgs> handler = routes.get(msg.getType());

        if (handler != null) {
            handlerPool.submit(() -> {
                try {
                    handler.accept(new DTTPArgs(
                            conn,
                            msg.getType(),
                            msg.getStatus(),
                            msg.getMessage(),
                            msg.getData()
                    ));
                } catch (Exception ex) {
                    System.err.println("Dispatch DTTTP Error : "+ex.getMessage());
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

    public DTTPBandwidthMonitor getMonitor() {
        return monitor;
    }

    public DTTPconnection getConnection() {
        return conn;
    }
}
