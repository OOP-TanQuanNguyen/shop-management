package edu.ptithcm.views.admin.employee_dialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog xác nhận xóa nhân viên
 */
public class EmployeeDeleteConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public EmployeeDeleteConfirmDialog(Frame owner, String employeeName) {
        super(owner, "🗑️ Xác nhận xóa", true);
        initComponents(employeeName);
    }

    private void initComponents(String employeeName) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Message panel
        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        JLabel lblMessage = new JLabel("<html>Bạn có chắc muốn xóa nhân viên:<br/><b>" + employeeName + "</b>?</html>");
        lblMessage.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        messagePanel.add(lblMessage);

        // Button panel
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
