package edu.ptithcm.frontend.view;

import edu.ptithcm.frontend.controller.AdminController;
import edu.ptithcm.frontend.protocols.DTTP;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AdminDashboard extends JFrame {

    private final AdminController controller;
    private final JTabbedPane tabs;

    // Khách hàng
    private final DefaultTableModel customerModel;
    private final JTable customerTable;

    // Nhân viên
    private final DefaultTableModel employeeModel;
    private final JTable employeeTable;

    // Sản phẩm
    private final DefaultTableModel productModel;
    private final JTable productTable;

    // Log
    private final JTextArea logArea;

    public AdminDashboard(DTTP client) {
        super("Quản lý Bán hàng - Admin");
        this.controller = new AdminController(this, client);

        // Khởi tạo Model và Table Khách hàng
        String[] customerColumns = {"Mã KH", "Tên khách hàng", "Số điện thoại", "Địa chỉ"};
        this.customerModel = new DefaultTableModel(customerColumns, 0);
        this.customerTable = new JTable(customerModel);

        // Khởi tạo Nhân viên
        String[] employeeColumns = {"Username", "Họ tên", "Vai trò", "Điện thoại", "Lương"};
        this.employeeModel = new DefaultTableModel(employeeColumns, 0);
        this.employeeTable = new JTable(employeeModel);

        // Khởi tạo Sản phẩm
        String[] productColumns = {"Mã SP", "Tên SP", "Giá", "Tồn kho"};
        this.productModel = new DefaultTableModel(productColumns, 0);
        this.productTable = new JTable(productModel);

        this.logArea = new JTextArea();
        this.logArea.setEditable(false);

        this.tabs = new JTabbedPane();
        tabs.add("👤 Nhân viên", buildEmployeePanel());
        tabs.add("📦 Sản phẩm", buildProductPanel());
        tabs.add("📊 Thống kê", buildReportPanel());

        tabs.add("👥 Khách hàng", buildCustomerPanel());

        tabs.add("📝 Log hệ thống", buildLogPanel());

        getContentPane().add(tabs);

        // Thiết lập JFrame
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tải dữ liệu ban đầu (KHẮC PHỤC LỖI: Cần AdminController có các phương thức này)
        controller.refreshEmployees();
        controller.refreshCustomers();
    }

    // ===================== XÂY DỰNG GIAO DIỆN CÁC PANELS =====================
    private JPanel buildEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnRefresh = new JButton("Làm mới");
        // KHẮC PHỤC LỖI: Gọi phương thức trong Controller
        btnRefresh.addActionListener(e -> controller.refreshEmployees());
        btnPanel.add(btnRefresh);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // KHẮC PHỤC LỖI: Đảm bảo có return JPanel
    private JPanel buildProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        return panel;
    }

    // KHẮC PHỤC LỖI: Đảm bảo có return JPanel
    private JPanel buildReportPanel() {
        return new JPanel();
    }

    private JPanel buildLogPanel() {
        JPanel panel = new JPanel(new BorderLayout()); // Khởi tạo JPanel
        panel.add(new JScrollPane(logArea), BorderLayout.CENTER); // Gọi hàm add() (void)
        return panel; // Trả về JPanel đã được thêm thành phần (ĐÚNG)
    }

    private JPanel buildCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JScrollPane scroll = new JScrollPane(customerTable);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("➕ Thêm khách");
        JButton btnDelete = new JButton("❌ Xóa");
        JButton btnRefresh = new JButton("🔄 Làm mới");

        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

        // Gắn sự kiện vào Controller (KHẮC PHỤC LỖI: Cần AdminController có các phương thức này)
        btnAdd.addActionListener(e -> controller.addCustomer());
        btnDelete.addActionListener(e -> controller.deleteCustomer());
        btnRefresh.addActionListener(e -> controller.refreshCustomers());

        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ===================== PHƯƠNG THỨC CẬP NHẬT VIEW =====================
    public void updateCustomers(List<Map<String, Object>> customers) {
        customerModel.setRowCount(0);
        if (customers != null) {
            for (Map<String, Object> cust : customers) {
                customerModel.addRow(new Object[]{
                    cust.get("id"),
                    cust.get("name"),
                    cust.get("phone"),
                    cust.get("address")
                });
            }
        }
    }

    public void updateEmployees(List<Map<String, Object>> employees) {
        employeeModel.setRowCount(0);
        if (employees != null) {
            for (Map<String, Object> emp : employees) {
                employeeModel.addRow(new Object[]{
                    emp.get("username"),
                    emp.get("fullname"),
                    emp.get("role"),
                    emp.get("phone"),
                    emp.get("salary")
                });
            }
        }
    }

    public void updateLog(String msg) {
        logArea.append(msg + "\n");
    }

    public void updateProducts(List<Map<String, Object>> products) {
        // Cần thêm logic cập nhật sản phẩm nếu muốn dùng
    }

    // ===================== GETTERS (CHO CONTROLLER TRUY CẬP) =====================
    public DefaultTableModel getCustomerTableModel() {
        return customerModel;
    }

    public JTable getCustomerTable() {
        return customerTable;
    }

    public void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", messageType);
    }
}
