package edu.ptithcm.views.admin.branch_dialogs;

import javax.swing.*;
import java.awt.*;

public class BranchDeleteConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public BranchDeleteConfirmDialog(Frame owner, String branchName) {
        super(owner, "🗑️ Xác nhận xóa", true);
        initComponents(branchName);
    }

    private void initComponents(String branchName) {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        JLabel lblMessage = new JLabel(
                "<html>Bạn có chắc muốn xóa chi nhánh:<br/><b>" + branchName + "</b>?</html>"
        );
        lblMessage.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        messagePanel.add(lblMessage);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnYes = new JButton("Xóa");
        JButton btnNo = new JButton("Hủy");

        btnYes.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        btnNo.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
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
