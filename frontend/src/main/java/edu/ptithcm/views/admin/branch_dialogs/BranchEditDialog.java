package edu.ptithcm.views.admin.branch_dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class BranchEditDialog extends BranchFormDialog {

    private final String branchId;

    public BranchEditDialog(Frame owner,
            String branchId,
            String name,
            String phone,
            String address) {

        super(owner, "✏️ Cập nhật chi nhánh");
        this.branchId = branchId;

        System.out.println("[DEBUG][EditDialog] Open for ID = " + branchId);

        initComponents(name, phone, address);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initComponents(String name, String phone, String address) {

        JPanel panel = createFormPanel();

        txtName = new JTextField(name, 20);
        txtPhone = new JTextField(phone, 20);
        txtAddress = new JTextField(address, 20);

        addField(panel, "Tên chi nhánh *", txtName, 0);
        addField(panel, "Số điện thoại", txtPhone, 1);
        addField(panel, "Địa chỉ", txtAddress, 2);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    @Override
    protected void onOkClicked() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập tên chi nhánh!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("[DEBUG][EditDialog] Confirm update for ID = " + branchId);

        confirmed = true;
        setVisible(false);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", txtName.getText().trim());
        m.put("phone", txtPhone.getText().trim());
        m.put("address", txtAddress.getText().trim());
        return m;
    }

    public String getBranchId() {
        return branchId;
    }
}
