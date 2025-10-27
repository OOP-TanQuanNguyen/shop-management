package edu.ptithcm.frontend.controllers;

import edu.ptithcm.frontend.services.AuthService;
import edu.ptithcm.frontend.services.AuthService.AuthCallback;

/**
 * Controller xử lý logic đăng nhập, kết nối giữa UI và Service.
 */
public class LoginController {

    private AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void login(String username, String password, LoginCallback callback) {
        System.out.println("[CONTROLLER] 🔄 Login request for: " + username);

        if (authService == null) {
            callback.onResult(false, "Chưa kết nối đến server. Vui lòng thử lại.");
            return;
        }

        authService.login(username, password, new AuthCallback() {
            @Override
            public void onResult(boolean success, String message, Object userData) {
                if (success) {
                    System.out.println("[CONTROLLER] ✅ Login successful for user: " + username);
                } else {
                    System.out.println("[CONTROLLER] ❌ Login failed: " + message);
                }
                callback.onResult(success, message);
            }
        });
    }

    public interface LoginCallback {

        void onResult(boolean success, String message);
    }
}
