package edu.ptithcm.views.pos.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import edu.ptithcm.models.InvoiceInfo;

public class MyInvoicePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnReload;
    private JButton btnConfirm;
    private JButton btnCancel;

    private JLabel lblMessage;

    private static final DateTimeFormatter TIME_FORMAT
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        JLabel lblTitle = new JLabel("🧾 Hóa đơn của tôi", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitle, BorderLayout.NORTH);
    }

    /* ============================================================
       TABLE
    ============================================================ */
    private void initTable() {

        // ✅ FIX: THÊM cột "Trạng thái"
        model = new DefaultTableModel(
                new Object[]{"Mã HĐ", "Thời gian", "Khách hàng", "Tổng tiền", "Trạng thái"}, 0) {

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

        btnReload = new JButton("🔄 Tải lại");
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
       FORMAT METHODS
    ============================================================ */
    private String formatTime(Long ts) {
        if (ts == null) {
            return "";
        }
        LocalDateTime dt
                = LocalDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneId.systemDefault());
        return TIME_FORMAT.format(dt);
    }

    private String formatMoney(Object value) {
        if (value == null) {
            return "0 ₫";
        }

        try {
            double v = Double.parseDouble(value.toString());
            return String.format("%,.0f ₫", v);
        } catch (Exception e) {
            return value.toString();
        }
    }

    private String formatCustomer(String customerId) {
        return (customerId == null || customerId.isBlank())
                ? "Khách lẻ"
                : customerId;
    }

    // ✅ FIX: THÊM formatStatus()
    private String formatStatus(String status) {
        if (status == null || status.isEmpty()) {
            return "";
        }

        return switch (status) {
            case "PENDING" ->
                "⏳ Chờ xử lý";
            case "COMPLETED" ->
                "✅ Hoàn thành";
            case "CANCELLED" ->
                "❌ Đã hủy";
            default ->
                status;
        };
    }

    /* ============================================================
       UPDATE TABLE
    ============================================================ */
    public void updateTable(List<InvoiceInfo> list) {
        model.setRowCount(0);
        if (list == null) {
            return;
        }

        // ✅ FIX: THÊM cột Status
        for (InvoiceInfo inv : list) {
            model.addRow(new Object[]{
                inv.getInvoiceId(),
                formatTime(inv.getCreatedAt()),
                formatCustomer(inv.getCustomerId()),
                formatMoney(inv.getTotal()),
                formatStatus(inv.getStatus()) // ← THÊM cột này
            });
        }
    }
}
