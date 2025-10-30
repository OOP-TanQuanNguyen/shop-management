package edu.ptithcm.app;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.ptithcm.app.reducers.AuthReducer;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.controllers.admin.AdminController;
import edu.ptithcm.controllers.login.LoginController;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.services.admin.AdminService;
import edu.ptithcm.services.authentication.AuthService;
import edu.ptithcm.views.admin.AdminForm;
import edu.ptithcm.views.login.LoginForm;

/**
 * App: Điểm khởi động toàn bộ chương trình
 * - Đăng ký reducer vào Store
 * - Theo dõi state để chuyển view (Login <-> Admin)
 * - Tạo và truyền các service/controller cần thiết
 */
public class App {
    private static JFrame currentView; // form hiện tại
    private static DTTP client;        // kết nối socket
    private static AuthService authService; // service dùng chung
    private static AdminService adminService;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 1️⃣ Đăng ký reducer
                AuthReducer.register(Store.getInstance());
                client = new DTTP("127.0.0.1", 2025);
                client.listen();
                client.setOnDisconnect(()->{
                    App.handleDisconnectServer();
                });
                authService = new AuthService(client);
                openLoginForm();
                Store.getInstance().subcribe(App::handleStateChange);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "❌ Không thể kết nối server: " + e.getMessage());
            }
        });
    }

    /** Mở form đăng nhập */
    private static void openLoginForm() {
        if (currentView != null) currentView.dispose();

        LoginForm view = new LoginForm();
        new LoginController(view, authService);
        currentView = view;
        currentView.setVisible(true);
    }

    /** Mở form admin */
    private static void openAdminForm(UserModel user) {
        if (currentView != null) currentView.dispose();

        AdminForm adminView = new AdminForm(user.toMap());
        new AdminController(adminView,adminService);
        currentView = adminView;
        currentView.setVisible(true);
    }

    private static void handleDisconnectServer(){
        currentView.dispose();
    }

    /** Theo dõi state trong Store để điều hướng UI */
    private static void handleStateChange(AppState state) {
        System.out.println("[DEBUG] Opening AdminForm controller initialized");
        Boolean isAuth = (Boolean) state.get("isAuthenticated");
        Boolean isLogout = (Boolean) state.get("isLogout");
        Object userObj = state.get("user");

        SwingUtilities.invokeLater(() -> {
            if (Boolean.TRUE.equals(isAuth) && userObj instanceof UserModel user) {
                openAdminForm(user);
            }
            if (Boolean.TRUE.equals(isLogout)){
                openLoginForm();
                Store.getInstance().getAppState().set("isLogout", false);
            }
        });
    }
}
