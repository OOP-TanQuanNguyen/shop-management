package edu.ptithcm.views.admin.employee_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog sửa thông tin nhân viên
 */
public class EmployeeEditDialog extends EmployeeFormDialog {

    private JComboBox<String> cboRole;
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

        // ✅ ComboBox cho chức vụ
        cboRole = new JComboBox<>(new String[]{"STAFF", "ADMIN"});
        cboRole.setSelectedItem(role != null ? role : "STAFF");

        // ✅ Checkbox trạng thái làm việc
        chkStatus = new JCheckBox("Đang làm việc", status);

        // Add fields to form
        addField(formPanel, "Tên nhân viên:", txtName, 0);
        addField(formPanel, "Số điện thoại:", txtPhone, 1);
        addField(formPanel, "Chức vụ:", cboRole, 2);
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
        return (String) cboRole.getSelectedItem();  // ✅ lấy từ combo box
    }

    public boolean getStatus() {
        return chkStatus.isSelected();
    }
}
