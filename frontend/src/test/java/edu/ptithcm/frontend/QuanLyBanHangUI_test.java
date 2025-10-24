package edu.ptithcm.frontend;

import javax.swing.SwingUtilities;

/**
 * Lớp chính khởi động ứng dụng Quản lý Bán hàng.
 */
public class QuanLyBanHangUI_test {

    public static void main(String[] args) {
        // Đảm bảo giao diện được tạo trên EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> new LoginForm_test().setVisible(true));
    }
}
