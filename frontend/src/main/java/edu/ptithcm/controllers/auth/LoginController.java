package edu.ptithcm.controllers.auth;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.services.authentication.AuthService;
import edu.ptithcm.views.components.AppMessageBox;
import edu.ptithcm.views.login.LoginForm;

public class LoginController {
    private final LoginForm view;
    private final AuthService authService;
    private final Store store = Store.getInstance();

    public LoginController(LoginForm view, AuthService authService) {
        this.view = view;
        this.authService = authService;
        this.registerEvent();
        Store.getInstance().subcribe(this::handleState);
    }

    private void registerEvent() {
        view.getBtnLogin().addActionListener(e -> handleLogin());
        view.getBtnExit().addActionListener(e -> System.exit(0));
    }

    private void handleLogin() {
        String user = view.getUsername();
        String pass = view.getPassword();

        if (user.isEmpty()) {
            AppMessageBox.showWarning("Tên đăng nhập không được để trống!");
            return;
        }
        try {
            authService.login(user, pass); // chỉ gửi, không chờ return
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Lỗi gửi yêu cầu: " + ex.getMessage());
        }
    }

    private void handleState(AppState state){
        Boolean isAuth = (Boolean) state.get("isAuthenticated");
        SwingUtilities.invokeLater(() -> {
            if (Boolean.FALSE.equals(isAuth)) {
                view.resetInputPassword();
            }
        });
    }
}
