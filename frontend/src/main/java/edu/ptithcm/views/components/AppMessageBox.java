package edu.ptithcm.views.components;

import javax.swing.*;

public class AppMessageBox {

    private AppMessageBox() {} // ✅ chặn khởi tạo instance

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    public static void showSuccess(String message) {
        // Thực ra Swing không có SUCCESS_ICON sẵn, dùng icon info thay thế
        JOptionPane.showMessageDialog(null, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}
