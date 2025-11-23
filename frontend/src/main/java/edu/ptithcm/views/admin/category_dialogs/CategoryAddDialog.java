package edu.ptithcm.views.admin.category_dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CategoryAddDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtName;
    private boolean confirmed = false;

    public CategoryAddDialog(Frame owner) {
        super(owner, "➕ Thêm danh mục mới", true);
        initUI();
        pack();
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtName = new JTextField(20);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Tên danh mục:"), gbc);

        // Text field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(txtName, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");

        btnOk.addActionListener(e -> onOk());
        btnCancel.addActionListener(e -> onCancel());

        btnPanel.add(btnOk);
        btnPanel.add(btnCancel);

        // Set default button (Enter)
        getRootPane().setDefaultButton(btnOk);

        // ESC = Cancel
        getRootPane().registerKeyboardAction(
                e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void onOk() {
        String name = txtName.getText() != null ? txtName.getText().trim() : "";
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tên không được để trống",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE
            );
            txtName.requestFocus();
            return;
        }
        confirmed = true;
        setVisible(false); // hoặc dispose() nếu bạn không reuse dialog
    }

    private void onCancel() {
        confirmed = false;
        setVisible(false); // hoặc dispose()
    }

    public void showDialog() {
        confirmed = false;           // reset state mỗi lần show
        txtName.setText("");         // reset input
        txtName.requestFocus();      // focus lại
        setVisible(true);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getCategoryName() {
        return txtName.getText() != null ? txtName.getText().trim() : "";
    }
}
