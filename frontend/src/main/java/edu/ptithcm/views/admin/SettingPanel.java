package edu.ptithcm.views.admin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class SettingPanel extends JPanel {
    public SettingPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("⚙️ Cấu hình hệ thống", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel form = new JPanel(new GridLayout(5, 2, 15, 15));
        form.add(new JLabel("Ngôn ngữ:"));
        form.add(new JComboBox<>(new String[]{"Tiếng Việt", "English"}));

        form.add(new JLabel("Chủ đề giao diện:"));
        form.add(new JComboBox<>(new String[]{"Sáng", "Tối"}));

        form.add(new JLabel("Tự động sao lưu:"));
        form.add(new JCheckBox("Bật"));

        form.add(new JLabel("Hiển thị thông báo:"));
        form.add(new JCheckBox("Bật"));

        form.add(new JLabel("Phiên bản ứng dụng:"));
        form.add(new JLabel("v1.0.0 (MiniMarket)"));

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
    }
}
