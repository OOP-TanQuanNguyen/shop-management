package edu.ptithcm.views.pos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import edu.ptithcm.models.UserModel;
import edu.ptithcm.views.pos.panels.CustomerPanel;
import edu.ptithcm.views.pos.panels.MyInvoicePanel;
import edu.ptithcm.views.pos.panels.SalePanel;

public class POSForm extends JFrame {

    private JButton btnLogout = new JButton("Đăng xuất");

    public POSForm(UserModel user) {
        setTitle("🛒 POS - Hệ thống bán hàng");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TOP BAR =====
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0, 102, 102));

        JLabel lblTitle = new JLabel("💰 HỆ THỐNG BÁN HÀNG", SwingConstants.CENTER);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));

        // Info user
        JLabel lblUser = new JLabel(user.getUsername()+" | " + user.getRole() + " | " + user.getBranch());
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        this.btnLogout.setBackground(new Color(220, 53, 69));
        this.btnLogout.setForeground(Color.WHITE);
        this.btnLogout.setFocusPainted(false);
        this.btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JPanel rightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightBar.setOpaque(false);
        rightBar.add(lblUser);
        rightBar.add(btnLogout);

        topBar.add(lblTitle, BorderLayout.CENTER);
        topBar.add(rightBar, BorderLayout.EAST);

        // ===== TABS =====
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("🛒 Bán hàng", new SalePanel());
        tabs.addTab("👥 Khách hàng", new CustomerPanel());
        tabs.addTab("🧾 Hóa đơn của tôi", new MyInvoicePanel());

        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    public JButton getLogoutButton(){
        return this.btnLogout;
    }
}
