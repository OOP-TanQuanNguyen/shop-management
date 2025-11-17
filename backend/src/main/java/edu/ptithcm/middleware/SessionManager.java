package edu.ptithcm.middleware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import edu.ptithcm.models.EmployeeModel;

public class SessionManager {
    // key: sessionId, value: employee đang login
    private static final Map<String, EmployeeModel> sessionMap = new ConcurrentHashMap<>();

    //Thêm session khi login thành công
    public static void addSession(String sessionId, EmployeeModel employee) {
        if (sessionId != null && employee != null) {
            sessionMap.put(sessionId, employee);
        }
    }

    // Xóa session khi logout hoặc timeout
    public static void removeSession(String sessionId) {
        if (sessionId != null) {
            sessionMap.remove(sessionId);
        }
    }

    //Lấy employee hiện tại từ sessionId
    public static EmployeeModel getCurrentEmployee(String sessionId) {
        if (sessionId == null) return null;
        return sessionMap.get(sessionId);
    }
}
