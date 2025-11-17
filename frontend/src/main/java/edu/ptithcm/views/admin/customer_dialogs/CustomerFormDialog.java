package edu.ptithcm.views.admin.customer_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Base dialog cho Customer form với các field chung
 */
public abstract class CustomerFormDialog extends JDialog {

    protected JTextField txtName;
    protected JTextField txtPhone;
    protected JTextField txtPoint;

    protected JButton btnOk;
    protected JButton btnCancel;

    protected boolean confirmed = false;

    public CustomerFormDialog(Frame owner, String title) {
        super(owner, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(owner);
    }

    protected JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        return panel;
    }

    protected void addField(JPanel panel, String label, JComponent field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        // Field
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnOk = new JButton("OK");
        btnCancel = new JButton("Hủy");

        btnOk.addActionListener(e -> onOkClicked());
        btnCancel.addActionListener(e -> onCancelClicked());

        panel.add(btnOk);
        panel.add(btnCancel);

        return panel;
    }

    protected abstract void onOkClicked();

    protected void onCancelClicked() {
        confirmed = false;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void showDialog() {
        setVisible(true);
    }
}
