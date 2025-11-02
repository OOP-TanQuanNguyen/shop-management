package edu.ptithcm.views.pos.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class SalePanel extends JPanel {

    public SalePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // ===== PRODUCT LIST =====
        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.setBorder(BorderFactory.createTitledBorder("🔍 Tìm sản phẩm"));

        JTextField txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTable tblProducts = new JTable(new DefaultTableModel(
                new Object[][] {
                        {"SP001", "Sữa tươi Vinamilk 1L", 32000, 45},
                        {"SP002", "Mì Hảo Hảo", 3500, 120},
                        {"SP003", "Dầu ăn Tường An 1L", 52000, 30},
                        {"SP004", "Bánh Oreo 133g", 18000, 80},
                        {"SP005", "Nước suối Lavie 500ml", 6000, 200}
                },
                new Object[] {"Mã SP", "Tên sản phẩm", "Giá (₫)", "Tồn"}
        ));
        tblProducts.setRowHeight(26);
        tblProducts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        leftPanel.add(txtSearch, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(tblProducts), BorderLayout.CENTER);

        // ===== CART =====
        JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
        centerPanel.setBorder(BorderFactory.createTitledBorder("🛍️ Giỏ hàng"));

        JTable tblCart = new JTable(new DefaultTableModel(
                new Object[][] {},
                new Object[] {"Mã SP", "Tên", "SL", "Giá", "Thành tiền"}
        ));
        tblCart.setRowHeight(26);
        tblCart.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel lblTotal = new JLabel("Tổng cộng: 0 ₫", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        centerPanel.add(new JScrollPane(tblCart), BorderLayout.CENTER);
        centerPanel.add(lblTotal, BorderLayout.SOUTH);

        // ===== PAYMENT PANEL =====
        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBorder(BorderFactory.createTitledBorder("💳 Thanh toán"));

        JPanel customerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        customerPanel.add(new JLabel("Khách hàng:"));
        JTextField txtPhone = new JTextField(10);
        customerPanel.add(txtPhone);
        JButton btnFind = new JButton("🔍");
        customerPanel.add(btnFind);

        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        methodPanel.add(new JLabel("Phương thức:"));
        JComboBox<String> cmbMethod = new JComboBox<>(new String[]{"Tiền mặt", "Thẻ", "QR"});
        methodPanel.add(cmbMethod);

        JButton btnPay = new JButton("💵 Xác nhận thanh toán");
        btnPay.setBackground(new Color(0, 123, 255));
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPay.setPreferredSize(new Dimension(180, 40));
        btnPay.setFocusPainted(false);

        JPanel rightContent = new JPanel();
        rightContent.setLayout(new BoxLayout(rightContent, BoxLayout.Y_AXIS));
        rightContent.add(customerPanel);
        rightContent.add(Box.createVerticalStrut(10));
        rightContent.add(methodPanel);
        rightPanel.add(rightContent, BorderLayout.NORTH);
        rightPanel.add(btnPay, BorderLayout.SOUTH);

        // ===== BUTTONS =====
        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        JButton btnAdd = new JButton("➕ Thêm vào giỏ");
        JButton btnRemove = new JButton("➖ Xóa khỏi giỏ");
        btnAdd.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRemove.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnAdd.setPreferredSize(new Dimension(140, 35));
        btnRemove.setPreferredSize(new Dimension(140, 35));
        actionPanel.add(btnAdd);
        actionPanel.add(btnRemove);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(120, 5, 120, 5));

        JSplitPane splitLeft = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, centerPanel);
        splitLeft.setResizeWeight(0.45);
        splitLeft.setDividerSize(6);

        JSplitPane splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitLeft, rightPanel);
        splitMain.setResizeWeight(0.75);
        splitMain.setDividerSize(6);

        add(actionPanel, BorderLayout.WEST);
        add(splitMain, BorderLayout.CENTER);
    }
}
