package edu.ptithcm.app;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.ptithcm.app.reducers.AuthReducer;
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
 * App: Điểm khởi động toàn bộ chương trình
 * - Đăng ký reducer vào Store
 * - Theo dõi state để chuyển view (Login <-> Admin)
 * - Tạo và truyền các service/controller cần thiết
 */
public class App {
    private static JFrame currentView; // form hiện tại
    private static DTTP client;        // kết nối socket
    private static AuthService authService; // service dùng chung

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AuthReducer.register(Store.getInstance());
                client = new DTTP("127.0.0.1", 2025);
                client.listen();

                //middleware
                SystemMiddleWare.start(client);
<<<<<<< HEAD
                SystemMiddleWare.handleDifferenceLogin(client);
=======
>>>>>>> 179bce8e7583fd747eda0e28b5de8d2397de3efc

                authService = new AuthService(client);
                openLoginForm();
                Store.getInstance().subcribe(App::handleStateChange);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, " Không thể kết nối server: " + e.getMessage());
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

        AdminForm adminView = new AdminForm(user);
        new AdminController(adminView);
        currentView = adminView;
        currentView.setVisible(true);
    }

    private static void openStaffForm(UserModel user){
        if (currentView != null) currentView.dispose();
        
        POSForm posForm = new POSForm(user);
        new POSController(posForm);
        currentView = posForm;
        currentView.setVisible(true);
    }


    /** Theo dõi state trong Store để điều hướng UI */
    private static void handleStateChange(AppState state) {
        System.out.println("[DEBUG] Opening AdminForm controller initialized");
        Boolean isAuth = (Boolean) state.get("isAuthenticated");
        Boolean isLogout = (Boolean) state.get("isLogout");
        Boolean isLossConnectionServer = (Boolean) state.get("isLossConnectionServer");
<<<<<<< HEAD
        Boolean isDoubleConnection = (Boolean)state.get("isDoubleConnection");
=======
>>>>>>> 179bce8e7583fd747eda0e28b5de8d2397de3efc
        Object userObj = state.get("user");

        SwingUtilities.invokeLater(() -> {
            if (Boolean.TRUE.equals(isAuth) && userObj instanceof UserModel user) {
                System.out.println(user.getRole());
                if (user.getRole().equals("ADMIN")){
                    openAdminForm(user);
                }
                else{
                    openStaffForm(user);
                }
            }
            if (Boolean.TRUE.equals(isLogout)){
                try {
                    client.send("LOGOUT", null, "REQUEST", "message");
                }catch(IOException e) {
                    e.printStackTrace();   
                }
                openLoginForm();

                Store store = Store.getInstance();
                store.getAppState().set("user", null);
                store.getAppState().set("isAuthenticated", false);
                store.getAppState().set("isLogout", false);
            }
            if (Boolean.TRUE.equals(isLossConnectionServer)){
                if (!(currentView instanceof LoginForm)){
                    currentView.dispose();
                    openLoginForm();
                }
            }

            if (Boolean.TRUE.equals(isDoubleConnection)){
                if (!(currentView instanceof LoginForm)){
                    currentView.dispose();
                    openLoginForm();
                    Store.getInstance().getAppState().set("isDoubleConnection",false);
                }
            }
            if (Boolean.TRUE.equals(isLossConnectionServer)){
                if (!(currentView instanceof LoginForm)){
                    currentView.dispose();
                    openLoginForm();
                }
            }
        });
    }
}
