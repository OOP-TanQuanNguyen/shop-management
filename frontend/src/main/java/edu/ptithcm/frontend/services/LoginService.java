package edu.ptithcm.frontend.services;

import edu.ptithcm.frontend.protocols.DTTP;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class LoginService {

    private final DTTP client;
    private final Consumer<Map<String, Object>> callback;

    public LoginService(Consumer<Map<String, Object>> callback) {
        this.callback = callback;
        DTTP temp = null;
        try {
            temp = new DTTP("localhost", 2025);
            temp.on("LOGIN_RESPONSE", callback);
            temp.listen();
            System.out.println("[INFO] ket noi server thành công.");
        } catch (IOException e) {
            System.out.println("[WARN] Không the ket noi server. kich hoat che do offline.");
        }
        this.client = temp;
    }

    public void sendLogin(String username, String password) throws IOException {
        if (client == null) {
            // Chế độ DEMO OFFLINE: Giả lập đăng nhập thành công với vai trò ADMIN
            Map<String, Object> demoData = Map.of(
                    "status", "OK",
                    "message", "Đăng nhập DEMO OFFLINE thành công!",
                    "role", "ADMIN"
            );
            callback.accept(demoData);
            return;
        }

        // Logic đăng nhập Online thông thường
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        client.send("LOGIN", data, "REQUEST", "Yêu cầu đăng nhập");
    }

    public DTTP getClient() {
        return client;
    }
}
