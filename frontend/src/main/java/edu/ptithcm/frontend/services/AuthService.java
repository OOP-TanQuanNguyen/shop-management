package edu.ptithcm.frontend.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.ptithcm.frontend.protocols.DTTP;

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

    private void setupHandlers() {
        // Handler nhận response từ server (type = "LOGIN")
        client.on(LOGIN_REQUEST, this::handleLoginResponse);
    }

    public void login(String username, String password, AuthCallback callback) {
        System.out.println("[SERVICE] 📤 Sending login request: " + username);

        String callbackKey = username;
        pendingCallbacks.put(callbackKey, callback);

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        try {
            client.send(LOGIN_REQUEST, data, REQUEST_STATUS, "Đăng nhập hệ thống");
            System.out.println("[SERVICE] ✅ Login request sent");
        } catch (IOException e) {
            System.err.println("[SERVICE] ❌ Failed to send: " + e.getMessage());
            pendingCallbacks.remove(callbackKey);
            callback.onResult(false, "Lỗi kết nối đến server!", null);
        }
    }

    /**
     * Xử lý response từ server. Backend trả về: status
     * (SUCCESS/NOT_FOUND_USER/WRONG_PASSWORD), message, data
     */
    private void handleLoginResponse(Map<String, Object> response) {
        System.out.println("[SERVICE] 📥 Login response received: " + response);

        // Lấy callback (vì backend không trả username, ta lấy callback đầu tiên)
        AuthCallback callback = null;
        if (!pendingCallbacks.isEmpty()) {
            String key = pendingCallbacks.keySet().iterator().next();
            callback = pendingCallbacks.remove(key);
        }

        if (callback == null) {
            System.out.println("[SERVICE] ⚠️ No callback found for response");
            return;
        }

        // Parse response theo format backend
        String status = (String) response.get("status");
        String message = (String) response.get("message");

        @SuppressWarnings("unchecked")
        Map<String, Object> userData = (Map<String, Object>) response.get("data");

        // Map status từ backend
        boolean success = "SUCCESS".equals(status);

        // Map message
        String displayMessage = message;
        if (displayMessage == null) {
            if ("NOT_FOUND_USER".equals(status)) {
                displayMessage = "Người dùng không tồn tại";
            } else if ("WRONG_PASSWORD".equals(status)) {
                displayMessage = "Mật khẩu không đúng";
            } else if ("SUCCESS".equals(status)) {
                displayMessage = "Đăng nhập thành công";
            } else {
                displayMessage = "Lỗi không xác định";
            }
        }

        System.out.println("[SERVICE] Status: " + status + " | Message: " + displayMessage);

        // Callback về controller
        callback.onResult(success, displayMessage, userData);
    }

    public interface AuthCallback {

        void onResult(boolean success, String message, Object userData);
    }
}
