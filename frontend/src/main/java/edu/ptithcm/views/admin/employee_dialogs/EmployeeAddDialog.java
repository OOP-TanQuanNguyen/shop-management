package edu.ptithcm.views.admin.employee_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog thêm nhân viên mới
 */
public class EmployeeAddDialog extends EmployeeFormDialog {

    // 🔹 Thêm dòng này để khai báo biến cboRole
    private JComboBox<String> cboRole;

    public EmployeeAddDialog(Frame owner) {
        super(owner, "➕ Thêm nhân viên mới");
        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = createFormPanel();

        // Create fields
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtName = new JTextField(20);
        txtPhone = new JTextField(20);
        cboRole = new JComboBox<>(new String[]{"STAFF", "ADMIN"}); // ✅ đã khai báo ở trên

        // Add fields to form
        addField(formPanel, "Tên đăng nhập: *", txtUsername, 0);
        addField(formPanel, "Mật khẩu: *", txtPassword, 1);
        addField(formPanel, "Tên nhân viên: *", txtName, 2);
        addField(formPanel, "Số điện thoại:", txtPhone, 3);
        addField(formPanel, "Chức vụ: *", cboRole, 4);

        // Layout
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {
        if (validateInput()) {
            confirmed = true;
            dispose();
        }
    }

    private boolean validateInput() {
        if (txtUsername.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtPassword.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (cboRole.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    // Getters
    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword()).trim();
    }

    public String getEmployeeName() {
        return txtName.getText().trim();
    }

    public String getPhone() {
        return txtPhone.getText().trim();
    }

    public String getRole() {
        return cboRole.getSelectedItem().toString();
    }
}
