package edu.ptithcm.protocols;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.google.gson.Gson;

public class DTTP {

    private static final String SHARED_KEY = "1234567890123456";
    private static final int COMPRESS_THRESHOLD = 4096;

    private final Gson gson = new Gson();
    private final DTTPconnection conn;

    private volatile boolean running = false;
    private Runnable onDisconnect = null;

    private final ConcurrentHashMap<String, Consumer<DTTPArgs>> routes = new ConcurrentHashMap<>();

    // CPU pool: 10 core threads, grows to 200 under load
    private final ExecutorService cpuPool = new ThreadPoolExecutor(
            10,
            200,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            Thread.ofPlatform().factory()
    );

    // Handler pool: virtual threads, cheap for I/O logic
    private final ExecutorService handlerPool = Executors.newVirtualThreadPerTaskExecutor();


    public DTTP(Socket socket) throws IOException {
        this.conn = new DTTPconnection(socket);
    }

    // =====================================================================
    // ARGS
    // =====================================================================
    public class DTTPArgs {
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

        public void reply(String type, Map<String, Object> data, String status, String message) {
            DTTP.this.send(type, data, status, message);
        }
    }


    public void on(String type, Consumer<DTTPArgs> handler) {
        routes.put(type, handler);
    }

    public void send(String type, Map<String, Object> data, String status, String message) {

        cpuPool.submit(() -> {
            try {
                // Encode
                DTTPmsg msg = new DTTPmsg(type, data, status, message);
                byte[] raw = gson.toJson(msg).getBytes(StandardCharsets.UTF_8);

                boolean compressed = false;
                if (raw.length > COMPRESS_THRESHOLD) {
                    raw = GzipCompression.compress(raw);
                    compressed = true;
                }

                String payload = Base64.getEncoder().encodeToString(raw);
                DTTPCompressedMsg wrapper = new DTTPCompressedMsg(compressed, payload);

                // Encrypt
                String encrypted = DTTPEncryptor.encrypt(gson.toJson(wrapper), SHARED_KEY);

                // Send
                conn.send(encrypted);
            }
            catch (IOException e) {
                throw new RuntimeException("Failed to send packet", e);
            }
            catch (Exception e) {
                System.err.println("Unexpected send error: " + e.getMessage());
            }
        });
    }

    private DTTPmsg decode(String encrypted) {
        try {
            String decrypted = DTTPEncryptor.decrypt(encrypted, SHARED_KEY);
            if (decrypted == null) return null;

            DTTPCompressedMsg wrapper = gson.fromJson(decrypted, DTTPCompressedMsg.class);

            byte[] raw = Base64.getDecoder().decode(wrapper.payload);
            if (wrapper.compressed) raw = GzipCompression.decompress(raw);

            return gson.fromJson(new String(raw, StandardCharsets.UTF_8), DTTPmsg.class);
        }
        catch (Exception e) {
            System.err.println("Decode error: " + e.getMessage());
            return null;
        }
    }

    public void listen() {

        this.running = true;

        Thread.startVirtualThread(() -> {
            while (running) {
                try {
                    String encrypted = conn.readJson();
                    if (encrypted == null) continue;

                    cpuPool.submit(() -> {
                        DTTPmsg msg = decode(encrypted);
                        if (msg != null) dispatch(msg);
                    });

                } catch (IOException e) {
                    stop();
                    if (onDisconnect != null) onDisconnect.run();
                    break;
                }
            }
        });
    }

    private void dispatch(DTTPmsg msg) {
        Consumer<DTTPArgs> handler = routes.get(msg.getType());
        if (handler == null) return;

        handlerPool.submit(() -> {
            try {
                handler.accept(new DTTPArgs(
                        conn,
                        msg.getType(),
                        msg.getStatus(),
                        msg.getMessage(),
                        msg.getData()
                ));
            }
            catch (Exception ex) {
                System.err.println("Handler error: " + ex.getMessage());
            }
        });
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


}
