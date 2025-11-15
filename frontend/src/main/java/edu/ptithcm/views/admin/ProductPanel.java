package edu.ptithcm.views.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import edu.ptithcm.models.ProductInfo;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JPanel {

    private JTable table;
    private JButton btnAdd, btnEdit, btnDelete, btnReload;
    private DefaultTableModel model;

    public ProductPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // Title
        JLabel title = new JLabel("📦 Quản lý sản phẩm", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Button bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnAdd = new JButton("➕ Thêm");
        btnEdit = new JButton("✏️ Sửa");
        btnDelete = new JButton("🗑️ Xóa");
        btnReload = new JButton("🔄 Tải lại");

        topBar.add(btnAdd);
        topBar.add(btnEdit);
        topBar.add(btnDelete);
        topBar.add(btnReload);
        add(topBar, BorderLayout.SOUTH);

        // Table
        String[] columns = {
            "Mã SP",
            "Tên sản phẩm",
            "Danh mục",
            "Giá vốn",
            "Giá bán",
            "Hạn dùng",
            "Trạng thái"
        };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    // ============================================================
    // Getters for Buttons
    // ============================================================
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

    public JTable getTable() {
        return table;
    }

    // ============================================================
    // Update Table Data
    // ============================================================
    public void updateTable(List<ProductInfo> products) {
        model.setRowCount(0);

        if (products == null || products.isEmpty()) {
            return;
        }

        for (ProductInfo product : products) {
            Object[] row = new Object[]{
                product.getId(),
                product.getName(),
                product.getCategoryName() != null ? product.getCategoryName() : "N/A",
                formatPrice(product.getCostPrice()),
                formatPrice(product.getSellPrice()),
                product.getExpiryDate() != null ? product.getExpiryDate() : "N/A",
                product.getIsActive() ? "Đang bán" : "Ngừng bán"
            };
            model.addRow(row);
        }
    }

    // ============================================================
    // Helper Methods
    // ============================================================
    private String formatPrice(Double price) {
        if (price == null) {
            return "0";
        }
        return String.format("%,.0f đ", price);
    }
}
