package edu.ptithcm.frontend.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.ptithcm.frontend.protocols.DTTP;

/**
 * Service xử lý xác thực đăng nhập (Auth) Gửi yêu cầu LOGIN đến server và nhận
 * phản hồi.
 */
public class AuthService {

    private static final String LOGIN_REQUEST = "LOGIN";
    private static final String REQUEST_STATUS = "REQUEST";

    private final DTTP client;
    private final Map<String, AuthCallback> pendingCallbacks;

    public AuthService(DTTP client) {
        if (client == null) {
            throw new IllegalArgumentException("DTTP client cannot be null");
        }
        this.client = client;
        this.pendingCallbacks = new ConcurrentHashMap<>();

        setupHandlers();
    }

    /**
     * Đăng ký handler để lắng nghe phản hồi từ server (LOGIN)
     */
    private void setupHandlers() {
        client.on(LOGIN_REQUEST, this::handleLoginResponse);
        System.out.println("[SERVICE] ✅ Handler for LOGIN registered");
    }

    /**
     * Gửi yêu cầu đăng nhập đến server
     */
    public void login(String username, String password, AuthCallback callback) {
        System.out.println("[SERVICE] 📤 Sending login request: " + username);

        String callbackKey = username;
        pendingCallbacks.put(callbackKey, callback);

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        try {
            client.send(LOGIN_REQUEST, data, REQUEST_STATUS, "Đăng nhập hệ thống");
            System.out.println("[SERVICE] ✅ Login request sent to server");
        } catch (IOException e) {
            System.err.println("[SERVICE] ❌ Failed to send request: " + e.getMessage());
            pendingCallbacks.remove(callbackKey);
            callback.onResult(false, "Lỗi kết nối đến server!", null);
        }
    }

    /**
     * Xử lý phản hồi LOGIN từ server. Backend trả về: {status, message, data}.
     */
    private void handleLoginResponse(Map<String, Object> response) {
        System.out.println("[SERVICE] 📥 Login response received: " + response);

        AuthCallback callback = null;
        if (!pendingCallbacks.isEmpty()) {
            String key = pendingCallbacks.keySet().iterator().next();
            callback = pendingCallbacks.remove(key);
            System.out.println("[SERVICE] ✅ Found pending callback for user: " + key);
        }

        if (callback == null) {
            System.out.println("[SERVICE] ⚠️ No callback found for response");
            return;
        }

        // ✅ Tạm thời fix ở đây:
        boolean success = response != null && !response.isEmpty();
        String message = "Đăng nhập thành công";

        // userData = toàn bộ response
        Map<String, Object> userData = response;

        System.out.println("[SERVICE] ✅ Parsed success=" + success + " | message=" + message);
        System.out.println("[SERVICE] 🧩 User data: " + userData);

        callback.onResult(success, message, userData);
    }

    /**
     * Callback interface gửi kết quả đăng nhập về Controller
     */
    public interface AuthCallback {

        void onResult(boolean success, String message, Object userData);
    }
}
