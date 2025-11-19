package edu.ptithcm.views.pos.panels.customer_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog sửa thông tin khách hàng (POS) ✅ Tương thích với Admin Controller
 */
public class CustomerEditDialog extends CustomerFormDialog {

    private final String customerId;

    public CustomerEditDialog(Frame owner, String id, String name, String phone, int point) {
        super(owner, "✏️ Cập nhật khách hàng");
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
        confirmed = true;
        dispose();
    }

    // Getters - PHẢI GIỐNG VỚI ADMIN
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
