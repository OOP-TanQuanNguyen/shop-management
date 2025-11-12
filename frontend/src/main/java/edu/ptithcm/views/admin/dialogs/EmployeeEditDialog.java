package edu.ptithcm.views.admin.dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog sửa thông tin nhân viên
 */
public class EmployeeEditDialog extends EmployeeFormDialog {

    private final String employeeId;

    public EmployeeEditDialog(Frame owner, String id, String name, String phone, String role, boolean status) {
        super(owner, "✏️ Cập nhật nhân viên");
        this.employeeId = id;
        initComponents(name, phone, role, status);
    }

    private void initComponents(String name, String phone, String role, boolean status) {
        JPanel formPanel = createFormPanel();

        // Create fields with initial values
        txtName = new JTextField(name, 20);
        txtPhone = new JTextField(phone, 20);
        txtRole = new JTextField(role, 20);
        chkStatus = new JCheckBox("Đang làm việc", status);

        // Add fields to form
        addField(formPanel, "Tên nhân viên:", txtName, 0);
        addField(formPanel, "Số điện thoại:", txtPhone, 1);
        addField(formPanel, "Chức vụ:", txtRole, 2);
        addField(formPanel, "Trạng thái:", chkStatus, 3);

        // Layout
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {
        confirmed = true;
        dispose();
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return txtName.getText().trim();
    }

    public String getPhone() {
        return txtPhone.getText().trim();
    }

    public String getRole() {
        return txtRole.getText().trim();
    }

    public boolean getStatus() {
        return chkStatus.isSelected();
    }
}
