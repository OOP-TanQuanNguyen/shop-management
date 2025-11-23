package edu.ptithcm.views.admin.category_dialogs;

import javax.swing.*;
import java.awt.*;

public class CategoryDeleteConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public CategoryDeleteConfirmDialog(Frame owner, String categoryName) {
        super(owner, "Xác nhận xóa", true);

        setLayout(new BorderLayout());
        setSize(350, 150);
        setLocationRelativeTo(owner);

        JLabel label = new JLabel("Xóa danh mục: " + categoryName + " ?", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(label, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnOK = new JButton("Xóa");
        JButton btnCancel = new JButton("Hủy");

        btnOK.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        btnCancel.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        buttons.add(btnOK);
        buttons.add(btnCancel);

        add(buttons, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
