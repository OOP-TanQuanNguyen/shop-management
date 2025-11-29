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

    private JButton btnReload;
    private JButton btnConfirm;
    private JButton btnCancel;

    private JLabel lblMessage;

    public MyInvoicePanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(10, 15, 10, 15));

        initTitle();
        initTable();
        initBottomBar();
    }

    /* ============================================================
       TITLE
    ============================================================ */
    private void initTitle() {
        JLabel lblTitle = new JLabel(
                "🧾 Hóa đơn của tôi",
                SwingConstants.CENTER
        );
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitle, BorderLayout.NORTH);
    }

    /* ============================================================
       TABLE
    ============================================================ */
    private void initTable() {
        // ❌ Bỏ Thời gian & Trạng thái — chỉ còn 3 cột
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

    /* ============================================================
       BOTTOM BAR
    ============================================================ */
    private void initBottomBar() {
        JPanel bottom = new JPanel(new BorderLayout());

        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMessage.setForeground(new Color(180, 32, 42));

        bottom.add(lblMessage, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnReload = new JButton(" Tải lại");
        btnConfirm = new JButton("✔ Xác nhận");
        btnCancel = new JButton("✖ Hủy hóa đơn");

        btnPanel.add(btnReload);
        btnPanel.add(btnConfirm);
        btnPanel.add(btnCancel);

        bottom.add(btnPanel, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    /* ============================================================
       PUBLIC UI METHODS
    ============================================================ */
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

    public JButton getBtnConfirm() {
        return btnConfirm;
    }

    public JButton getBtnCancel() {
        return btnCancel;
    }

    /* ============================================================
       GET SELECTED INVOICE
    ============================================================ */
    public String getSelectedInvoiceId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }
        return (String) table.getValueAt(row, 0);
    }

    /* ============================================================
       FORMAT CUSTOMER NAME
    ============================================================ */
    private String formatCustomer(String customerName) {
        return (customerName == null || customerName.isBlank())
                ? "Khách lẻ"
                : customerName;
    }

    /* ============================================================
       UPDATE TABLE
    ============================================================ */
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
                        inv.getTotal() != null ? inv.getTotal().toString() : "0",}
            );
        }
    }
}
