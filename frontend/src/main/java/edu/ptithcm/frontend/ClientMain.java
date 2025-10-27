package edu.ptithcm.frontend;

import java.util.HashMap;
import java.util.Map;

import edu.ptithcm.frontend.protocols.DTTP;

/**
 * Client DTTP (phiên bản không mã hóa)
 * - Gửi LOGIN, PING
 * - Nhận và xử lý phản hồi từ server
 */
public class ClientMain {
    public static void main(String[] args) {
        try {
            DTTP client = new DTTP("0.tcp.ap.ngrok.io", 14678);

            client.on("LOGIN", (data) -> {
                System.out.println("[CLIENT] 🟢 Nhận phản hồi LOGIN từ server:");
                if (data == null || data.isEmpty()) {
                    System.out.println("   ⚠ Không có dữ liệu trong data (server có thể gửi null).");
                } else {
                    data.forEach((k, v) -> System.out.println("   " + k + " = " + v));
                }
            });

            client.on("PONG", (data) -> {
                System.out.println("[CLIENT] 🟣 Nhận PONG từ server:");
                if (data != null)
                    System.out.println("   reply = " + data.get("reply"));
            });

            client.listen();

            Map<String, Object> login = new HashMap<>();
            login.put("username", "admin1232");
            login.put("password", "ddddd");
            client.send("LOGIN", login, "REQUEST", "Đăng nhập hệ thống");

            Map<String, Object> ping = new HashMap<>();
            ping.put("time", System.currentTimeMillis());
            client.send("PING", ping, "REQUEST", "Ping server");

            Thread.sleep(5000);

            client.stop();
            System.out.println("[CLIENT] 🔒 Đã ngắt kết nối.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
