package edu.ptithcm.views.components;

import javax.swing.*;

public class AppMessageBox {

    // cho phép dùng AppMessageBox.YES
    public static final int YES = JOptionPane.YES_OPTION;

    private AppMessageBox() {
    }

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }

    public static void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message,
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int showConfirm(String message) {
        return JOptionPane.showConfirmDialog(
                null,
                message,
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
    }
}
