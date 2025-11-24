package edu.ptithcm.views.admin.category_dialogs;

import javax.swing.*;
import java.awt.*;

public class CategoryDeleteConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public CategoryDeleteConfirmDialog(Frame owner, String categoryName) {
        super(owner, "Xác nhận xóa danh mục", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(owner);

        initComponents(categoryName);
    }

    private void initComponents(String categoryName) {
        setLayout(new BorderLayout(10, 10));

        // Icon warning
        JLabel iconLabel = new JLabel("⚠️", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        add(iconLabel, BorderLayout.WEST);

        // Message
        JPanel messagePanel = new JPanel(new BorderLayout(5, 5));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        JLabel titleLabel = new JLabel("Xác nhận xóa danh mục");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        messagePanel.add(titleLabel, BorderLayout.NORTH);

        JLabel messageLabel = new JLabel(
                "<html>Bạn có chắc chắn muốn xóa danh mục<br/>"
                + "<b>" + categoryName + "</b>?<br/><br/>"
                + "Hành động này không thể hoàn tác!</html>"
        );
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        add(messagePanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton btnConfirm = new JButton("Xóa");
        btnConfirm.setBackground(new Color(220, 53, 69));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFocusPainted(false);
        btnConfirm.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(btnConfirm);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void showDialog() {
        setVisible(true);
    }
}
