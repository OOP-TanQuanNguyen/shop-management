package edu.ptithcm.frontend.controller;

import edu.ptithcm.frontend.protocols.DTTP;
import edu.ptithcm.frontend.services.LoginService;
import edu.ptithcm.frontend.view.AdminDashboard;
import edu.ptithcm.frontend.view.LoginForm;
import javax.swing.*;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public class LoginController {

    private final LoginForm view;
    private final LoginService service;
    private final DTTP client;

    public LoginController(LoginForm view) {
        this.view = view;
        // Khởi tạo Service, truyền phương thức xử lý phản hồi (callback)
        // Phương thức callback là handleLoginResponse(Map<String, Object>)
        this.service = new LoginService(this::handleLoginResponse);
        this.client = this.service.getClient(); // Lấy DTTP client (có thể là null nếu offline)
    }

    // =======================================================
    // 1. XỬ LÝ SỰ KIỆN NÚT BẤM (GỌI TỪ LoginForm)
    // ⭐️ PHƯƠNG THỨC NÀY KHẮC PHỤC LỖI KHÔNG TƯƠNG THÍCH KIỂU DỮ LIỆU
    // =======================================================
    public void handleLogin(String username, String password) {
        try {
            // Gọi Service để gửi yêu cầu đăng nhập
            service.sendLogin(username, password);
        } catch (IOException e) {
            // Xảy ra lỗi I/O khi cố gắng gửi yêu cầu (ví dụ: mất mạng giữa chừng)
            JOptionPane.showMessageDialog(view, "Lỗi giao tiếp mạng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =======================================================
    // 2. XỬ LÝ PHẢN HỒI (GỌI TỪ LoginService LÀM CALLBACK)
    // =======================================================
    private void handleLoginResponse(Map<String, Object> data) {
        // Luôn chạy trên Event Dispatch Thread để cập nhật UI an toàn
        SwingUtilities.invokeLater(() -> {
            String status = (String) data.getOrDefault("status", "ERROR");
            String message = (String) data.getOrDefault("message", "Lỗi không xác định");

            if ("OK".equals(status)) {
                String role = (String) data.getOrDefault("role", "STAFF");

                // Mở giao diện tương ứng
                if ("ADMIN".equals(role)) {
                    // Mở AdminDashboard và truyền client connection vào
                    new AdminDashboard(client).setVisible(true);
                    view.dispose(); // Đóng Login Form
                } else if ("STAFF".equals(role)) {
                    // Xử lý cho vai trò Staff (nếu có)
                    // new StaffDashboard(client).setVisible(true);
                    view.dispose();
                }
            } else {
                // Đăng nhập thất bại (Server/Demo gửi status ERROR)
                view.showMessage(message, "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
