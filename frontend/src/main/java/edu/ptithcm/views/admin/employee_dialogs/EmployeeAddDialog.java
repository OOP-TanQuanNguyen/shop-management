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

        // Khởi tạo các field
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtName = new JTextField(20);
        txtPhone = new JTextField(20);

        initRoleComboBox();           // ✅ Khởi tạo Role dropdown
        initBranchComboBox(branches); // ✅ Khởi tạo Branch dropdown

        chkStatus = new JCheckBox("Đang làm việc");
        chkStatus.setSelected(true);

        // Thêm vào panel
        addField(formPanel, "Tên đăng nhập:", txtUsername, 0);
        addField(formPanel, "Mật khẩu:", txtPassword, 1);
        addField(formPanel, "Tên nhân viên:", txtName, 2);
        addField(formPanel, "Số điện thoại:", txtPhone, 3);
        addField(formPanel, "Vai trò:", cmbRole, 4);
        addField(formPanel, "Chi nhánh:", cmbBranch, 5);
        addField(formPanel, "Trạng thái:", chkStatus, 6);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {
        // Validate
        if (txtUsername.getText().trim().isEmpty()
                || txtPassword.getPassword().length == 0
                || txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng điền đầy đủ thông tin bắt buộc!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    // Getters
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
