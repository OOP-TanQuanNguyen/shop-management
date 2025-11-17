package edu.ptithcm.middleware;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionCustomerMap {
    // key: employeeId, value: set of customerId added in this session
    private static final Map<String, Set<String>> sessionCustomerMap = new ConcurrentHashMap<>();

    // Thêm customerId vào session
    public static void addCustomer(String employeeId, String customerId) {
        sessionCustomerMap.computeIfAbsent(employeeId, k -> ConcurrentHashMap.newKeySet())
                          .add(customerId);
    }

    // Kiểm tra quyền chỉnh sửa
    public static boolean canEdit(String employeeId, String customerId, boolean isAdmin) {
        if (isAdmin) return true;
        Set<String> allowed = sessionCustomerMap.get(employeeId);
        return allowed != null && allowed.contains(customerId);
    }

    // Xóa session khi logout hoặc timeout
    public static void removeSession(String employeeId) {
        sessionCustomerMap.remove(employeeId);
    }
}
