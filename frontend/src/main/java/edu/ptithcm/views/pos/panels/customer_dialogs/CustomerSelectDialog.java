package edu.ptithcm.views.pos.panels.customer_dialogs;

import javax.swing.*;
import java.awt.*;
import edu.ptithcm.models.CustomerModel;

public class CustomerSelectDialog extends JDialog {

    private JTextField txtName;
    private JTextField txtPhone;

    private CustomerModel result = null;

    public CustomerSelectDialog(Window parent) {
        super(parent, "Thêm / Chọn khách hàng", ModalityType.APPLICATION_MODAL);
        initUI();
    }

    private void initUI() {

        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(4, 1, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // ----------- NAME -----------
        form.add(new JLabel("Tên khách hàng:"));
        txtName = new JTextField();
        form.add(txtName);

        // ----------- PHONE ----------
        form.add(new JLabel("Số điện thoại:"));
        txtPhone = new JTextField();
        form.add(txtPhone);

        add(form, BorderLayout.CENTER);

        // =========================================================
        // BUTTON AREA
        // =========================================================
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");

        buttons.add(btnOk);
        buttons.add(btnCancel);
        add(buttons, BorderLayout.SOUTH);

        // =========================================================
        // ACTIONS
        // =========================================================
        btnOk.addActionListener(e -> onSubmit());
        btnCancel.addActionListener(e -> {
            result = null;
            dispose();
        });

        // Auto focus
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                txtName.requestFocusInWindow();
            }
        });

        pack();                     // TỰ SET SIZE ĐẸP THEO NỘI DUNG
        setResizable(false);
        setLocationRelativeTo(getParent());
    }

    private void onSubmit() {

        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên không được để trống!");
            return;
        }
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "SĐT không được để trống!");
            return;
        }

        // Trả về CustomerModel không có ID -> Backend sẽ tạo
        result = new CustomerModel.Builder()
                .name(name)
                .phone(phone)
                .point(0)
                .build();

        dispose();
    }

    public CustomerModel getResult() {
        return result;
    }
}
