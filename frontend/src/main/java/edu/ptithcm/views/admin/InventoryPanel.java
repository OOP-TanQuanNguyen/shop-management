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
    private DefaultTableModel model;

    // ===== NEW: Label hiển thị thông báo =====
    private JLabel lblMessage;

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ===== TITLE =====
        JLabel title = new JLabel("📦 Quản lý kho hàng", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        // ===== NEW: MESSAGE LABEL =====
        lblMessage = new JLabel("");
        lblMessage.setForeground(new Color(200, 0, 0)); // Màu đỏ cảnh báo
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMessage.setBorder(new EmptyBorder(5, 10, 5, 10));

        // Gói Title + Message vào 1 panel
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.add(title, BorderLayout.NORTH);
        topWrapper.add(lblMessage, BorderLayout.SOUTH);

        add(topWrapper, BorderLayout.NORTH);

        // ===== BUTTONS =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnAdd = new JButton("➕ Thêm");
        btnEdit = new JButton("✏️ Sửa");
        btnDelete = new JButton("🗑 Xóa");
        btnReload = new JButton("🔄 Tải lại");
        top.add(btnAdd);
        top.add(btnEdit);
        top.add(btnDelete);
        top.add(btnReload);
        add(top, BorderLayout.SOUTH);

        // ===== TABLE =====
        String[] columns = {
            "Chi nhánh",
            "Sản phẩm",
            "Số lượng"
        };

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // ===== PUBLIC GETTERS =====

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

    // ===== PUBLIC MESSAGE FUNCTIONS =====

    public void showMessage(String msg) {
        lblMessage.setText(msg);
    }

    public void clearMessage() {
        lblMessage.setText("");
    }

    // ===== UPDATE TABLE =====

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
}
