package edu.ptithcm.views.admin.branch_dialogs;

import javax.swing.*;
import java.awt.*;

public class BranchDeleteConfirmDialog extends JDialog {

    private boolean confirmed = false;

    public BranchDeleteConfirmDialog(Frame owner, String name) {
        super(owner, "Xác nhận xoá", true);

        JLabel label = new JLabel(
                "Bạn có chắc chắn muốn xoá chi nhánh: " + name + " ?",
                SwingConstants.CENTER
        );

        JButton ok = new JButton("Xóa");
        JButton cancel = new JButton("Huỷ");

        ok.addActionListener(e -> {
            confirmed = true;
            setVisible(false);
        });

        cancel.addActionListener(e -> setVisible(false));

        JPanel btn = new JPanel(new FlowLayout());
        btn.add(ok);
        btn.add(cancel);

        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        add(btn, BorderLayout.SOUTH);

        setSize(350, 150);
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
