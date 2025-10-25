package edu.ptithcm.frontend.controller;

import edu.ptithcm.frontend.protocols.DTTP;
import edu.ptithcm.frontend.services.AdminService;
import edu.ptithcm.frontend.view.AdminDashboard;
import edu.ptithcm.frontend.view.LoginForm;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AdminController {

    private final AdminDashboard view;
    private final DTTP client;
    private final AdminService service;

    public AdminController(AdminDashboard view, DTTP client) {
        this.view = view;
        this.client = client;
        this.service = new AdminService(client, this::handleServiceResponse);
    }

    // ===================== 1. XỬ LÝ PHẢN HỒI TỪ BACKEND (SERVICE) =====================
    private void handleServiceResponse(String type, Map<String, Object> data) {
        SwingUtilities.invokeLater(() -> {
            switch (type) {
                case "EMPLOYEE_LIST":
                    List<Map<String, Object>> empList = (List<Map<String, Object>>) data.get("list");
                    view.updateEmployees(empList);
                    break;
                // ... (Các case khác giữ nguyên)

                case "CUSTOMER_LIST":
                    List<Map<String, Object>> custList = (List<Map<String, Object>>) data.get("list");
                    view.updateCustomers(custList); // Yêu cầu View cập nhật
                    break;

                case "CUSTOMER_ACTION_RESPONSE":
                    String status = (String) data.getOrDefault("status", "ERROR");
                    String message = (String) data.getOrDefault("message", "Lỗi không xác định");

                    int msgType = "OK".equals(status) ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
                    view.showMessage(message, msgType);

                    if ("OK".equals(status)) {
                        service.getCustomers(); // Điều phối làm mới sau khi thao tác thành công
                    }
                    break;
                case "LOG":
                    view.updateLog((String) data.getOrDefault("message", "Log trống"));
                    break;
            }
        });
    }

    // ===================== 2. XỬ LÝ INPUT TỪ VIEW (ĐIỀU PHỐI) =====================
    // ⭐️ Làm mới Khách hàng
    public void refreshCustomers() {
        service.getCustomers();
    }

    // ⭐️ Thêm Khách hàng
    public void addCustomer() {
        String name = JOptionPane.showInputDialog(view, "Nhập tên khách hàng:");
        String phone = JOptionPane.showInputDialog(view, "Nhập số điện thoại:");

        if (name != null && !name.trim().isEmpty() && phone != null) {
            Map<String, Object> newCustomer = Map.of(
                    "name", name,
                    "phone", phone,
                    "address", "Chưa cập nhật"
            );
            // Gói dữ liệu và gửi đến Service
            service.addCustomer(newCustomer);
        }
    }

    // ⭐️ Xóa Khách hàng
    public void deleteCustomer() {
        try {
            int selectedRow = view.getCustomerTable().getSelectedRow();
            if (selectedRow != -1) {
                int id = Integer.parseInt(view.getCustomerTableModel().getValueAt(selectedRow, 0).toString());

                if (JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa khách hàng ID: " + id + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    service.deleteCustomer(id); // Điều phối Service
                }
            } else {
                view.showMessage("Vui lòng chọn một khách hàng để xóa.", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            view.showMessage("Lỗi khi xử lý thao tác: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    // Làm mới Nhân viên
    public void refreshEmployees() {
        service.getEmployees();
    }

    public void logout() {
        try {
            if (client != null) {
                client.stop();
            }
        } catch (Exception ignored) {
        }
        view.dispose();
        new LoginForm().setVisible(true);
    }
}
