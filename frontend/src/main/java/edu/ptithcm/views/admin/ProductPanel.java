package edu.ptithcm.views.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import edu.ptithcm.models.ProductInfo;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {

    private JTable table;

    // === BUTTONS ===
    private JButton btnAdd, btnEdit, btnDelete, btnReload, btnFilter;

    // === FILTER FIELDS ===
    private JTextField txtSearch;              // Tìm theo tên
    private JComboBox<String> cbCategory;      // Lọc theo danh mục
    private JComboBox<String> cbStatus;        // Lọc theo trạng thái

    private DefaultTableModel model;

    public ProductPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ===== TITLE =====
        JLabel title = new JLabel("📦 Quản lý sản phẩm", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // =====================================================================
        // SEARCH BAR — GIỐNG NHÂN VIÊN
        // =====================================================================
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        txtSearch = new JTextField(15);
        txtSearch.setToolTipText("Nhập tên sản phẩm...");

        cbCategory = new JComboBox<>();
        cbCategory.addItem("Tất cả");

        cbStatus = new JComboBox<>(new String[]{
            "Tất cả", "Đang bán", "Ngừng bán"
        });

        btnFilter = new JButton("🔍 Lọc");

        searchBar.add(new JLabel("Tìm kiếm:"));
        searchBar.add(txtSearch);

        searchBar.add(new JLabel("Danh mục:"));
        searchBar.add(cbCategory);

        searchBar.add(new JLabel("Trạng thái:"));
        searchBar.add(cbStatus);

        searchBar.add(btnFilter);

        add(searchBar, BorderLayout.NORTH);

        // =====================================================================
        // BUTTON BAR
        // =====================================================================
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnAdd = new JButton(" Thêm");
        btnEdit = new JButton(" Sửa");
        btnDelete = new JButton(" Xóa");
        btnReload = new JButton(" Tải lại");

        topBar.add(btnAdd);
        topBar.add(btnEdit);
        topBar.add(btnDelete);
        topBar.add(btnReload);

        add(topBar, BorderLayout.SOUTH);

        // =====================================================================
        // TABLE
        // =====================================================================
        String[] columns = {
            "Tên sản phẩm",
            "Danh mục",
            "Giá vốn",
            "Giá bán",
            "Hạn dùng",
            "Trạng thái"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // =====================================================================
    // Getters — Controller sẽ dùng
    // =====================================================================
    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnReload() {
        return btnReload;
    }

    public JButton getBtnFilter() {
        return btnFilter;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JComboBox<String> getCbCategory() {
        return cbCategory;
    }

    public JComboBox<String> getCbStatus() {
        return cbStatus;
    }

    public JTable getTable() {
        return table;
    }

    // =====================================================================
    // Update danh sách category vào combo filter
    // =====================================================================
    public void updateCategoryFilter(List<String> categoryNames) {
        cbCategory.removeAllItems();
        cbCategory.addItem("Tất cả");

        if (categoryNames != null) {
            for (String name : categoryNames) {
                cbCategory.addItem(name);
            }
        }
    }

    // =====================================================================
    // Update Table
    // =====================================================================
    public void updateTable(List<ProductInfo> products) {
        model.setRowCount(0);
        if (products == null) {
            return;
        }

        for (ProductInfo p : products) {
            model.addRow(new Object[]{
                p.getName(),
                p.getCategoryName() != null ? p.getCategoryName() : "N/A",
                formatPrice(p.getCostPrice()),
                formatPrice(p.getSellPrice()),
                p.getExpiryDate() != null ? p.getExpiryDate() : "N/A",
                p.getIsActive() ? "Đang bán" : "Ngừng bán"
            });
        }
    }

    private String formatPrice(Double price) {
        if (price == null) {
            return "0 đ";
        }
        return String.format("%,.0f đ", price);
    }
}
