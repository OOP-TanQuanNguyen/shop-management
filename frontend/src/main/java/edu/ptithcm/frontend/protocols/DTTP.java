package edu.ptithcm.frontend.protocols;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DTTP {

    private static int MAX_THREAD = 50;
    private DTTPconnection conn;
    private boolean running = false;
    private final Map<String, Consumer<Map<String, Object>>> routes = new HashMap<>();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(DTTP.MAX_THREAD);

    //client
    public DTTP(String host, int port) throws IOException {
        this.conn = new DTTPconnection(new Socket(host, port));
    }

    //server
    public DTTP(Socket socket) throws IOException {
        this.conn = new DTTPconnection(socket);
    }

    public void on(String type, Consumer<Map<String, Object>> handler) {
        this.routes.put(type, handler);
    }

    public void send(String type, Map<String, Object> data, String status, String message) throws IOException {
        DTTPmsg msg = new DTTPmsg(type, data, status, message);
        conn.send(msg.toJson());
    }

    public void listen() {
        this.running = true;
        threadPool.execute(() -> {
            while (running) {
                try {
                    String json = this.conn.readJson();
                    if (json == null) {
                        continue;
                    }

                    DTTPmsg msg = DTTPmsg.fromJson(json);
                    Consumer<Map<String, Object>> handler = routes.get(msg.getType());

                    if (handler != null) {
                        threadPool.execute(() -> handler.accept(msg.getData()));
                    } else {
                        System.out.println("[⚠] No handler for type: " + msg.getType());
                    }
                } catch (IOException e) {
                    if (running) {
                        System.out.println("[⚠] Connection lost or closed: " + e.getMessage());
                    }
                    this.stop(); // ✅ Đảm bảo ngắt kết nối an toàn
                    break;
                }
            }
        });
    }

    public void stop() {
        this.running = false;
        if (conn != null) {
            conn.close();
        }
    }

}
