package edu.ptithcm.views.admin.employee_dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmployeeAddDialog extends EmployeeFormDialog {

    public EmployeeAddDialog(Frame owner, List<BranchItem> branches) {
        super(owner, "Thêm nhân viên mới");
        initComponents(branches);
    }

    private void initComponents(List<BranchItem> branches) {
        JPanel formPanel = createFormPanel();

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtName = new JTextField(20);
        txtPhone = new JTextField(20);

        initRoleComboBox();
        initBranchComboBox(branches);

        // ==========================================
        // ROLE CHANGE → ADMIN không cần chi nhánh
        // ==========================================
        cmbRole.addActionListener(e -> handleRoleChanged());

        chkStatus = new JCheckBox("Đang làm việc");
        chkStatus.setSelected(true);

        addField(formPanel, "Tên đăng nhập:", txtUsername, 0);
        addField(formPanel, "Mật khẩu:", txtPassword, 1);
        addField(formPanel, "Tên nhân viên:", txtName, 2);
        addField(formPanel, "Số điện thoại:", txtPhone, 3);
        addField(formPanel, "Vai trò:", cmbRole, 4);
        addField(formPanel, "Chi nhánh:", cmbBranch, 5);
        addField(formPanel, "Trạng thái:", chkStatus, 6);

        // Áp dụng ngay khi mở form
        handleRoleChanged();

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    // ======================================================
    // ADMIN → disable chi nhánh
    // STAFF → phải chọn chi nhánh
    // ======================================================
    private void handleRoleChanged() {
        String role = (String) cmbRole.getSelectedItem();

        if ("ADMIN".equalsIgnoreCase(role)) {
            cmbBranch.setEnabled(false);
            cmbBranch.setSelectedIndex(0); // không cần chọn chi nhánh
        } else {
            cmbBranch.setEnabled(true);
        }
    }

    @Override
    protected void onOkClicked() {

        if (txtUsername.getText().trim().isEmpty()
                || txtPassword.getPassword().length == 0
                || txtName.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ==========================================
        // VALIDATE SĐT 9–11 số
        // ==========================================
        String phone = txtPhone.getText().trim().replaceAll("[^0-9]", "");

        if (phone.length() < 9 || phone.length() > 11) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải từ 9 đến 11 số!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // STAFF bắt buộc chọn chi nhánh
        String role = (String) cmbRole.getSelectedItem();
        if ("STAFF".equalsIgnoreCase(role)) {
            if (cmbBranch.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Nhân viên STAFF phải thuộc một chi nhánh!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        confirmed = true;
        dispose();
    }

    // ===== GETTERS =====
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public String getEmployeeName() {
        return txtName.getText().trim();
    }

    public String getPhone() {
        return txtPhone.getText().trim();
    }

    public String getRole() {
        return (String) cmbRole.getSelectedItem();
    }

    public Integer getBranchId() {
        BranchItem selected = (BranchItem) cmbBranch.getSelectedItem();
        return (selected != null) ? selected.getId() : null;
    }

    public boolean getStatus() {
        return chkStatus.isSelected();
    }
}
