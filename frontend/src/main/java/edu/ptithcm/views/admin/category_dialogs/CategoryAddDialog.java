package edu.ptithcm.views.admin.category_dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CategoryAddDialog extends JDialog {

    private JTextField txtName;
    private JButton btnOk, btnCancel;
    private boolean confirmed = false;

    public CategoryAddDialog(Frame owner) {
        super(owner, "➕ Thêm danh mục mới", true);
        setSize(350, 200);
        initUI();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtName = new JTextField(20);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tên danh mục:"), gbc);

        gbc.gridx = 1;
        panel.add(txtName, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnOk = new JButton("OK");
        btnCancel = new JButton("Hủy");

        btnOk.addActionListener(e -> onOk());
        btnCancel.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void onOk() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên không được để trống",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        confirmed = true;
        setVisible(false);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", txtName.getText().trim());
        return map;
    }
}
