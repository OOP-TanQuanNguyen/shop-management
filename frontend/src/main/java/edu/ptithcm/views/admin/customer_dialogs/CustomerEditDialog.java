package edu.ptithcm.views.admin.customer_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog sửa thông tin khách hàng — KHÔNG cho phép sửa điểm tích lũy
 */
public class CustomerEditDialog extends CustomerFormDialog {

    private final String customerId;

    public CustomerEditDialog(Frame owner, String id, String name, String phone, int point) {
        super(owner, " Cập nhật khách hàng");
        this.customerId = id;
        initComponents(name, phone);
    }

    private void initComponents(String name, String phone) {
        JPanel formPanel = createFormPanel();

        txtName = new JTextField(name, 20);
        txtPhone = new JTextField(phone, 20);

        addField(formPanel, "Tên khách hàng:", txtName, 0);
        addField(formPanel, "Số điện thoại:", txtPhone, 1);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {

        // ================================
        // VALIDATE TÊN
        // ================================
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tên khách hàng không được để trống!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ================================
        // VALIDATE SỐ ĐIỆN THOẠI (9–11 số)
        // ================================
        String phone = txtPhone.getText().trim().replaceAll("[^0-9]", "");

        if (phone.length() < 9 || phone.length() > 11) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải từ 9 đến 11 số!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        confirmed = true;
        dispose();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return txtName.getText().trim();
    }

    public String getPhone() {
        return txtPhone.getText().trim();
    }
}
