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

public class BranchPanel extends JPanel {
    public BranchPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("🏬 Quản lý chi nhánh", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.add(new JButton("➕ Thêm"));
        actions.add(new JButton("✏️ Sửa"));
        actions.add(new JButton("🗑️ Xóa"));
        actions.add(new JButton("🔄 Làm mới"));

        String[] columns = {"Mã CN", "Tên chi nhánh", "SĐT", "Địa chỉ", "Ngày mở", "Tình trạng"};
        Object[][] data = {
                {"CN001", "Chi nhánh Q1", "0933222111", "12 Nguyễn Huệ, Q1", "2022-01-01", "Hoạt động"},
                {"CN002", "Chi nhánh Q7", "0988332211", "45 Nguyễn Văn Linh, Q7", "2023-02-15", "Bảo trì"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scroll = new JScrollPane(table);

        add(title, BorderLayout.NORTH);
        add(actions, BorderLayout.SOUTH);
        add(scroll, BorderLayout.CENTER);
    }
}
