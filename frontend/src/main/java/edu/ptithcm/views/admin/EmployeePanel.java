package edu.ptithcm.views.admin;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import edu.ptithcm.models.UserModel;

public class EmployeePanel extends JPanel {

    private JTable table;
    private JButton btnAdd, btnEdit, btnDelete, btnReload, btnFilter;

    private JComboBox<String> cbRole, cbStatus, cbBranch;

    private DefaultTableModel model;

    public EmployeePanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ===== TITLE =====
        JLabel title = new JLabel("👥 Quản lý nhân viên", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // =========================================================
        // FILTER BAR
        // =========================================================
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        cbRole = new JComboBox<>(new String[]{"Tất cả", "ADMIN", "STAFF"});
        cbStatus = new JComboBox<>(new String[]{"Tất cả", "Đang làm", "Đã nghỉ"});

        cbBranch = new JComboBox<>();
        cbBranch.addItem("Tất cả");

        btnFilter = new JButton(" Lọc");

        filterBar.add(new JLabel("Vai trò:"));
        filterBar.add(cbRole);

        filterBar.add(new JLabel("Trạng thái:"));
        filterBar.add(cbStatus);

        filterBar.add(new JLabel("Chi nhánh:"));
        filterBar.add(cbBranch);

        filterBar.add(btnFilter);

        add(filterBar, BorderLayout.BEFORE_FIRST_LINE);

        // ===== BUTTON BAR =====
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

        // ===== TABLE =====
        String[] columns = {
            "Tên nhân viên", "Tên đăng nhập", "Vai trò", "Chi nhánh",
            "Số điện thoại", "Ngày vào làm", "Trạng thái"
        };

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

    // ======================================================================
    // NORMALIZE & VALIDATE PHONE (Không bôi đỏ)
    // ======================================================================
    private String normalizePhone(String phone) {

        if (phone == null) {
            return "SĐT phải 9–11 số";
        }

        // Giữ lại số
        phone = phone.trim().replaceAll("[^0-9]", "");

        if (phone.isEmpty()) {
            return "SĐT phải 9–11 số";
        }

        if (phone.length() < 9 || phone.length() > 11) {
            return "SĐT phải 9–11 số";
        }

        return phone;
    }

    // ======================================================================
    // GETTERS
    // ======================================================================
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

    public JComboBox<String> getCbRole() {
        return cbRole;
    }

    public JComboBox<String> getCbStatus() {
        return cbStatus;
    }

    public JComboBox<String> getCbBranch() {
        return cbBranch;
    }

    // ======================================================================
    // UPDATE BRANCH LIST
    // ======================================================================
    public void updateBranchList(List<String> branchNames) {
        cbBranch.removeAllItems();
        cbBranch.addItem("Tất cả");

        if (branchNames != null) {
            for (String name : branchNames) {
                cbBranch.addItem(name);
            }
        }
    }

    // ======================================================================
    // UPDATE TABLE
    // ======================================================================
    public void updateTable(List<UserModel> employees) {
        model.setRowCount(0);
        if (employees == null) {
            return;
        }

        for (UserModel emp : employees) {

            String phone = normalizePhone(emp.getPhone());  // validate

            model.addRow(new Object[]{
                emp.getName(),
                emp.getUsername(),
                emp.getRole(),
                emp.getBranch() != null ? emp.getBranch() : "N/A",
                phone,
                emp.getHireDate() != null ? emp.getHireDate() : "N/A",
                Boolean.TRUE.equals(emp.getStatus()) ? "Đang làm việc" : "Đã nghỉ"
            });
        }
    }
}
