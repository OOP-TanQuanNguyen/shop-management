package edu.ptithcm.views.admin;

import edu.ptithcm.models.InventoryModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {

    private JTable table;
    private JButton btnAdd, btnEdit, btnDelete, btnReload;

    // ===== NEW: FILTER COMPONENTS =====
    private JComboBox<String> cbBranchFilter;
    private JComboBox<String> cbProductFilter;

    private JButton btnFilter;

    private DefaultTableModel model;

    // ===== NEW: Label hiển thị thông báo =====
    private JLabel lblMessage;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ===== TITLE =====
        JLabel title = new JLabel("📦 Quản lý kho hàng", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        // ===== MESSAGE LABEL =====
        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(200, 0, 0));
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMessage.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.add(title, BorderLayout.NORTH);
        topWrapper.add(lblMessage, BorderLayout.SOUTH);

        add(topWrapper, BorderLayout.NORTH);

        // =======================================================
        // NEW: FILTER BAR (Branch + Product)
        // =======================================================
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        cbBranchFilter = new JComboBox<>();
        cbBranchFilter.addItem("Tất cả chi nhánh");

        cbProductFilter = new JComboBox<>();
        cbProductFilter.addItem("Tất cả sản phẩm");

        btnFilter = new JButton("🔍 Lọc");

        filterPanel.add(new JLabel("Chi nhánh:"));
        filterPanel.add(cbBranchFilter);

        filterPanel.add(new JLabel("Sản phẩm:"));
        filterPanel.add(cbProductFilter);

        filterPanel.add(btnFilter);

        add(filterPanel, BorderLayout.BEFORE_FIRST_LINE);

        // =======================================================
        // BUTTON BAR
        // =======================================================
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnAdd = new JButton("➕ Thêm");
        btnEdit = new JButton("✏️ Sửa");
        btnDelete = new JButton("🗑 Xóa");
        btnReload = new JButton("🔄 Tải lại");

        bottomBar.add(btnAdd);
        bottomBar.add(btnEdit);
        bottomBar.add(btnDelete);
        bottomBar.add(btnReload);

        add(bottomBar, BorderLayout.SOUTH);

        // =======================================================
        // TABLE
        // =======================================================
        String[] columns = {"Chi nhánh", "Sản phẩm", "Số lượng"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // -----------------------------------------------------------
    // GETTERS
    // -----------------------------------------------------------
    public JTable getTable() {
        return table;
    }

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

    public JComboBox<String> getCbBranchFilter() {
        return cbBranchFilter;
    }

    public JComboBox<String> getCbProductFilter() {
        return cbProductFilter;
    }

    // -----------------------------------------------------------
    // MESSAGE PANEL
    // -----------------------------------------------------------
    public void showMessage(String msg) {
        lblMessage.setText(msg);
    }

    public void clearMessage() {
        lblMessage.setText("");
    }

    // -----------------------------------------------------------
    // UPDATE TABLE
    // -----------------------------------------------------------
    public void updateTable(List<InventoryModel> list) {
        model.setRowCount(0);
        if (list == null) {
            return;
        }

        for (InventoryModel item : list) {
            model.addRow(new Object[]{
                item.getBranchName(),
                item.getProductName(),
                item.getQuantity()
            });
        }
    }

    // -----------------------------------------------------------
    // UPDATE COMBOBOX DATA (FROM CONTROLLER)
    // -----------------------------------------------------------
    public void updateBranchFilter(List<String> branches) {
        cbBranchFilter.removeAllItems();
        cbBranchFilter.addItem("Tất cả chi nhánh");
        for (String b : branches) {
            cbBranchFilter.addItem(b);
        }
    }

    public void updateProductFilter(List<String> products) {
        cbProductFilter.removeAllItems();
        cbProductFilter.addItem("Tất cả sản phẩm");
        for (String p : products) {
            cbProductFilter.addItem(p);
        }
    }
}
