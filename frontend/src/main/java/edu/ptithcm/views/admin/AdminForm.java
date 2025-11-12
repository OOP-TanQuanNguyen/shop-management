package edu.ptithcm.views.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import edu.ptithcm.models.UserModel;

public class AdminForm extends JFrame {

    private final JTabbedPane tabPane = new JTabbedPane();
    private final JButton btnLogout = new JButton(" 🚪 Đăng xuất");

    // 👉 THÊM DÒNG NÀY:
    private final EmployeePanel employeePanel = new EmployeePanel();

    public AdminForm(UserModel userData) {
        setTitle("🏢 Hệ thống quản trị mini market");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 102, 102));
        header.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel lblTitle = new JLabel("QUẢN LÝ HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.CENTER);

        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorder(new LineBorder(new Color(180, 35, 50)));
        header.add(btnLogout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Tabs
        tabPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabPane.addTab("👤 Nhân viên", employeePanel); // dùng biến
        tabPane.addTab("📦 Sản phẩm", new ProductPanel());
        tabPane.addTab("🏬 Chi nhánh", new BranchPanel());
        tabPane.addTab("📈 Thống kê", new StatisticPanel());
        tabPane.addTab("⚙️ Cài đặt", new SettingPanel());
        add(tabPane, BorderLayout.CENTER);
    }

    public JButton getLogoutButton() {
        return btnLogout;
    }

    // 👉 THÊM GETTER NÀY:
    public EmployeePanel getEmployeePanel() {
        return employeePanel;
    }
}
