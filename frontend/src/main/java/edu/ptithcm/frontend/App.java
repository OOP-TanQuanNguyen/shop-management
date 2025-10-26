package edu.ptithcm.frontend;

import java.io.IOException;
import java.util.Map;

import edu.ptithcm.frontend.protocols.DTTP;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println("🟢 [CLIENT] Connecting to server on  localhost:2025...");

            // Kết nối đến server (địa chỉ + cổng)
            DTTP client = new DTTP("localhost", 2025);

            // Khi nhận được phản hồi từ server (pong)
            client.on("pong", data -> {
                System.out.println("💬 [CLIENT] Received from server: " + data);
            });

            // Gửi gói tin "ping"
            System.out.println("📤 [CLIENT] Sending ping...");
            client.send("ping", Map.of("msg", "Ping #1 from client"), "OK", "Initial ping test");


            // Lắng nghe server bằng thread riêng
            client.listen();

            // Giữ chương trình chạy — để client không thoát ngay
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
