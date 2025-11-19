package edu.ptithcm.views.pos.panels.customer_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog thêm khách hàng mới (POS) ✅ Tương thích với Admin Controller
 */
public class CustomerAddDialog extends CustomerFormDialog {

    public CustomerAddDialog(Frame owner) {
        super(owner, "➕ Thêm khách hàng mới");
        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = createFormPanel();

        txtName = new JTextField(20);
        txtPhone = new JTextField(20);

        addField(formPanel, "Tên khách hàng: *", txtName, 0);
        addField(formPanel, "Số điện thoại:", txtPhone, 1);

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
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!");
            return false;
        }

        String phone = txtPhone.getText().trim();
        if (!phone.isEmpty() && !phone.matches("\\d{9,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
            return false;
        }

        return true;
    }

    // Getters - PHẢI GIỐNG VỚI ADMIN
    public String getCustomerName() {
        return txtName.getText().trim();
    }

    public String getPhone() {
        String phone = txtPhone.getText().trim();
        return phone.isEmpty() ? null : phone;
    }

    public int getPoint() {
        return 0; // ✅ Luôn trả về 0
    }
}
