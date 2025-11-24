package edu.ptithcm.views.admin.category_dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CategoryEditDialog extends JDialog {

    private final String categoryId;

    private JTextField txtName;
    private boolean confirmed = false;

    public CategoryEditDialog(Frame owner, String id, String name) {
        super(owner, "✏️ Cập nhật danh mục", true);
        this.categoryId = id;

        initUI(name);
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI(String name) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtName = new JTextField(name != null ? name : "", 20);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tên danh mục *"), gbc);

        gbc.gridx = 1;
        panel.add(txtName, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");

        btnOk.addActionListener(e -> onOkClicked());
        btnCancel.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void onOkClicked() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        confirmed = true;
        setVisible(false);
    }

    // ========================
    // PUBLIC API FOR CONTROLLER
    // ========================
    public void showDialog() {
        setVisible(true);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return txtName.getText().trim();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", categoryId);
        map.put("name", getCategoryName());
        return map;
    }
}
