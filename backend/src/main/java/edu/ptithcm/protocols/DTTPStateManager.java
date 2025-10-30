package edu.ptithcm.protocols;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DTTPStateManager {
    private final Map<String, Map<String,Object>> onlineUsers = new ConcurrentHashMap<>();

    /** Đăng ký người dùng mới khi login thành công */
    public void addUser(String username, DTTPconnection conn, int branch_id, String role) {
        Map<String,Object> userData = new ConcurrentHashMap<>();
        userData.put("connection", conn);
        userData.put("branch_id", branch_id);
        userData.put("role", role);
        onlineUsers.put(username, userData);
    }

    /** Xóa người dùng khi logout hoặc mất kết nối */
    public void removeUser(String username) {
        onlineUsers.remove(username);
    }

    /** Kiểm tra user có online không */
    public boolean isOnline(String username) {
        return onlineUsers.containsKey(username);
    }

    /** Lấy connection của user */
    public DTTPconnection getConn(String username) {
        return (DTTPconnection)onlineUsers.get(username).get("connection");
    }

    /** Lấy danh sách toàn bộ người dùng online */
    public Map<String, Map<String,Object>> getAllUsers() {
        return onlineUsers;
    }

    /** Số lượng user đang online */
    public int count() {
        return onlineUsers.size();
    }

    /** Gửi message riêng cho 1 user */
    public void sendTo(String username, DTTPmsg msg) {
        DTTPconnection conn = (DTTPconnection)onlineUsers.get(username).get("connection");
        if (conn == null) return;
        try {
            String enc = DTTPEncryptor.encrypt(msg.toJson(), "1234567890123456"); // 🔐 cùng key với DTTP
            conn.send(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Gửi broadcast đến toàn bộ user online */
    public void broadcast(DTTPmsg msg) throws Exception {
        for (Map.Entry<String, Map<String,Object>> entry : onlineUsers.entrySet()) {
            DTTPconnection conn = (DTTPconnection)entry.getValue().get("connection");
                String enc = DTTPEncryptor.encrypt(msg.toJson(), "1234567890123456");
                conn.send(enc);
        }
    }

    /** Kick user ra khỏi hệ thống */
    public void kick(String username, String reason) {
        DTTPconnection conn = (DTTPconnection)onlineUsers.remove(username).get("connection");
        if (conn == null) return;

        try {
            DTTPmsg msg = new DTTPmsg(
                "FORCE_LOGOUT",
                Map.of("reason", reason != null ? reason : "Kicked by server"),
                "INFO",
                "Disconnected by server"
            );
            String enc = DTTPEncryptor.encrypt(msg.toJson(), "1234567890123456");
            conn.send(enc);
        } catch (Exception ignored) {}

        try { conn.close(); } catch (Exception ignored) {}
        System.out.println("[KICKED] " + username + " -> " + reason);
    }
}
