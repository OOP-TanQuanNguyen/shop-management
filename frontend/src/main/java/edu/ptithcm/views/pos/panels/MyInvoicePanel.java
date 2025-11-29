package edu.ptithcm.views.pos.panels;

import edu.ptithcm.models.InvoiceInfo;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MyInvoicePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnReload; // chỉ còn nút Reload
    private JLabel lblMessage;

    public MyInvoicePanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(10, 15, 10, 15));

        initTitle();
        initTable();
        initBottomBar();
    }

    private void initTitle() {
        JLabel lblTitle = new JLabel(
                " Hóa đơn của tôi",
                SwingConstants.CENTER
        );
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitle, BorderLayout.NORTH);
    }

    private void initTable() {

        model = new DefaultTableModel(
                new Object[]{"Mã HĐ", "Khách hàng", "Tổng tiền"},
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initBottomBar() {
        JPanel bottom = new JPanel(new BorderLayout());

        // -------- LEFT: only message --------
        JPanel left = new JPanel(new GridLayout(1, 1));
        left.setOpaque(false);

        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMessage.setForeground(new Color(180, 32, 42));

        left.add(lblMessage);
        bottom.add(left, BorderLayout.WEST);

        // -------- RIGHT: only button --------
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnReload = new JButton(" Tải lại");
        btnPanel.add(btnReload);

        bottom.add(btnPanel, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    public void showMessage(String msg) {
        lblMessage.setText(msg != null ? msg : "");
    }

    public void clearMessage() {
        lblMessage.setText("");
    }

    public JTable getTable() {
        return table;
    }

    public JButton getBtnReload() {
        return btnReload;
    }

    // ============================================================
    // LẤY ID HÓA ĐƠN ĐANG CHỌN
    // ============================================================
    public String getSelectedInvoiceId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }
        return (String) table.getValueAt(row, 0);
    }

    private String formatCustomer(String customerName) {
        return (customerName == null || customerName.isBlank())
                ? "Khách lẻ"
                : customerName;
    }

    public void updateTable(List<InvoiceInfo> list) {
        model.setRowCount(0);
        if (list == null) {
            return;
        }

        for (InvoiceInfo inv : list) {
            model.addRow(
                    new Object[]{
                        inv.getInvoiceId(),
                        formatCustomer(inv.getcustomerName()),
                        inv.getTotal() != null ? inv.getTotal().toString() : "0"
                    }
            );
        }
    }
}
