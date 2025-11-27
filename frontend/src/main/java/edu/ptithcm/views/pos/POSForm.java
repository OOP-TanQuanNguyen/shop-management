package edu.ptithcm.views.pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.FlowLayout;

import edu.ptithcm.models.UserModel;
import edu.ptithcm.views.pos.panels.CustomerPanel;
import edu.ptithcm.views.pos.panels.MyInvoicePanel;
import edu.ptithcm.views.pos.panels.SalePanel;

public class POSForm extends JFrame {

    private final JButton btnLogout = new JButton("Đăng xuất");

    // Panels
    private final CustomerPanel customerPanel = new CustomerPanel();
    private final SalePanel salePanel = new SalePanel();
    private final MyInvoicePanel myInvoicePanel = new MyInvoicePanel();

    private final JTabbedPane tabs = new JTabbedPane();

    public POSForm(UserModel user) {
        setTitle("🛒 POS - Hệ thống bán hàng");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initTopBar(user);
        initTabs();
    }

    private void initTopBar(UserModel user) {

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 102, 102));

        JLabel lblTitle = new JLabel("💰 HỆ THỐNG BÁN HÀNG", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblUser = new JLabel(user.getUsername() + " | " + user.getRole() + " | " + user.getBranch());
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel rightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightBar.setOpaque(false);
        rightBar.add(lblUser);
        rightBar.add(btnLogout);

        topBar.add(lblTitle, BorderLayout.CENTER);
        topBar.add(rightBar, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
    }

    private void initTabs() {
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabs.addTab("🛒 Bán hàng", salePanel);
        tabs.addTab("👥 Khách hàng", customerPanel);
        tabs.addTab("🧾 Hóa đơn của tôi", myInvoicePanel);

        add(tabs, BorderLayout.CENTER);
    }

    // getters for controller
    public JButton getLogoutButton() {
        return btnLogout;
    }

    public CustomerPanel getCustomerPanel() {
        return customerPanel;
    }

    public SalePanel getSalePanel() {
        return salePanel;
    }

    public MyInvoicePanel getMyInvoicePanel() {
        return myInvoicePanel;
    }

    public JTabbedPane getTabPane() {
        return tabs;
    }
}
