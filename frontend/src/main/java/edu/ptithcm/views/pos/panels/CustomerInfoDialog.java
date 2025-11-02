package edu.ptithcm.views.pos.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CustomerInfoDialog extends JDialog {
    public CustomerInfoDialog(String id, String name, String phone, int point) {
        setTitle("👤 Thông tin khách hàng");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JLabel lblHeader = new JLabel("Chi tiết khách hàng", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 8, 8));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        infoPanel.add(new JLabel("Mã KH:"));
        infoPanel.add(new JLabel(id));
        infoPanel.add(new JLabel("Tên KH:"));
        infoPanel.add(new JLabel(name));
        infoPanel.add(new JLabel("Số điện thoại:"));
        infoPanel.add(new JLabel(phone));
        infoPanel.add(new JLabel("Điểm tích lũy:"));
        infoPanel.add(new JLabel(String.valueOf(point)));

        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(btnClose);

        add(lblHeader, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}
