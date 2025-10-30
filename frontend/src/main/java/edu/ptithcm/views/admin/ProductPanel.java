package edu.ptithcm.views.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ProductPanel extends JPanel {
    public ProductPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("📦 Quản lý sản phẩm", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.add(new JButton("➕ Thêm"));
        controls.add(new JButton("✏️ Sửa"));
        controls.add(new JButton("🗑️ Xóa"));
        controls.add(new JButton("🔄 Làm mới"));

        String[] columns = {"Mã SP", "Tên sản phẩm", "Danh mục", "Giá nhập", "Giá bán", "Hạn sử dụng", "Trạng thái"};
        Object[][] data = {
                {"SP001", "Bánh quy Oreo", "Bánh kẹo", "15.000", "25.000", "2025-12-31", "Đang bán"},
                {"SP002", "Nước suối Lavie", "Đồ uống", "5.000", "10.000", "2026-03-20", "Đang bán"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scroll = new JScrollPane(table);

        add(title, BorderLayout.NORTH);
        add(controls, BorderLayout.SOUTH);
        add(scroll, BorderLayout.CENTER);
    }
}
