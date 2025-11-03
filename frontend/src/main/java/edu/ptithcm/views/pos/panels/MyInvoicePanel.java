package edu.ptithcm.views.pos.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class MyInvoicePanel extends JPanel {
    public MyInvoicePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("🧾 Hóa đơn của tôi", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JTable tbl = new JTable(new DefaultTableModel(
                new Object[][] {
                        {"HD001", "2025-10-31 09:00", "Nguyễn Văn B", "450,000 ₫"},
                        {"HD002", "2025-10-31 10:20", "Trần Thị C", "150,000 ₫"}
                },
                new Object[] {"Mã HĐ", "Thời gian", "Khách hàng", "Tổng tiền"}
        ));
        tbl.setRowHeight(26);
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JButton btnReload = new JButton("🔄 Tải lại");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnReload);

        add(lblTitle, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}
