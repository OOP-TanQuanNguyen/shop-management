// ==========================================
// File: StaffDashboard_test.java (Staff Dashboard)
// Project: Quản lý bán hàng (Frontend Demo Only)
// Author: Gemini
// ==========================================
package edu.ptithcm.frontend;
// <-- Khai báo package đã được thêm

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Giao diện chính dành cho Nhân viên bán hàng (Staff). Bao gồm các tab Bán
 * hàng, Hóa đơn, Khách hàng và Tồn kho.
 */
class StaffDashboard_test extends JFrame {

    public StaffDashboard_test() {
        setTitle("Staff Dashboard - Nhân viên bán hàng");
        setSize(900, 600);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel (Logout functionality)
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Chào mừng, Staff! (Nhân viên bán hàng)", JLabel.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(e -> handleLogout());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabs.addTab("Bán hàng / Tạo đơn", buildSellPanel());
        tabs.addTab("Hóa đơn cá nhân", buildInvoicePanel());
        tabs.addTab("Khách hàng", buildCustomerPanel());
        tabs.addTab("Tồn kho (Xem)", buildStockPanel());

        add(tabs, BorderLayout.CENTER);
    }

    /**
     * Xử lý đăng xuất (quay lại màn hình Login).
     */
    private void handleLogout() {
        dispose();
        // Cần khởi tạo LoginForm, giả định nó cũng nằm trong package này
        new LoginForm_test().setVisible(true);
    }

    // --- 1. Bán hàng / Tạo đơn ---
    private JPanel buildSellPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input & Summary Panel
        JPanel top = new JPanel(new BorderLayout(10, 10));
        JPanel inputGrid = new JPanel(new GridLayout(3, 4, 10, 5));
        inputGrid.setBorder(BorderFactory.createTitledBorder("Thông tin đơn hàng"));

        inputGrid.add(new JLabel("Mã HĐ:"));
        inputGrid.add(new JTextField("HD-001-AUTO"));
        inputGrid.add(new JLabel("Khách hàng (SĐT):"));
        inputGrid.add(new JTextField("09x-xxx-xxxx"));

        inputGrid.add(new JLabel("Sản phẩm:"));
        inputGrid.add(new JComboBox<>(new String[]{"SP01 - Laptop Dell", "SP02 - Chuột Logitech", "SP03 - Bàn phím cơ"}));
        inputGrid.add(new JLabel("Số lượng:"));
        inputGrid.add(new JTextField("1"));

        inputGrid.add(new JLabel("Giảm giá (%):"));
        inputGrid.add(new JTextField("0"));
        inputGrid.add(new JLabel("Điểm tích lũy:"));
        inputGrid.add(new JTextField("0"));

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.add(new JLabel("Tạm tính: 15,000,000 VNĐ", JLabel.RIGHT));
        summaryPanel.add(new JLabel("VAT (10%): 1,500,000 VNĐ", JLabel.RIGHT));
        JLabel lblTotal = new JLabel("TỔNG THANH TOÁN: 16,500,000 VNĐ", JLabel.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryPanel.add(lblTotal);

        top.add(inputGrid, BorderLayout.NORTH);
        top.add(summaryPanel, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);

        // Order Items Table
        String[] cols = {"Mã SP", "Tên SP", "SL", "Đơn giá", "Thành tiền"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        model.addRow(new Object[]{"SP01", "Laptop Dell", 1, "15,000,000", "15,000,000"});
        JTable table = new JTable(model);
        table.setRowHeight(25);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btns.add(new JButton("Thêm vào đơn"));
        btns.add(new JButton("Xóa mục"));
        JButton btnCheckout = new JButton("THANH TOÁN & In hóa đơn");
        btnCheckout.setBackground(new Color(40, 167, 69));
        btnCheckout.setForeground(Color.WHITE);
        btns.add(btnCheckout);

        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    // --- 2. Hóa đơn cá nhân ---
    private JPanel buildInvoicePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search/Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.add(new JLabel("Tìm kiếm (Mã HĐ/SĐT KH):"));
        filterPanel.add(new JTextField(20));
        filterPanel.add(new JButton("Tìm"));
        filterPanel.add(new JButton("In lại HĐ"));

        panel.add(filterPanel, BorderLayout.NORTH);

        // Table
        DefaultTableModel modelHD = new DefaultTableModel(new Object[]{"Mã HĐ", "Khách hàng", "Ngày", "Tổng tiền (VNĐ)", "Nhân viên"}, 0);
        modelHD.addRow(new Object[]{"HD001", "Trần Văn B (09x-xxx-xxxx)", "2025-10-22", "16,500,000", "staff"});
        modelHD.addRow(new Object[]{"HD002", "Lê Thị D (09y-yyy-yyyy)", "2025-10-23", "3,500,000", "staff"});
        JTable tblHoaDon = new JTable(modelHD);
        tblHoaDon.setRowHeight(25);
        panel.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        return panel;
    }

    // --- 3. Khách hàng ---
    private JPanel buildCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input Panel (Add/Edit Customer)
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Khách hàng"));
        inputPanel.add(new JLabel("Mã KH (SĐT):"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Tên KH:"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Địa chỉ:"));
        inputPanel.add(new JTextField());
        inputPanel.add(new JLabel("Điểm thưởng:"));
        inputPanel.add(new JTextField("0"));

        panel.add(inputPanel, BorderLayout.NORTH);

        // Table
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Mã KH (SĐT)", "Tên KH", "Địa chỉ", "Điểm thưởng"}, 0);
        model.addRow(new Object[]{"09x-xxx-xxxx", "Trần Văn B", "Hà Nội", 120});
        JTable tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(25);
        panel.add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

        // Buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttons.add(new JButton("Thêm Khách hàng mới"));
        buttons.add(new JButton("Cập nhật thông tin"));
        buttons.add(new JButton("Xem lịch sử mua"));
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // --- 4. Tồn kho (Chỉ xem) ---
    private JPanel buildStockPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.add(new JLabel("Tìm kiếm (Mã/Tên SP):"));
        filterPanel.add(new JTextField(20));
        filterPanel.add(new JButton("Tìm"));

        panel.add(filterPanel, BorderLayout.NORTH);

        // Table
        DefaultTableModel modelKho = new DefaultTableModel(new Object[]{"Mã SP", "Tên SP", "Giá bán (VNĐ)", "Số lượng tồn"}, 0);
        modelKho.addRow(new Object[]{"SP01", "Laptop Dell XPS 13", "35,000,000", 5});
        modelKho.addRow(new Object[]{"SP02", "Chuột Logitech G Pro", "1,800,000", 12});
        modelKho.addRow(new Object[]{"SP03", "Bàn phím cơ Redragon", "1,200,000", 20});
        JTable tblKho = new JTable(modelKho);
        tblKho.setRowHeight(25);

        JLabel lblNotice = new JLabel("LƯU Ý: Nhân viên chỉ được xem thông tin tồn kho, không có quyền chỉnh sửa.", JLabel.CENTER);
        lblNotice.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        panel.add(new JScrollPane(tblKho), BorderLayout.CENTER);
        panel.add(lblNotice, BorderLayout.SOUTH);

        return panel;
    }
}
