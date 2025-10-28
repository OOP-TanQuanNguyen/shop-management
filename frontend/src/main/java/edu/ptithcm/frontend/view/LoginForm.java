package edu.ptithcm.frontend.view;

import javax.swing.*;
import java.awt.*;
import edu.ptithcm.frontend.controllers.LoginController;

/**
 * Giao diện đăng nhập. Validation: Chỉ kiểm tra không để trống.
 */
public class LoginForm extends JFrame {

    private static final String TITLE = "Đăng nhập hệ thống";
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;

    private final LoginController controller;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnExit;

    public LoginForm(LoginController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("LoginController cannot be null");
        }
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setupWindow();

        JPanel mainPanel = createMainPanel();
        add(mainPanel);
    }

    private void setupWindow() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        panel.add(createHeaderPanel(), BorderLayout.NORTH);
        panel.add(createFormPanel(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 128, 128));
        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, 100));

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        panel.add(title);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.LIGHT_GRAY);

        addFormFields(panel);
        addButtons(panel);

        return panel;
    }

    private void addFormFields(JPanel panel) {
        JLabel lblUsername = createLabel("Tên đăng nhập", 120, 50);
        txtUsername = createTextField(280, 50);
        panel.add(lblUsername);
        panel.add(txtUsername);

        JLabel lblPassword = createLabel("Mật khẩu", 120, 100);
        txtPassword = createPasswordField(280, 100);
        panel.add(lblPassword);
        panel.add(txtPassword);
    }

    private void addButtons(JPanel panel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setBounds(0, 160, WINDOW_WIDTH, 80);

        btnLogin = createButton("Đăng nhập", new Color(100, 149, 237));
        btnExit = createButton("Thoát", new Color(220, 20, 60));

        btnLogin.addActionListener(e -> handleLogin());
        btnExit.addActionListener(e -> System.exit(0));
        txtPassword.addActionListener(e -> handleLogin());

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);
        panel.add(buttonPanel);
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBounds(x, y, 150, 30);
        return label;
    }

    private JTextField createTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBounds(x, y, 250, 30);
        return field;
    }

    private JPasswordField createPasswordField(int x, int y) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBounds(x, y, 250, 30);
        return field;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập!");
            txtUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showError("Vui lòng nhập mật khẩu!");
            txtPassword.requestFocus();
            return;
        }

        setButtonsEnabled(false);

        controller.login(username, password, (success, message) -> {
            SwingUtilities.invokeLater(() -> {
                setButtonsEnabled(true);

                if (success) {
                    showSuccess(message);
                    dispose();
                    // TODO: Mở MainForm
                } else {
                    showError(message);
                    txtPassword.setText("");
                    txtUsername.requestFocus();
                }
            });
        });
    }

    private void setButtonsEnabled(boolean enabled) {
        btnLogin.setEnabled(enabled);
        btnExit.setEnabled(enabled);
        txtUsername.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}
