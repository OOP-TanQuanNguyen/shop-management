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

        // ======= TITLE =======
        JLabel title = new JLabel("👥 Quản lý nhân viên", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // ======= BUTTON BAR =======
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

        // ======= TABLE =======
        String[] columns = {
            "Tên nhân viên",
            "Tên đăng nhập",
            "Vai trò",
            "Chi nhánh",
            "Số điện thoại",
            "Ngày vào làm",
            "Ngày nghỉ việc",
            "Trạng thái"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // toàn bộ bảng là read-only
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    // ===== GETTERS =====
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

    // ===== UPDATE TABLE =====
    public void updateTable(List<UserModel> employees) {
        model.setRowCount(0);
        if (employees == null) {
            return;
        }

        for (UserModel emp : employees) {
            Object[] row = new Object[]{
                emp.getName(),
                emp.getUsername(),
                emp.getRole(),
                // tránh null pointer khi branch = null
                emp.getBranch() != null ? emp.getBranch() : "N/A",
                emp.getPhone(),
                emp.getHireDate() != null ? emp.getHireDate() : "N/A",
                emp.getEndDate() != null ? emp.getEndDate() : "N/A",
                (emp.getStatus() != null && emp.getStatus()) ? "Đang làm việc" : "Đã nghỉ"
            };

            model.addRow(row);
        }
    }
}
