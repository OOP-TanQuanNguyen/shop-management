package edu.ptithcm.views.pos.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class CustomerPanel extends JPanel {
    public CustomerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblTitle = new JLabel("👥 Khách hàng & Tích điểm", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JTable tbl = new JTable(new DefaultTableModel(
                new Object[][] {
                        {"KH001", "Nguyễn Văn B", "0909123456", 120},
                        {"KH002", "Trần Thị C", "0987654321", 80}
                },
                new Object[] {"Mã KH", "Tên KH", "SĐT", "Điểm tích lũy"}
        ));
        tbl.setRowHeight(26);
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Right-click menu
        JPopupMenu menu = new JPopupMenu();
        JMenuItem viewInfo = new JMenuItem("👁️ Xem thông tin");
        JMenuItem viewInvoice = new JMenuItem("👁️ Xem hóa đơn");
        menu.add(viewInfo);
        menu.add(viewInvoice);
        tbl.setComponentPopupMenu(menu);

        // Khi click "xem thông tin"
        viewInfo.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if (row != -1) {
                String id = tbl.getValueAt(row, 0).toString();
                String name = tbl.getValueAt(row, 1).toString();
                String phone = tbl.getValueAt(row, 2).toString();
                int point = Integer.parseInt(tbl.getValueAt(row, 3).toString());
                new CustomerInfoDialog(id, name, phone, point).setVisible(true);
            }
        });

        // Double click cũng mở
        tbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tbl.getSelectedRow() != -1) {
                    viewInfo.doClick();
                }
            }
        });

        add(lblTitle, BorderLayout.NORTH);
        add(new JScrollPane(tbl), BorderLayout.CENTER);
    }
}
