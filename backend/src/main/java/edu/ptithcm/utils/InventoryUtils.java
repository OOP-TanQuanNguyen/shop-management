package edu.ptithcm.utils;

public class InventoryUtils {
    /**
     * Chuyển String sang Integer, trả về null nếu không hợp lệ
     */
    public static Integer parseBranchId(String branchId) {
        if (branchId == null || branchId.isEmpty()) return null;
        try {
            return Integer.valueOf(branchId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
