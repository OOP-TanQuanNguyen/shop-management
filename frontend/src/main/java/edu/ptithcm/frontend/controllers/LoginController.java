package edu.ptithcm.frontend.controllers;

import java.util.Map;
import javax.swing.SwingUtilities;

import edu.ptithcm.frontend.services.AuthService;
import edu.ptithcm.frontend.view.AdminForm;
import edu.ptithcm.frontend.view.LoginForm;
import edu.ptithcm.frontend.view.StaffForm;

/**
 * Controller xử lý logic đăng nhập, kết nối với AuthService.
 */
public class LoginController {

    private AuthService authService;
    private LoginForm loginForm;

    public LoginController(AuthService authService) {
        this.authService = authService;
        System.out.println("[LOGIN_CONTROLLER] 🔧 Created LoginController (authService=" + (authService != null) + ")");
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
        System.out.println("[LOGIN_CONTROLLER] ✅ AuthService injected successfully!");
    }

    public void setLoginForm(LoginForm form) {
        this.loginForm = form;
        System.out.println("[LOGIN_CONTROLLER] 🪟 LoginForm reference set");
    }

    /**
     * Gọi service đăng nhập và mở form phù hợp khi thành công
     */
    public void login(String username, String password, LoginCallback callback) {
        System.out.println("[LOGIN_CONTROLLER] 🚀 login() called with username=" + username);

        if (authService == null) {
            System.err.println("[LOGIN_CONTROLLER] ❌ AuthService is null (chưa kết nối server)");
            callback.onResult(false, "Lỗi: Chưa kết nối đến server!", null);
            return;
        }

        // Gọi AuthService
        authService.login(username, password, (success, message, data) -> {
            SwingUtilities.invokeLater(() -> {
                System.out.println("[LOGIN_CONTROLLER] 📥 Callback received from AuthService:");
                System.out.println("   → success: " + success);
                System.out.println("   → message: " + message);
                System.out.println("   → data: " + data);

                Map<String, Object> userData = null;
                if (data instanceof Map<?, ?> mapData) {
                    try {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> casted = (Map<String, Object>) mapData;
                        userData = casted;
                    } catch (ClassCastException e) {
                        System.err.println("[LOGIN_CONTROLLER] ⚠️ Data cast failed: " + e.getMessage());
                    }
                }

                if (success && userData != null) {
                    String role = (String) userData.get("role");
                    System.out.println("[LOGIN_CONTROLLER] ✅ Login successful, role = " + role);

                    if ("ADMIN".equalsIgnoreCase(role)) {
                        System.out.println("[LOGIN_CONTROLLER] 🏢 Opening AdminForm...");
                        new AdminForm(userData).setVisible(true);
                    } else if ("STAFF".equalsIgnoreCase(role)) {
                        System.out.println("[LOGIN_CONTROLLER] 👨‍💼 Opening StaffForm...");
                        new StaffForm(userData).setVisible(true);
                    } else {
                        System.out.println("[LOGIN_CONTROLLER] ⚠️ Unknown role: " + role);
                    }

                    if (loginForm != null) {
                        System.out.println("[LOGIN_CONTROLLER] 🔒 Closing LoginForm...");
                        loginForm.dispose();
                    } else {
                        System.out.println("[LOGIN_CONTROLLER] ⚠️ LoginForm reference is null!");
                    }
                } else {
                    System.out.println("[LOGIN_CONTROLLER] ❌ Login failed: " + message);
                }

                // callback về UI để hiển thị thông báo
                callback.onResult(success, message, userData);
            });
        });
    }

    @FunctionalInterface
    public interface LoginCallback {

        void onResult(boolean success, String message, Map<String, Object> userData);
    }
}
