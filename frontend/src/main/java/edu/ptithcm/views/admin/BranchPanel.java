package edu.ptithcm.views.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import edu.ptithcm.models.BranchInfo;
import java.awt.*;
import java.util.List;

public class BranchPanel extends JPanel {

    private final JTable table;
    private final JButton btnAdd, btnEdit, btnDelete, btnReload;
    private final DefaultTableModel model;

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

        model = new DefaultTableModel(
                new String[]{"Tên", "SĐT", "Địa chỉ"}, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);

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

    public void updateTable(List<BranchInfo> list) {

        System.out.println("[DEBUG][Panel] updateTable size = "
                + (list == null ? 0 : list.size()));

        model.setRowCount(0);

        if (list == null) {
            return;
        }

        for (BranchInfo b : list) {
            model.addRow(new Object[]{
                b.getName(),
                b.getPhone() != null ? b.getPhone() : "N/A",
                b.getAddress() != null ? b.getAddress() : "N/A"
            });
        }
    }
}
