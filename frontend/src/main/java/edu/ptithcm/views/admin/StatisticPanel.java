package edu.ptithcm.views.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class StatisticPanel extends JPanel {

    public StatisticPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel(" Thống kê doanh thu & hoạt động", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel stats = new JPanel(new GridLayout(2, 2, 15, 15));
        stats.add(createCard(" Doanh thu hôm nay", "12.530.000₫"));
        stats.add(createCard(" Hóa đơn trong ngày", "89"));
        stats.add(createCard(" Khách hàng mới", "12"));
        stats.add(createCard(" Sản phẩm sắp hết hạn", "6"));

        add(title, BorderLayout.NORTH);
        add(stats, BorderLayout.CENTER);
    }

    private JPanel createCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(245, 250, 250));
        card.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        JLabel lbl1 = new JLabel(label, SwingConstants.CENTER);
        lbl1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel lbl2 = new JLabel(value, SwingConstants.CENTER);
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        card.add(lbl1, BorderLayout.NORTH);
        card.add(lbl2, BorderLayout.CENTER);
        return card;
    }
}
