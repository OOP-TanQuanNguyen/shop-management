package edu.ptithcm.views.admin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import edu.ptithcm.models.CategoryModel;

import java.awt.*;
import java.util.List;

public class CategoryPanel extends JPanel {

    private JTable table;
    private JButton btnAdd, btnEdit, btnDelete, btnReload, btnFilter;
    private JTextField txtSearch;

    private DefaultTableModel model;

    public CategoryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        // ===== TITLE =====
        JLabel title = new JLabel("📂 Quản lý danh mục", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // ===== SEARCH BAR =====
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        txtSearch = new JTextField(20);
        txtSearch.setToolTipText("Nhập tên danh mục...");

        btnFilter = new JButton("🔍 Lọc");

        searchBar.add(new JLabel("Tìm kiếm:"));
        searchBar.add(txtSearch);
        searchBar.add(btnFilter);

        add(searchBar, BorderLayout.BEFORE_FIRST_LINE);

        // ===== BUTTON BAR =====
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnAdd = new JButton("➕ Thêm");
        btnEdit = new JButton("✏️ Sửa");
        btnDelete = new JButton("🗑 Xóa");
        btnReload = new JButton("🔄 Tải lại");

        topBar.add(btnAdd);
        topBar.add(btnEdit);
        topBar.add(btnDelete);
        topBar.add(btnReload);

        add(topBar, BorderLayout.SOUTH);

        // ===== TABLE =====
        model = new DefaultTableModel(new String[]{"Tên danh mục"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // GETTERS
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

    public JButton getBtnFilter() {
        return btnFilter;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JTable getTable() {
        return table;
    }

    // UPDATE TABLE
    public void updateTable(List<CategoryModel> categories) {
        model.setRowCount(0);
        if (categories == null) {
            return;
        }

        for (CategoryModel c : categories) {
            model.addRow(new Object[]{c.getName()});
        }
    }
}
