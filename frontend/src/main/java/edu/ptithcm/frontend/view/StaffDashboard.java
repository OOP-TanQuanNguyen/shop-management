package edu.ptithcm.frontend.view;

import edu.ptithcm.frontend.controller.StaffController;
import edu.ptithcm.frontend.protocols.DTTP;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Giao diện nhân viên - Quản lý đơn hàng và hồ sơ cá nhân.
 */
public class StaffDashboard extends JFrame {

    private final StaffController controller;
    private DefaultTableModel orderModel; // Đã khởi tạo ngay trong constructor

    public StaffDashboard(DTTP dttp) {
        controller = new StaffController(this, dttp);

        setTitle("Nhân viên - Quản lý bán hàng");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("📦 Giao diện Nhân viên", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(e -> controller.logout());
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("🧾 Đơn hàng", buildOrderPanel());
        tabs.add("👤 Hồ sơ cá nhân", controller.buildProfilePanel());
        add(tabs, BorderLayout.CENTER);

        // Khởi tạo model (phải sau khi build)
        orderModel.setRowCount(0);
        controller.refreshOrders();
    }

    private JPanel buildOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Bảng đơn hàng
        String[] columns = {"Mã đơn", "Tên khách", "Tổng tiền", "Trạng thái", "Ngày tạo"};
        orderModel = new DefaultTableModel(columns, 0); // ✅ KHỞI TẠO Ở ĐÂY
        JTable table = new JTable(orderModel);
        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // Nút chức năng
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm đơn");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnPing = new JButton("Ping server");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnPing);

        // Gắn sự kiện
        btnAdd.addActionListener(e -> controller.addOrder());
        btnEdit.addActionListener(e -> controller.editOrder());
        btnDelete.addActionListener(e -> controller.deleteOrder());
        btnRefresh.addActionListener(e -> controller.refreshOrders());
        btnPing.addActionListener(e -> controller.pingServer());

        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Cập nhật danh sách hóa đơn trên bảng
     */
    public void updateInvoices(List<Map<String, Object>> invoices) {
        SwingUtilities.invokeLater(() -> {
            orderModel.setRowCount(0);
            if (invoices != null) {
                for (Map<String, Object> inv : invoices) {
                    orderModel.addRow(new Object[]{
                        inv.get("id"),
                        inv.get("customer"),
                        inv.get("total"),
                        inv.get("status"),
                        inv.get("date")
                    });
                }
            }
        });
    }

    /**
     * Hiển thị thông báo chung
     */
    public void showMessage(String msg, int type) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", type);
    }
}
