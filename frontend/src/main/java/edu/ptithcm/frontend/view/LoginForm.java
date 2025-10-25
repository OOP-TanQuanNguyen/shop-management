package edu.ptithcm.frontend.view;

import edu.ptithcm.frontend.controller.LoginController;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {

    private final JTextField txtUser;
    private final JPasswordField txtPass;
    private final LoginController controller;

    public LoginForm() {
        controller = new LoginController(this);

        setTitle("Đăng nhập - Quản lý bán hàng");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        panel.add(new JLabel("Tài khoản:"));
        txtUser = new JTextField();
        panel.add(txtUser);

        panel.add(new JLabel("Mật khẩu:"));
        txtPass = new JPasswordField();
        panel.add(txtPass);

        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnExit = new JButton("Thoát");
        panel.add(btnLogin);
        panel.add(btnExit);

        add(panel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> controller.handleLogin(txtUser.getText().trim(), new String(txtPass.getPassword())));
        btnExit.addActionListener(e -> System.exit(0));
    }

    public void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    public void close() {
        dispose();
    }
}
