package edu.ptithcm.frontend.admin;

import edu.ptithcm.frontend.protocols.DTTP;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class EmployeePanel extends JPanel {

    private JTable table;
    private DTTP client;

    public EmployeePanel(DTTP client) {
        this.client = client;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"Username", "Họ tên", "Chức vụ", "SĐT", "Lương", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnReload = new JButton("🔄 Làm mới");
        JButton btnAdd = new JButton("➕ Thêm");
        JButton btnDelete = new JButton("🗑 Xóa");
        JButton btnLock = new JButton("🔒 Khóa");
        JButton btnLog = new JButton("📜 Log");

        btnPanel.add(btnReload);
        btnPanel.add(btnAdd);
        btnPanel.add(btnDelete);
        btnPanel.add(btnLock);
        btnPanel.add(btnLog);
        add(btnPanel, BorderLayout.SOUTH);

        btnReload.addActionListener(e -> loadList());
        btnAdd.addActionListener(e -> addEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());

        loadList();
    }

    private void loadList() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        if (client == null) {
            // Demo offline
            model.addRow(new Object[]{"admin", "Chủ cửa hàng", "ADMIN", "090-000-0000", "20,000,000", "Active"});
            model.addRow(new Object[]{"staff", "Nguyễn Văn A", "Nhân viên", "091-111-1111", "7,000,000", "Active"});
            return;
        }

        try {
            client.send("EMPLOYEE_LIST", new HashMap<>(), "REQUEST", "Lấy danh sách nhân viên");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addEmployee() {
        JOptionPane.showMessageDialog(this, "Thêm nhân viên (gửi EMPLOYEE_ADD)");
    }

    private void deleteEmployee() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Chọn nhân viên cần xóa!");
            return;
        }
        String username = (String) table.getValueAt(row, 0);
        JOptionPane.showMessageDialog(this, "Gửi yêu cầu xóa: " + username);
    }
}
