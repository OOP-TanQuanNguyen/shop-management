package edu.ptithcm.app;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.ptithcm.app.reducers.AuthReducer;
import edu.ptithcm.app.reducers.BranchReducer;
import edu.ptithcm.app.reducers.CustomerReducer;
import edu.ptithcm.app.reducers.EmployeeReducer;
import edu.ptithcm.app.reducers.ProductReducer;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.controllers.admin.AdminController;
import edu.ptithcm.controllers.auth.LoginController;
import edu.ptithcm.controllers.pos.POSController;
import edu.ptithcm.middleware.SystemMiddleWare;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.services.authentication.AuthService;
import edu.ptithcm.views.admin.AdminForm;
import edu.ptithcm.views.login.LoginForm;
import edu.ptithcm.views.pos.POSForm;

/**
 * App: Điểm khởi động toàn bộ chương trình - Đăng ký reducer vào Store - Theo
 * dõi state để chuyển view (Login <-> Admin) - Tạo và truyền các
 * service/controller cần thiết
 */
public class App {

    private static JFrame currentView; // form hiện tại
    private static DTTP client;        // kết nối socket
    private static AuthService authService; // service dùng chung

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Store store = Store.getInstance();

                // --- Đăng ký reducer ---
                AuthReducer.register(store);
                EmployeeReducer.register(store);
                ProductReducer.register(store);
                BranchReducer.register(store);
                CustomerReducer.register(store);
                //CategoryReducer.register(store);
                // --- Kết nối socket ---
                client = new DTTP("127.0.0.1", 2025);
                client.listen();

                // --- Middleware ---
                SystemMiddleWare.start(client);
                SystemMiddleWare.handleDifferenceLogin(client);

                // --- Service xác thực ---
                authService = new AuthService(client);

                // --- Giao diện đăng nhập ---
                openLoginForm();

                // --- Theo dõi thay đổi state ---
                store.subcribe(App::handleStateChange);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Không thể kết nối server: " + e.getMessage());
            }
        });
    }

    /**
     * Mở form đăng nhập
     */
    private static void openLoginForm() {
        if (currentView != null) {
            currentView.dispose();
        }

        LoginForm view = new LoginForm();
        new LoginController(view, authService);
        currentView = view;
        currentView.setVisible(true);
    }

    /**
     * Mở form admin
     */
    private static void openAdminForm(UserModel user) {
        if (currentView != null) {
            currentView.dispose();
        }

        AdminForm adminView = new AdminForm(user);
        new AdminController(adminView, client);
        currentView = adminView;
        currentView.setVisible(true);
    }

    /**
     * Mở form nhân viên (POS)
     */
    private static void openStaffForm(UserModel user) {
        if (currentView != null) {
            currentView.dispose();
        }

        POSForm posForm = new POSForm(user);
        new POSController(posForm, client);
        currentView = posForm;
        currentView.setVisible(true);
    }

    /**
     * Theo dõi state trong Store để điều hướng UI
     */
    private static void handleStateChange(AppState state) {
        Boolean isAuth = (Boolean) state.get("isAuthenticated");
        Boolean isLogout = (Boolean) state.get("isLogout");
        Boolean isLossConnectionServer = (Boolean) state.get("isLossConnectionServer");
        Boolean isDoubleConnection = (Boolean) state.get("isDoubleConnection");
        Object userObj = state.get("user");

        SwingUtilities.invokeLater(() -> {
            // ✅ Chỉ mở form admin/POS khi đăng nhập lần đầu
            if (Boolean.TRUE.equals(isAuth)
                    && userObj instanceof UserModel user
                    && !(currentView instanceof AdminForm)
                    && !(currentView instanceof POSForm)) {

                System.out.println("[DEBUG] Opening AdminForm controller initialized");

                if ("ADMIN".equals(user.getRole())) {
                    openAdminForm(user);
                } else {
                    openStaffForm(user);
                }
            }

            // ✅ Xử lý đăng xuất
            if (Boolean.TRUE.equals(isLogout)) {
                client.send("LOGOUT", null, "REQUEST", "message");
                openLoginForm();

                Store store = Store.getInstance();
                store.getAppState().set("user", null);
                store.getAppState().set("isAuthenticated", false);
                store.getAppState().set("isLogout", false);
            }

            // ✅ Xử lý mất kết nối hoặc đăng nhập trùng
            if (Boolean.TRUE.equals(isLossConnectionServer)
                    || Boolean.TRUE.equals(isDoubleConnection)) {

                if (!(currentView instanceof LoginForm)) {
                    currentView.dispose();
                    openLoginForm();
                }

                Store store = Store.getInstance();
                store.getAppState().set("isLossConnectionServer", false);
                store.getAppState().set("isDoubleConnection", false);
            }
        });
    }
}
