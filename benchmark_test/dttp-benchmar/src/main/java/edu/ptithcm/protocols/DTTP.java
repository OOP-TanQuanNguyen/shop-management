package edu.ptithcm.protocols;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DTTP {
    private static final int MAX_THREAD = 50;
    private static final String SHARED_KEY = "1234567890123456"; // 🔐 khóa AES chung
    private final DTTPconnection conn;
    private boolean running = false;
    private final Map<String, Consumer<DTTPArgs>> routes = new HashMap<>();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREAD);

    // client
    public DTTP(String host, int port) throws IOException {
        this.conn = new DTTPconnection(new Socket(host, port));
    }

    // server
    public DTTP(Socket socket) throws IOException {
        this.conn = new DTTPconnection(socket);
    }

    /** Gói thông tin message + tiện ích reply */
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

        /** Gửi phản hồi lại client tương ứng */
        public void reply(String type, Map<String,Object> data, String status, String message) throws IOException {
                DTTPmsg msg = new DTTPmsg(type, data, status, message);
                String enc = DTTPEncryptor.encrypt(msg.toJson(), SHARED_KEY);
                conn.send(enc);
        }
    }

    /** Đăng ký handler */
    public void on(String type, Consumer<DTTPArgs> handler) {
        routes.put(type, handler);
    }

    /** Gửi message (dùng chung key) */
    public void send(String type, Map<String,Object> data, String status, String message) throws IOException {
        DTTPmsg msg = new DTTPmsg(type, data, status, message);
        String enc = DTTPEncryptor.encrypt(msg.toJson(), SHARED_KEY);
        conn.send(enc);
    }

    /** Lắng nghe dữ liệu đến */
    public void listen() {
        running = true;
        threadPool.execute(() -> {
            while (running) {
                try {
                    String enc = conn.readJson();
                    if (enc == null) continue;

                    String json = DTTPEncryptor.decrypt(enc, SHARED_KEY);
                    DTTPmsg msg = DTTPmsg.fromJson(json);

                    Consumer<DTTPArgs> handler = routes.get(msg.getType());
                    if (handler != null) {
                        DTTPArgs args = new DTTPArgs(conn, msg.getType(), msg.getStatus(), msg.getMessage(), msg.getData());
                        threadPool.execute(() -> handler.accept(args));
                    } else {
                        System.out.println("[⚠] No handler for type: " + msg.getType());
                    }
                } catch (IOException e) {
                    System.out.println("[DISCONNECTED] " + conn.getAddress());
                    stop();
                    break;
                }
            }
        });
    }

    public void stop() {
        running = false;
        conn.close();
    }

    public DTTPconnection getConnection() {
        return this.conn;
    }
}
