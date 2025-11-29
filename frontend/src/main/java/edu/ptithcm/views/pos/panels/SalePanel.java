package edu.ptithcm.views.pos.panels;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class SalePanel extends JPanel {

    private JTable tblProducts;
    private JTable tblCart;

    private JTextField txtSearch;

    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnPay;

    // ONLY BUTTON & LABEL (no input fields here)
    private JButton btnSelectCustomer;
    private JLabel lblCustomerName;

    private JLabel lblTotal;

    public SalePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 15, 10, 15));
        initUI();
    }

    private void initUI() {

        // ======================================================
        // LEFT: PRODUCT LIST
        // ======================================================
        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.setBorder(BorderFactory.createTitledBorder("🔍 Tìm sản phẩm"));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tblProducts = new JTable(new DefaultTableModel(
                new Object[]{"Mã SP", "Tên sản phẩm", "Giá", "Tồn"},
                0
        ));
        tblProducts.setRowHeight(26);
        tblProducts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        leftPanel.add(txtSearch, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(tblProducts), BorderLayout.CENTER);

        // ======================================================
        // CENTER: CART
        // ======================================================
        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        centerPanel.setBorder(BorderFactory.createTitledBorder("🛍️ Giỏ hàng"));

        tblCart = new JTable(new DefaultTableModel(
                new Object[]{"Mã SP", "Tên", "SL", "Giá", "Thành tiền"},
                0
        ));
        tblCart.setRowHeight(26);
        tblCart.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        lblTotal = new JLabel("Tổng cộng: 0 ₫", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotal.setBorder(new EmptyBorder(5, 10, 5, 10));

        centerPanel.add(new JScrollPane(tblCart), BorderLayout.CENTER);
        centerPanel.add(lblTotal, BorderLayout.SOUTH);

        // ======================================================
        // RIGHT: CUSTOMER + PAYMENT
        // ======================================================
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createTitledBorder("💳 Thanh toán"));

        // Only Button and label here
        btnSelectCustomer = new JButton(" Thêm / Chọn khách hàng");
        btnSelectCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSelectCustomer.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblCustomerName = new JLabel("Khách hàng: Chưa chọn");
        lblCustomerName.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblCustomerName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblCustomerName.setBorder(new EmptyBorder(10, 0, 10, 0));

        btnPay = new JButton(" Xác nhận thanh toán");
        btnPay.setBackground(new Color(0, 123, 255));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPay.setPreferredSize(new Dimension(180, 40));
        btnPay.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(btnSelectCustomer);
        rightPanel.add(lblCustomerName);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(btnPay);
        rightPanel.add(Box.createVerticalStrut(10));

        // ======================================================
        // SIDE BUTTONS
        // ======================================================
        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        actionPanel.setBorder(new EmptyBorder(120, 5, 120, 5));

        btnAdd = new JButton(" Thêm vào giỏ");
        btnRemove = new JButton(" Xóa khỏi giỏ");

        btnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRemove.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        actionPanel.add(btnAdd);
        actionPanel.add(btnRemove);

        // ======================================================
        // SPLIT PANELS
        // ======================================================
        JSplitPane splitLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, centerPanel);
        splitLeft.setResizeWeight(0.45);

        JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitLeft, rightPanel);
        splitMain.setResizeWeight(0.75);

        add(actionPanel, BorderLayout.WEST);
        add(splitMain, BorderLayout.CENTER);
    }

    // GETTERS ======================================================
    public JTable getProductTable() {
        return tblProducts;
    }

    public JTable getCartTable() {
        return tblCart;
    }

    public JTextField getTxtSearch() {
        return txtSearch;
    }

    public JButton getBtnSelectCustomer() {
        return btnSelectCustomer;
    }

    public JLabel getLblCustomerName() {
        return lblCustomerName;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnRemove() {
        return btnRemove;
    }

    public JButton getBtnPay() {
        return btnPay;
    }

    public JLabel getLblTotal() {
        return lblTotal;
    }

    // TABLE UPDATE ======================================================
    public void updateProductTable(java.util.List<Object[]> rows) {
        DefaultTableModel model = (DefaultTableModel) tblProducts.getModel();
        model.setRowCount(0);
        rows.forEach(model::addRow);
    }

    public void updateCartTable(java.util.List<Object[]> rows) {
        DefaultTableModel model = (DefaultTableModel) tblCart.getModel();
        model.setRowCount(0);
        rows.forEach(model::addRow);
    }

    public void updateTotal(String text) {
        lblTotal.setText(text);
    }

    public void setSearchListener(DocumentListener listener) {
        txtSearch.getDocument().addDocumentListener(listener);
    }
}
