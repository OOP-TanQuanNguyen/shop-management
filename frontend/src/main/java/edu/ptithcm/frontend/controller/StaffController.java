package edu.ptithcm.frontend.controller;

import edu.ptithcm.frontend.protocols.DTTP;
import edu.ptithcm.frontend.services.StaffService;
import edu.ptithcm.frontend.view.LoginForm;
import edu.ptithcm.frontend.view.StaffDashboard;

import javax.swing.*;
import java.awt.*; // <== QUAN TRỌNG: bổ sung import BorderLayout
import java.util.List;
import java.util.Map;

public class StaffController {

    private final StaffDashboard view;
    private final StaffService service;

    public StaffController(StaffDashboard view, DTTP client) {
        this.view = view;
        this.service = new StaffService(client, this::onServerResponse);
    }

    // ====== Các hành động nút ======
    public void addOrder() {
        showInfo("🧾 Tính năng thêm đơn hàng đang được phát triển...");
    }

    public void editOrder() {
        showInfo("✏️ Sửa đơn hàng đang được phát triển...");
    }

    public void deleteOrder() {
        showInfo("🗑️ Xóa đơn hàng đang được phát triển...");
    }

    public void refreshOrders() {
        service.getInvoices(); // không cần throws IOException
    }

    public void pingServer() {
        service.ping(); // không cần try-catch IOException
    }

    public void logout() {
        view.dispose();
        new LoginForm().setVisible(true);
    }

    // ====== Xử lý phản hồi từ server ======
    private void onServerResponse(String type, Map<String, Object> data) {
        switch (type) {
            case "INVOICE_LIST" -> {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> invoices = (List<Map<String, Object>>) data.get("data");
                view.updateInvoices(invoices);
            }
            case "PING_RESPONSE" ->
                view.showMessage("Ping server thành công!", JOptionPane.INFORMATION_MESSAGE);
            default ->
                System.out.println("[⚠] Unknown response type: " + type);
        }
    }

    // ====== Giao diện phụ ======
    public JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea("""
            👤 Hồ sơ cá nhân nhân viên

            Tên: Nguyễn Văn A
            Chức vụ: Nhân viên bán hàng
            Email: staff@shop.vn
        """);
        area.setEditable(false);
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}
