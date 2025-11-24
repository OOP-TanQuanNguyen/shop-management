package edu.ptithcm.views.admin.branch_dialogs;

import javax.swing.*;
import java.awt.*;

public class BranchDeleteConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public BranchDeleteConfirmDialog(Frame owner, String name) {
        super(owner, "🗑️ Xác nhận xoá chi nhánh", true);

        initComponents(name);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(owner);
    }

    private void initComponents(String name) {

        setLayout(new BorderLayout(10, 10));

        // MESSAGE PANEL
        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblMessage = new JLabel(
                "<html>Bạn có chắc muốn xóa chi nhánh:<br/><b>"
                + name + "</b>?</html>"
        );

        lblMessage.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        lblMessage.setIconTextGap(10);

        messagePanel.add(lblMessage);

        // BUTTON PANEL
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
}
