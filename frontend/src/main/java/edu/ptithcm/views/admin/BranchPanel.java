package edu.ptithcm.views.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import edu.ptithcm.models.BranchInfo;
import java.awt.*;
import java.util.List;

public class BranchPanel extends JPanel {

    private JTable table;
    private JButton btnAdd, btnEdit, btnDelete, btnReload;
    private DefaultTableModel model;

    public BranchPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("🏬 Quản lý chi nhánh", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

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

        // ✅ BỎ cột "ID"
        String[] columns = {"Tên chi nhánh", "Số điện thoại", "Địa chỉ", "Ngày mở cửa", "Trạng thái"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
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

    public JTable getTable() {
        return table;
    }

    public void updateTable(List<BranchInfo> branches) {
        model.setRowCount(0);

        if (branches == null || branches.isEmpty()) {
            return;
        }

        for (BranchInfo branch : branches) {
            // ✅ BỎ branch.getId()
            Object[] row = new Object[]{
                branch.getName(),
                branch.getPhone() != null ? branch.getPhone() : "N/A",
                branch.getAddress() != null ? branch.getAddress() : "N/A",
                branch.getOpenDate() != null ? branch.getOpenDate() : "N/A",
                branch.getStatusText()
            };
            model.addRow(row);
        }
    }
}
