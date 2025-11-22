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

        // FIX LỖI: chọn đúng BranchItem theo Integer ID
        selectBranch(branchId);

        chkStatus = new JCheckBox("Đang hoạt động");
        chkStatus.setSelected(status);

        txtUsername = new JTextField(employeeId); // read-only UI nếu muốn
        txtUsername.setEnabled(false);
        txtPassword = new JPasswordField();
        txtPassword.setEnabled(false);

        addField(panel, "Tên nhân viên:", txtName, 0);
        addField(panel, "Số điện thoại:", txtPhone, 1);
        addField(panel, "Chức vụ:", cmbRole, 2);
        addField(panel, "Chi nhánh:", cmbBranch, 3);
        addField(panel, "Trạng thái:", chkStatus, 4);

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

    @Override
    protected void onOkClicked() {

        if (txtName.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Tên không được trống!");
            return;
        }

        if (cmbBranch.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi nhánh!");
            return;
        }

        confirmed = true;
        dispose();
    }

    // GETTERS DÙNG CHO CONTROLLER
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
        return item.getId();   // Luôn Integer => KHÔNG BAO GIỜ LỖI NỮA
    }

    public Boolean getStatus() {
        return chkStatus.isSelected();
    }
}
