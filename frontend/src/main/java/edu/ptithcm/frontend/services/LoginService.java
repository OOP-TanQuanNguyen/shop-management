package edu.ptithcm.frontend.services;

import edu.ptithcm.frontend.protocols.DTTP;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class LoginService {

    private final DTTP client;
    private final Consumer<Map<String, Object>> callback;

    public LoginService(Consumer<Map<String, Object>> callback) {
        this.callback = callback;
        DTTP temp = null;
        try {
            // Thay đổi PORT nếu Server của bạn chạy PORT khác (ví dụ: 12345)
            temp = new DTTP("localhost", 2025);
            temp.on("LOGIN_RESPONSE", callback);
            temp.listen(); // Khởi động lắng nghe Server
            System.out.println("[INFO] ket noi server thành công.");
        } catch (IOException e) {
            System.out.println("[WARN] Không the ket noi server. kich hoat che do offline.");
        }
        this.client = temp;
    }

    public void sendLogin(String username, String password) throws IOException {
        if (client == null) {
            // ⭐️ ĐÃ SỬA: Chế độ DEMO OFFLINE: Kiểm tra username/password
            if ("admin".equals(username) && "123".equals(password)) {
                Map<String, Object> demoData = Map.of(
                        "status", "OK",
                        "message", "Đăng nhập DEMO OFFLINE thành công!",
                        "role", "ADMIN"
                );
                callback.accept(demoData);
            } else {
                Map<String, Object> errorData = Map.of(
                        "status", "ERROR",
                        "message", "Lỗi DEMO: Tài khoản hoặc mật khẩu không hợp lệ.",
                        "role", "ERROR"
                );
                callback.accept(errorData);
            }
            return;
        }

        // Logic đăng nhập Online thông thường
        Map<String, Object> data = Map.of(
                "username", username,
                "password", password
        );
        client.send("LOGIN", data, "REQUEST", "Yêu cầu đăng nhập");
    }

    public DTTP getClient() {
        return client;
    }
}
