package edu.ptithcm.views.admin.employee_dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmployeeEditDialog extends EmployeeFormDialog {

    private final String employeeId;

    public EmployeeEditDialog(
            Frame owner,
            String employeeId,
            String name,
            String phone,
            String role,
            Integer branchId,
            String branchName,
            boolean status,
            List<BranchItem> branches
    ) {
        super(owner, "Chỉnh sửa nhân viên");

        this.employeeId = employeeId;

        JPanel panel = createFormPanel();

        txtName = new JTextField(name);
        txtPhone = new JTextField(phone);

        initRoleComboBox();
        cmbRole.setSelectedItem(role);

        initBranchComboBox(branches);
        selectBranch(branchId);

        // Lắng nghe sự kiện đổi ROLE để disable/enable chi nhánh
        cmbRole.addActionListener(e -> handleRoleChanged());

        chkStatus = new JCheckBox("Đang hoạt động");
        chkStatus.setSelected(status);

        txtUsername = new JTextField(employeeId);
        txtUsername.setEnabled(false);
        txtPassword = new JPasswordField();
        txtPassword.setEnabled(false);

        addField(panel, "Tên nhân viên:", txtName, 0);
        addField(panel, "Số điện thoại:", txtPhone, 1);
        addField(panel, "Chức vụ:", cmbRole, 2);
        addField(panel, "Chi nhánh:", cmbBranch, 3);
        addField(panel, "Trạng thái:", chkStatus, 4);

        // Áp dụng logic ngay khi form mở ra
        handleRoleChanged();

        add(panel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private void selectBranch(Integer branchId) {
        if (branchId == null) {
            return;
        }

        for (int i = 0; i < cmbBranch.getItemCount(); i++) {
            BranchItem item = cmbBranch.getItemAt(i);
            if (item.getId() != null && item.getId().equals(branchId)) {
                cmbBranch.setSelectedIndex(i);
                return;
            }
        }
    }

    /* ============================================================
       ADMIN → KHÔNG cần chi nhánh (disable)
       STAFF → bắt buộc phải chọn chi nhánh
    ============================================================ */
    private void handleRoleChanged() {
        String role = (String) cmbRole.getSelectedItem();

        if ("ADMIN".equalsIgnoreCase(role)) {
            cmbBranch.setEnabled(false);
            cmbBranch.setSelectedIndex(0); // ADMIN không thuộc chi nhánh
        } else {
            cmbBranch.setEnabled(true); // STAFF phải chọn
        }
    }

    @Override
    protected void onOkClicked() {

        if (txtName.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Tên không được trống!");
            return;
        }

        // ============================
        // VALIDATE SỐ ĐIỆN THOẠI
        // ============================
        String phone = txtPhone.getText().trim().replaceAll("[^0-9]", "");

        if (phone.length() < 9 || phone.length() > 11) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải từ 9 đến 11 số!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ============================
        // STAFF BẮT BUỘC CÓ CHI NHÁNH
        // ============================
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

    // ============================================================
    // GETTERS
    // ============================================================
    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return txtName.getText();
    }

    public String getPhone() {
        return txtPhone.getText();
    }

    public String getRole() {
        return (String) cmbRole.getSelectedItem();
    }

    public Integer getBranchId() {
        BranchItem item = (BranchItem) cmbBranch.getSelectedItem();
        return item.getId();
    }

    public Boolean getStatus() {
        return chkStatus.isSelected();
    }
}
