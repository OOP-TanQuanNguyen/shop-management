package edu.ptithcm.frontend.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.ptithcm.frontend.protocols.DTTP;

/**
 * Service xử lý logic đăng nhập (liên lạc với server) Không liên quan đến giao
 * diện (View).
 */
public class AuthService {

    private final DTTP dttp;
    private LoginCallback loginCallback; // callback để báo kết quả về controller

    public AuthService(DTTP dttp) {
        this.dttp = dttp;
        System.out.println("[SERVICE] ✅ AuthService created & login handler registered");

        // Đăng ký handler khi server phản hồi LOGIN
        this.dttp.on("LOGIN", this::handleLoginResponse);
    }

    /**
     * Interface callback báo kết quả về controller
     */
    @FunctionalInterface
    public interface LoginCallback {

        void onResult(boolean success, String message, Map<String, Object> data);
    }

    /**
     * Đăng nhập – gửi request lên server
     */
    public void login(String username, String password, LoginCallback callback) {
        this.loginCallback = callback;
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        try {
            System.out.println("[SERVICE] 🚀 Sending login request: " + username);
            dttp.send("LOGIN", data, "REQUEST", "Yêu cầu đăng nhập");
            System.out.println("[SERVICE] ✅ Login request sent to server");
        } catch (IOException e) {
            System.err.println("[SERVICE] ❌ Lỗi khi gửi request: " + e.getMessage());
            callback.onResult(false, "Không thể gửi yêu cầu đăng nhập đến server", null);
        }
    }

    /**
     * Xử lý phản hồi từ server
     */
    private void handleLoginResponse(Map<String, Object> response) {
        try {
            System.out.println("[SERVICE] 📥 Login response received: " + response);

            // ✅ Server gửi về dạng: {hireDate=..., role=..., username=..., status=false, ...}
            // Không nên ép kiểu sai như lỗi cũ (Boolean -> String)
            // Ở đây không lấy "status" trong data, mà lấy từ phản hồi ngoài DTTPmsg (status, message)
            // → nên ta cần tách từ response chính (Map data của DTTPmsg)
            Map<String, Object> data = new HashMap<>(response);

            // Tùy vào cách server phản hồi, giả sử DTTPmsg.status chứa SUCCESS/FAILURE,
            // thì bạn nên kiểm tra trong tầng DTTP hoặc message — ở đây ta xử lý cơ bản
            Object roleObj = data.get("role");
            boolean success = roleObj != null; // có role nghĩa là đăng nhập thành công

            if (loginCallback != null) {
                loginCallback.onResult(success,
                        success ? "Đăng nhập thành công" : "Sai tài khoản hoặc mật khẩu",
                        data);
            }
        } catch (Exception e) {
            System.err.println("[SERVICE] ⚠️ Lỗi xử lý phản hồi đăng nhập: " + e.getMessage());
            e.printStackTrace();
            if (loginCallback != null) {
                loginCallback.onResult(false, "Lỗi xử lý phản hồi đăng nhập", null);
            }
        }
    }
}
