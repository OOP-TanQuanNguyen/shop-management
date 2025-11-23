package edu.ptithcm.views.admin.branch_dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BranchAddDialog extends BranchFormDialog {

    public BranchAddDialog(Frame owner) {
        super(owner, "➕ Thêm chi nhánh mới");

        initComponents();

        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JPanel formPanel = createFormPanel();

        txtName = new JTextField(20);
        txtPhone = new JTextField(20);
        txtAddress = new JTextField(20);
        chkStatus = new JCheckBox("Hoạt động", true);

        addField(formPanel, "Tên chi nhánh: *", txtName, 0);
        addField(formPanel, "Số điện thoại:", txtPhone, 1);
        addField(formPanel, "Địa chỉ:", txtAddress, 2);
        addField(formPanel, "Trạng thái:", chkStatus, 3);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {
        if (validateInput()) {
            confirmed = true;
            setVisible(false);
        }
    }

    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên chi nhánh!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();

        data.put("name", getBranchName());
        data.put("phone", getPhone());
        data.put("address", getAddress());

        return data;
    }

    public String getBranchName() {
        return txtName.getText().trim();
    }

    public String getPhone() {
        String val = txtPhone.getText().trim();
        return val.isEmpty() ? null : val;
    }

    public String getAddress() {
        String val = txtAddress.getText().trim();
        return val.isEmpty() ? null : val;
    }

    public Boolean getStatus() {
        return chkStatus.isSelected();
    }
}
