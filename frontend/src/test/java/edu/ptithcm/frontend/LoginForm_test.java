package edu.ptithcm.frontend;

import edu.ptithcm.frontend.protocols.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Giao diện Đăng nhập (Login_dttp) - Gửi yêu cầu đăng nhập qua DTTP (socket) -
 * Nhận phản hồi từ backend - Tự động mở Dashboard phù hợp (ADMIN / STAFF)
 */
public class LoginForm_test extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cbRole;
    private DTTP client;

    public LoginForm_test() {
        setTitle("Đăng nhập hệ thống - Quản lý Bán hàng (DTTP Demo)");
        setSize(380, 230);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("QUẢN LÝ BÁN HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        panel.add(new JLabel("Tài khoản (Username):"));
        txtUser = new JTextField("admin");
        panel.add(txtUser);

        panel.add(new JLabel("Mật khẩu (Password):"));
        txtPass = new JPasswordField("123456");
        panel.add(txtPass);

        panel.add(new JLabel("Vai trò (Role):"));
        cbRole = new JComboBox<>(new String[]{"ADMIN", "STAFF"});
        panel.add(cbRole);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.WHITE);

        JButton btnExit = new JButton("Thoát");

        panel.add(btnLogin);
        panel.add(btnExit);

        add(panel, BorderLayout.CENTER);

        // Xử lý sự kiện
        btnLogin.addActionListener(e -> handleLogin());
        btnExit.addActionListener(e -> System.exit(0));

        initDTTPClient();
    }

    /**
     * Kết nối DTTP client tới backend (localhost:9090)
     */
    private void initDTTPClient() {
        try {
            client = new DTTP("localhost", 2025);
            client.on("LOGIN", data -> SwingUtilities.invokeLater(() -> onLoginResponse(data)));
            client.listen();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Không thể kết nối server! Sẽ dùng chế độ demo offline.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            client = null;
        }
    }

    /**
     * Gửi yêu cầu đăng nhập qua DTTP
     */
    private void handleLogin() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        String role = (String) cbRole.getSelectedItem();

        if (client == null) {
            // Offline mode demo
            if (user.equals("admin") && pass.equals("123456") && role.equals("ADMIN")) {
                openAdmin();
            } else if (user.equals("staff") && pass.equals("123456") && role.equals("STAFF")) {
                openStaff();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        try {
            Map<String, Object> data = new HashMap<>();
            data.put("username", user);
            data.put("password", pass);
            data.put("role", role);
            client.send("LOGIN", data, "REQUEST", "Yêu cầu đăng nhập");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi gửi dữ liệu đến server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Nhận phản hồi LOGIN từ server
     */
    private void onLoginResponse(Map<String, Object> data) {
        if (data == null) {
            JOptionPane.showMessageDialog(this, "Phản hồi rỗng từ server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String status = (String) data.getOrDefault("status", "ERROR");
        String message = (String) data.getOrDefault("message", "Không có thông báo");

        if ("OK".equalsIgnoreCase(status)) {
            String role = (String) data.getOrDefault("role", "STAFF");
            JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            if ("ADMIN".equalsIgnoreCase(role)) {
                openAdmin();
            } else {
                openStaff();
            }
        } else {
            JOptionPane.showMessageDialog(this, message, "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAdmin() {
        new AdminDashboard_test().setVisible(true);
    }

    private void openStaff() {
        new StaffDashboard_test().setVisible(true);
    }
}
