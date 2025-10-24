package edu.ptithcm.frontend;

import edu.ptithcm.frontend.admin.*;
import edu.ptithcm.frontend.protocols.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class AdminDashboard_test extends JFrame {

    private DTTP client;

    public AdminDashboard_test() {
        setTitle("Admin Dashboard - Chủ cửa hàng");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initDTTP();

        // Header
        JPanel header = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("👑 Xin chào, Admin!", JLabel.LEFT);
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(e -> handleLogout());
        header.add(lbl, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("Nhân viên", new EmployeePanel(client));
        tabs.addTab("Sản phẩm & Kho", new ProductPanel(client));
        tabs.addTab("Thống kê", new ReportPanel(client));
        tabs.addTab("Hệ thống", new SystemPanel(client, this));

        add(tabs, BorderLayout.CENTER);
    }

    private void initDTTP() {
        try {
            client = new DTTP("localhost", 9090);
            client.listen();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "⚠ Không thể kết nối server, dùng chế độ demo!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            client = null;
        }
    }

    private void handleLogout() {
        if (client != null) {
            client.stop();
        }
        dispose();
        new edu.ptithcm.frontend.LoginForm_test().setVisible(true);
    }
}
