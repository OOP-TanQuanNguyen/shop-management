package edu.ptithcm.frontend.view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StaffForm extends JFrame {

    private final Map<String, Object> userData;

    public StaffForm(Map<String, Object> userData) {
        this.userData = userData;
        initComponents();
    }

    private void initComponents() {
        setTitle("🧑‍💼 Giao diện nhân viên - STAFF");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 153, 51));
        header.setPreferredSize(new Dimension(900, 80));

        JLabel lblTitle = new JLabel("BẢNG ĐIỀU KHIỂN NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> handleLogout());
        header.add(btnLogout, BorderLayout.EAST);

        // Thông tin user
        JPanel infoPanel = createInfoPanel();

        // Khu vực nội dung chính (placeholder)
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(new JLabel("Chức năng STAFF ở đây (bán hàng, kiểm kho, ...)."));

        add(header, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));

        panel.add(createLabel("👤 " + userData.get("name")));
        panel.add(createLabel("🆔 Username: " + userData.get("username")));
        panel.add(createLabel("📞 Phone: " + userData.get("phone")));
        panel.add(createLabel("🕒 Hired: " + userData.get("hireDate")));
        panel.add(createLabel("🎯 Role: " + userData.get("role")));

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return label;
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginForm(
                    new edu.ptithcm.frontend.controllers.LoginController(
                            new edu.ptithcm.frontend.services.AuthService(null)
                    )
            ).setVisible(true));
        }
    }
}
