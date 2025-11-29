package edu.ptithcm.views.admin;

import edu.ptithcm.models.InvoiceInfo;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AdminInvoicePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private JButton btnReload;
    private JButton btnDelete;

    public AdminInvoicePanel() {
        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(10, 12, 10, 12));

        initTitle();
        initTable();
        initBottomBar();
    }

    private void initTitle() {
        JLabel lbl = new JLabel(
                " Quản lý hóa đơn (tất cả chi nhánh)",
                SwingConstants.CENTER
        );
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lbl, BorderLayout.NORTH);
    }

    private void initTable() {
        model = new DefaultTableModel(
                new Object[]{
                    "Mã HĐ",
                    "Chi nhánh",
                    "Nhân viên",
                    "Khách hàng",
                    "Tổng tiền",
                    "Trạng thái",},
                0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initBottomBar() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnReload = new JButton(" Tải lại");
        btnDelete = new JButton(" Xóa hóa đơn");

        bottom.add(btnReload);
        bottom.add(btnDelete);

        add(bottom, BorderLayout.SOUTH);
    }

    // ============================ PUBLIC METHODS ============================
    public JButton getBtnReload() {
        return btnReload;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public String getSelectedInvoiceId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return null;
        }
        return table.getValueAt(row, 0).toString();
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
                        inv.getBranchId(),
                        inv.getEmployeeId(),
                        inv.getcustomerName() != null
                        ? inv.getcustomerName()
                        : "Khách lẻ",
                        inv.getTotal(),
                        inv.getStatus(),}
            );
        }
    }
}
