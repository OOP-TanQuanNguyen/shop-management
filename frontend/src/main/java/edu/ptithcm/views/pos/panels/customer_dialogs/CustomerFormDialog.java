package edu.ptithcm.views.pos.panels.customer_dialogs;

import javax.swing.*;
import java.awt.*;

public class CustomerFormDialog extends JDialog {

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtPoint;

    private JButton btnOk;
    private JButton btnCancel;

    private boolean confirmed = false;

    public CustomerFormDialog(Frame owner, String title) {
        super(owner, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 320);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtId = new JTextField();
        txtName = new JTextField();
        txtPhone = new JTextField();
        txtPoint = new JTextField();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addField(panel, "Mã KH:", txtId, 0);
        addField(panel, "Tên khách hàng:", txtName, 1);
        addField(panel, "Số điện thoại:", txtPhone, 2);
        addField(panel, "Điểm tích lũy:", txtPoint, 3);

        return panel;
    }

    private void addField(JPanel panel, String label, JComponent field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        // text field
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnOk = new JButton("OK");
        btnCancel = new JButton("Hủy");

        btnOk.addActionListener(e -> onOkClicked());
        btnCancel.addActionListener(e -> onCancelClicked());

        panel.add(btnOk);
        panel.add(btnCancel);

        return panel;
    }

    private void onOkClicked() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng không được để trống!");
            return;
        }
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên khách hàng không được để trống!");
            return;
        }
        if (!txtPhone.getText().matches("\\d{9,11}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
            return;
        }
        try {
            Integer.parseInt(txtPoint.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số!");
            return;
        }

        confirmed = true;
        dispose();
    }

    private void onCancelClicked() {
        confirmed = false;
        dispose();
    }

    // =======================
    //      SET DATA
    // =======================
    public void setCustomerData(String id, String name, String phone, int point, boolean isEdit) {
        txtId.setText(id);
        txtName.setText(name);
        txtPhone.setText(phone);
        txtPoint.setText(String.valueOf(point));

        if (isEdit) {
            txtId.setEditable(false); // ID không cho sửa khi edit
        }
    }

    // =======================
    //      GET DATA
    // =======================
    public String getCustomerId() {
        return txtId.getText().trim();
    }

    public String getCustomerName() {
        return txtName.getText().trim();
    }

    public String getPhone() {
        return txtPhone.getText().trim();
    }

    public int getPoint() {
        return Integer.parseInt(txtPoint.getText().trim());
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
