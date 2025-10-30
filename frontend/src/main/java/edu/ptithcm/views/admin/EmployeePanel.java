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

public class EmployeePanel extends JPanel {
    public EmployeePanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("👤 Quản lý nhân viên", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        // Control bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnAdd = new JButton("➕ Thêm");
        JButton btnEdit = new JButton("✏️ Sửa");
        JButton btnDelete = new JButton("🗑️ Xóa");
        JButton btnReload = new JButton("🔄 Tải lại");
        topBar.add(btnAdd);
        topBar.add(btnEdit);
        topBar.add(btnDelete);
        topBar.add(btnReload);

        // Table
        String[] columns = {"Mã NV", "Tên nhân viên", "Chi nhánh", "Chức vụ", "SĐT", "Ngày bắt đầu", "Trạng thái"};
        Object[][] data = {
                {"NV001", "Nguyễn Văn A", "Quận 1", "ADMIN", "0909123456", "2023-01-01", "Hoạt động"},
                {"NV002", "Trần Thị B", "Quận 7", "STAFF", "0987654321", "2024-03-15", "Tạm nghỉ"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columns));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        JScrollPane scroll = new JScrollPane(table);

        add(title, BorderLayout.NORTH);
        add(topBar, BorderLayout.SOUTH);
        add(scroll, BorderLayout.CENTER);
    }
}
