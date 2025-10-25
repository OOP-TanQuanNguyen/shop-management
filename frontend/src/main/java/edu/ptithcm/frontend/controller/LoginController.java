package edu.ptithcm.frontend.controller;

import edu.ptithcm.frontend.protocols.DTTP;
import edu.ptithcm.frontend.services.LoginService;
import edu.ptithcm.frontend.view.AdminDashboard;
import edu.ptithcm.frontend.view.LoginForm;
import edu.ptithcm.frontend.view.StaffDashboard;
import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class LoginController {

    private final LoginForm view;
    private final LoginService service;

    public LoginController(LoginForm view) {
        this.view = view;
        this.service = new LoginService(this::onLoginResponse);
    }

    public void handleLogin(String username, String password) {
        try {
            service.sendLogin(username, password);
        } catch (IOException e) {
            view.showMessage("Không thể kết nối server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onLoginResponse(Map<String, Object> data) {
        String status = (String) data.getOrDefault("status", "ERROR");
        String message = (String) data.getOrDefault("message", "Không có phản hồi từ server");

        if ("OK".equalsIgnoreCase(status)) {
            String role = (String) data.getOrDefault("role", "STAFF");
            view.showMessage(message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            view.close();
            if ("ADMIN".equalsIgnoreCase(role)) {
                new AdminDashboard(service.getClient()).setVisible(true); 
            }else {
                new StaffDashboard(service.getClient()).setVisible(true);
            }
        } else {
            view.showMessage(message, "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }
}
