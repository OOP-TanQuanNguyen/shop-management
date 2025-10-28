package edu.ptithcm.frontend.view;

import edu.ptithcm.frontend.controllers.LoginController;
import edu.ptithcm.frontend.protocols.DTTP;
import edu.ptithcm.frontend.services.AuthService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class AdminForm extends JFrame {

    private final Map<String, Object> userData;

    public AdminForm(Map<String, Object> userData) {
        this.userData = userData;
        initComponents();
    }

    private void initComponents() {
        setTitle("🏢 Hệ thống quản trị - ADMIN");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        // Toàn bộ font dùng Segoe UI
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 14));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(102, 204, 204)); // pastel
        header.setPreferredSize(new Dimension(1000, 60));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN TRỊ ADMIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(51, 153, 153));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> handleLogout());
        header.add(btnLogout, BorderLayout.EAST);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.addTab("Quản lý sản phẩm", createProductPanel());
        tabbedPane.addTab("Quản lý nhân viên", createEmployeePanel());
        tabbedPane.addTab("Thống kê doanh số", createStatsPanel());

        add(header, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    // === TAB 1: QUẢN LÝ SẢN PHẨM ===
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 255, 255));

        // --- Thông tin sản phẩm ---
        JPanel infoPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        infoPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        infoPanel.setBackground(new Color(230, 255, 255));

        infoPanel.add(new JLabel("Mã SP:"));
        infoPanel.add(new JTextField());
        infoPanel.add(new JLabel("Tên SP:"));
        infoPanel.add(new JTextField());
        infoPanel.add(new JLabel("Giá:"));
        infoPanel.add(new JTextField());
        infoPanel.add(new JLabel("Số lượng:"));
        infoPanel.add(new JTextField());

        JPanel buttonPanel = createButtonPanel();

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(230, 255, 255));
        panelTop.add(infoPanel, BorderLayout.CENTER);
        panelTop.add(buttonPanel, BorderLayout.SOUTH);

        String[] cols = {"Mã SP", "Tên SP", "Giá", "Số lượng"};
        Object[][] data = {
            {"SP001", "Bình sữa", "120,000", "50"},
            {"SP002", "Sách thiếu nhi", "80,000", "120"},
            {"SP003", "Máy giặt", "8,000,000", "10"},};
        JTable table = createStyledTable(data, cols);
        JScrollPane scroll = new JScrollPane(table);

        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // === TAB 2: QUẢN LÝ NHÂN VIÊN ===
    private JPanel createEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 255, 255));

        JPanel infoPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        infoPanel.setBorder(new EmptyBorder(15, 15, 10, 15));
        infoPanel.setBackground(new Color(230, 255, 255));

        infoPanel.add(new JLabel("Mã nhân viên:"));
        infoPanel.add(new JTextField());
        infoPanel.add(new JLabel("Tên nhân viên:"));
        infoPanel.add(new JTextField());
        infoPanel.add(new JLabel("Lương cơ bản:"));
        infoPanel.add(new JTextField());
        infoPanel.add(new JLabel("Phụ cấp:"));
        infoPanel.add(new JTextField());
        infoPanel.add(new JLabel("Tình trạng:"));
        infoPanel.add(new JComboBox<>(new String[]{"Đang làm", "Nghỉ việc"}));

        JPanel buttonPanel = createButtonPanel();

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(230, 255, 255));
        panelTop.add(infoPanel, BorderLayout.CENTER);
        panelTop.add(buttonPanel, BorderLayout.SOUTH);

        String[] cols = {"Mã NV", "Tên nhân viên", "Lương cơ bản", "Phụ cấp", "Tình trạng"};
        Object[][] data = {
            {"NV01", "Nguyễn Văn A", "12,000,000", "2,000,000", "Đang làm"},
            {"NV02", "Trần Thị B", "8,000,000", "500,000", "Đang làm"},
            {"NV03", "Lê Văn C", "9,000,000", "1,000,000", "Nghỉ việc"},};
        JTable table = createStyledTable(data, cols);
        JScrollPane scroll = new JScrollPane(table);

        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // === TAB 3: THỐNG KÊ DOANH SỐ ===
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 255, 255));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(230, 255, 255));
        topPanel.add(new JLabel("🔍 Tìm kiếm:"));
        topPanel.add(new JTextField(15));
        topPanel.add(new JButton("Thống kê theo ngày"));
        topPanel.add(new JButton("Sắp xếp theo doanh thu"));
        topPanel.add(new JButton("In danh sách"));
        topPanel.add(new JButton("Reset"));

        String[] cols = {"Mã hóa đơn", "Mã NV", "Tên NV", "Tên KH", "Ngày lập", "Thành tiền"};
        Object[][] data = {
            {"HD01", "NV01", "Nguyễn Văn A", "Khách 1", "2025-10-28", "12,500,000"},
            {"HD02", "NV02", "Trần Thị B", "Khách 2", "2025-10-27", "7,800,000"},
            {"HD03", "NV03", "Lê Văn C", "Khách 3", "2025-10-25", "9,200,000"},};
        JTable table = createStyledTable(data, cols);
        JScrollPane scroll = new JScrollPane(table);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(230, 255, 255));
        bottom.add(new JLabel("Số lượng hóa đơn: 3"));
        bottom.add(Box.createHorizontalStrut(20));
        bottom.add(new JLabel("Tổng doanh thu: 29,500,000 VNĐ"));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    // === NÚT CHỨC NĂNG CHUNG ===
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(230, 255, 255));

        JButton[] buttons = {
            new JButton("➕ Thêm"),
            new JButton("✏️ Sửa"),
            new JButton("🗑️ Xóa"),
            new JButton("🔄 Reset"),
            new JButton("🔍 Tìm kiếm")
        };

        for (JButton b : buttons) {
            b.setFocusPainted(false);
            b.setBackground(new Color(102, 204, 204));
            b.setForeground(Color.WHITE);
            b.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.add(b);
        }
        return panel;
    }

    // === TẠO TABLE ĐẸP ===
    private JTable createStyledTable(Object[][] data, String[] cols) {
        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(179, 255, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(220, 220, 220));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(102, 204, 204));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        table.addMouseMotionListener(new MouseAdapter() {
            int lastRow = -1;

            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != lastRow) {
                    table.repaint();
                    lastRow = row;
                }
            }
        });

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 255, 255));
                }
                return c;
            }
        });

        return table;
    }

    // === ĐĂNG XUẤT ===
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn đăng xuất?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Đóng cửa sổ AdminForm
            JOptionPane.showMessageDialog(null, "Đã đăng xuất!");

            // ✅ Gọi restartClient() thay vì tự tạo lại kết nối
            java.awt.EventQueue.invokeLater(() -> {
                edu.ptithcm.frontend.ClientMain.restartClient();
            });
        }
    }
}
