package edu.ptithcm.frontend.admin;

import edu.ptithcm.frontend.protocols.DTTP;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class ProductPanel extends JPanel {

    private JTable table;
    private DTTP client;

    public ProductPanel(DTTP client) {
        this.client = client;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"Mã SP", "Tên SP", "Giá bán", "Số lượng", "Nhà cung cấp"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnReload = new JButton("🔄 Làm mới");
        JButton btnAdd = new JButton("➕ Nhập hàng");
        JButton btnEdit = new JButton("✏️ Sửa SP");
        JButton btnDelete = new JButton("🗑 Xóa SP");

        btnPanel.add(btnReload);
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        btnReload.addActionListener(e -> loadList());
        loadList();
    }

    private void loadList() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        if (client == null) {
            model.addRow(new Object[]{"SP01", "Laptop Dell", "35,000,000", 10, "FPT"});
            model.addRow(new Object[]{"SP02", "Chuột Logitech", "1,800,000", 20, "Viễn Sơn"});
            return;
        }

        try {
            client.send("PRODUCT_LIST", new HashMap<>(), "REQUEST", "Lấy danh sách sản phẩm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
