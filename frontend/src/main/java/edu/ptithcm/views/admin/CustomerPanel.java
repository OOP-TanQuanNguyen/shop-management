package edu.ptithcm.views.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import edu.ptithcm.models.CustomerModel;

public class CustomerPanel extends JPanel {

    private final JTable table;
    private final JButton btnAdd, btnEdit, btnDelete, btnReload;

    private final JTextField txtSearch;
    private final JButton btnFilter;

    private final DefaultTableModel model;

    public CustomerPanel() {

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel(" Quản lý khách hàng", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        txtSearch = new JTextField(20);
        txtSearch.setToolTipText("Nhập tên hoặc số điện thoại...");

        btnFilter = new JButton(" Lọc");

        searchBar.add(new JLabel("Tìm kiếm:"));
        searchBar.add(txtSearch);
        searchBar.add(btnFilter);

        add(searchBar, BorderLayout.NORTH);

        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnAdd = new JButton(" Thêm");
        btnEdit = new JButton(" Sửa");
        btnDelete = new JButton(" Xóa");
        btnReload = new JButton(" Tải lại");

        // =================================================================
        // ẨN NÚT THÊM — KHÔNG XOÁ KHỎI CODE ĐỂ GIỮ STRUCTURE
        // =================================================================
        btnAdd.setVisible(false);

        btnBar.add(btnAdd);
        btnBar.add(btnEdit);
        btnBar.add(btnDelete);
        btnBar.add(btnReload);
        add(btnBar, BorderLayout.SOUTH);

        model = new DefaultTableModel(
                new String[]{"Tên khách hàng", "Số điện thoại", "Điểm tích lũy"},
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

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

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JButton getBtnFilter() {
        return btnFilter;
    }

    // ============================================================
    // VALIDATE SỐ ĐIỆN THOẠI giống Employee
    // ============================================================
    private String normalizePhone(String phone) {
        if (phone == null) {
            return "";
        }

        phone = phone.replaceAll("[^0-9]", "");

        if (phone.length() < 9 || phone.length() > 11) {
            return "SĐT phải 9–11 số";
        }

        return phone;
    }

    public void updateTable(List<CustomerModel> list) {
        model.setRowCount(0);
        if (list == null) {
            return;
        }

        for (CustomerModel c : list) {

            String phone = normalizePhone(c.getPhone());

            model.addRow(new Object[]{
                c.getName(),
                phone,
                c.getPoint()
            });
        }
    }

    public void setCustomerPoints(String customerId, int points, List<CustomerModel> list) {
        if (list == null) {
            return;
        }

        for (CustomerModel c : list) {
            if (c.getId().equals(customerId)) {
                c.setPoint(points);
                break;
            }
        }
    }
}
