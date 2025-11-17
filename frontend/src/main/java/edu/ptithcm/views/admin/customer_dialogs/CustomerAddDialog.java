package edu.ptithcm.views.admin.customer_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog thêm khách hàng mới
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
        txtPoint = new JTextField("0", 20);

        addField(formPanel, "Tên khách hàng: *", txtName, 0);
        addField(formPanel, "Số điện thoại:", txtPhone, 1);
        addField(formPanel, "Điểm tích lũy:", txtPoint, 2);

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

        if (!txtPhone.getText().trim().matches("\\d{9,11}") && !txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
            return false;
        }

        try {
            Integer.parseInt(txtPoint.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Điểm phải là số!");
            return false;
        }
        return true;
    }

    // Getters
    public String getCustomerName() {
        return txtName.getText().trim();
    }

    public String getPhone() {
        return txtPhone.getText().trim();
    }

    public int getPoint() {
        return Integer.parseInt(txtPoint.getText().trim());
    }
}
