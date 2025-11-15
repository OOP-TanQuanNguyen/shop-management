package edu.ptithcm.views.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import edu.ptithcm.models.UserModel;

public class EmployeePanel extends JPanel {

    private JTable table;
    private JButton btnAdd, btnEdit, btnDelete, btnReload;
    private DefaultTableModel model;

    public EmployeePanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("👤 Quản lý nhân viên", SwingConstants.CENTER);
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

        String[] columns = {"Mã NV", "Tên nhân viên", "Chi nhánh", "Chức vụ", "SĐT", "Ngày bắt đầu", "Trạng thái"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
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

    public void updateTable(List<UserModel> employees) {
        model.setRowCount(0);

        for (UserModel user : employees) {

            String statusText;
            if (user.getStatus() == null) {
                statusText = "Không rõ";
            } else {
                statusText = user.getStatus() ? "Đang làm việc" : "Đã nghỉ";
            }

            Object[] row = new Object[]{
                user.getId(),
                user.getName() != null ? user.getName() : user.getUsername(),
                user.getBranch(),
                user.getRole(),
                user.getPhone(),
                user.getHireDate(),
                statusText
            };

            model.addRow(row);
        }
    }
}
