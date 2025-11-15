package edu.ptithcm.views.admin.product_dialogs;

import javax.swing.*;
import java.awt.*;

public abstract class ProductFormDialog extends JDialog {

    protected JTextField txtName;
    protected JTextField txtCategoryId;
    protected JTextField txtCostPrice;
    protected JTextField txtSellPrice;
    protected JTextField txtExpiry;
    protected JCheckBox chkStatus;

    protected JButton btnOk;
    protected JButton btnCancel;

    protected boolean confirmed = false;

    public ProductFormDialog(Frame owner, String title) {
        super(owner, title, true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(450, 400);
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

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnOk = new JButton("OK");
        btnCancel = new JButton("Hủy");

        btnOk.addActionListener(e -> onOkClicked());
        btnCancel.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        panel.add(btnOk);
        panel.add(btnCancel);

        return panel;
    }

    protected abstract void onOkClicked();

    public boolean isConfirmed() {
        return confirmed;
    }
}
