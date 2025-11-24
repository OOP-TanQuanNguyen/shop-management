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

    public InventoryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("📦 Quản lý kho hàng", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

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

        String[] columns = {
            "Chi nhánh",
            "Sản phẩm",
            "Số lượng", // "Cập nhật"
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

    public void updateTable(List<InventoryModel> list) {
        model.setRowCount(0);
        if (list == null) {
            return;
        }

        for (InventoryModel item : list) {
            model.addRow(new Object[]{
                item.getBranchName(),
                item.getProductName(),
                item.getQuantity(), //item.getUpdatedAt()
            });
        }
    }
}
