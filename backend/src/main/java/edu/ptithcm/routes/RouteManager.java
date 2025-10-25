package edu.ptithcm.routes;

import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.controller.LoginController;
import java.io.IOException;
import java.util.Map;

public class RouteManager {
    public static void registerRoutes(DTTP server) {
        server.on("LOGIN", (Map<String, Object> data) -> {
            Map<String, Object> result = LoginController.handleLogin(data);

            try {
                String status = (String) result.getOrDefault("status", "ERROR");
                String message = (String) result.getOrDefault("message", "Lỗi không xác định");

                // Dữ liệu trả về (nếu có)
                Map<String, Object> payload = result.containsKey("username")
                        ? Map.of("username", result.get("username"))
                        : null;

                server.send("LOGIN_RESPONSE", payload, status, message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
