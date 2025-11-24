package edu.ptithcm.views.admin.inventory_dialogs;

import javax.swing.*;
import java.awt.*;

public class InventoryDeleteConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public InventoryDeleteConfirmDialog(Frame owner,
            String branchName,
            String productName) {
        super(owner, "🗑️ Xóa kho", true);

        initComponents(branchName, productName);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 160);
        setLocationRelativeTo(owner);
    }

    private void initComponents(String branchName, String productName) {

        setLayout(new BorderLayout(10, 10));

        // MESSAGE PANEL
        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblMessage = new JLabel(
                "<html>Bạn có chắc muốn xóa sản phẩm:<br/>"
                + "<b>" + productName + "</b><br/>"
                + "tại chi nhánh <b>" + branchName + "</b>?</html>"
        );

        lblMessage.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        lblMessage.setIconTextGap(10);

        messagePanel.add(lblMessage);

        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton yes = new JButton("Xóa");
        JButton no = new JButton("Hủy");

        yes.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        no.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(yes);
        buttonPanel.add(no);

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
