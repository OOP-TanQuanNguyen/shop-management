package edu.ptithcm.views.pos.panels.customer_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog xác nhận xóa khách hàng
 */
public class CustomerDeleteConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public CustomerDeleteConfirmDialog(Frame owner, String customerName) {
        super(owner, "🗑️ Xác nhận xóa khách hàng", true);
        initComponents(customerName);
    }

    private void initComponents(String customerName) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblMessage = new JLabel("<html>Bạn có chắc muốn xóa khách hàng:<br/><b>" + customerName + "</b>?</html>");
        lblMessage.setIcon(UIManager.getIcon("OptionPane.warningIcon"));

        messagePanel.add(lblMessage);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnYes = new JButton("Xóa");
        JButton btnNo = new JButton("Hủy");

        btnYes.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        btnNo.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(btnYes);
        buttonPanel.add(btnNo);

        add(messagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void showDialog() {
        setVisible(true);
    }
}
