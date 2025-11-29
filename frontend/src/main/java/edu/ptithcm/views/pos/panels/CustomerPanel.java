package edu.ptithcm.views.pos.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import edu.ptithcm.models.CustomerModel;

public class CustomerPanel extends JPanel {

    private JTable table;
    private JButton btnAdd, btnEdit, btnDelete, btnReload;
    private DefaultTableModel model;

    public CustomerPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ======= TITLE =======
        JLabel title = new JLabel(" Quản lý khách hàng", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // ======= BUTTON BAR =======
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnAdd = new JButton(" Thêm");
        btnEdit = new JButton(" Sửa");
        btnDelete = new JButton(" Xóa");
        btnReload = new JButton(" Tải lại");
        topBar.add(btnAdd);
        topBar.add(btnEdit);
        topBar.add(btnDelete);
        topBar.add(btnReload);
        add(topBar, BorderLayout.SOUTH);

        // ======= TABLE =======
        // ✅ BỎ cột "Mã KH", chỉ hiển thị: Tên, SĐT, Điểm
        String[] columns = {"Tên khách hàng", "Số điện thoại", "Điểm tích lũy"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho edit trực tiếp trên table
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
    }

    // ===== GETTERS =====
    public JTable getTable() {
        return table;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnReload() {
        return btnReload;
    }

    // ===== UPDATE TABLE =====
    public void updateTable(List<CustomerModel> customers) {
        model.setRowCount(0);

        for (CustomerModel customer : customers) {
            // ✅ BỎ customerId, chỉ hiển thị: name, phone, point
            Object[] row = new Object[]{
                customer.getName(),
                customer.getPhone(),
                customer.getPoint()
            };

            model.addRow(row);
        }
    }

    public String getCustomerIdAtRow(List<CustomerModel> customers, int selectedRow) {
        if (selectedRow >= 0 && selectedRow < customers.size()) {
            return customers.get(selectedRow).getId();
        }
        return null;
    }
}
