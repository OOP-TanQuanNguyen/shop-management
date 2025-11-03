package edu.ptithcm.protocols;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DTTPStateManager {
    private final Map<String, DTTP> userToConn = new ConcurrentHashMap<>();
    private final Map<DTTP, String> connToUser = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> userMeta = new ConcurrentHashMap<>();

    // --- Quản lý user ---
    public void addUser(String username, DTTP conn, int branchId, String role) {

        userToConn.put(username, conn);
        connToUser.put(conn, username);

        Map<String, Object> meta = new ConcurrentHashMap<>();
        meta.put("username", username);
        meta.put("branch_id", branchId);
        meta.put("role", role);
        userMeta.put(username, meta);

        System.out.println("[ONLINE] " + username + " (" + role + ") branch=" + branchId);
    }

    public void removeUser(String username) {
        DTTP conn = userToConn.remove(username);
        if (conn != null) connToUser.remove(conn);
        userMeta.remove(username);
        System.out.println("[OFFLINE] " + username);
    }

    public void removeConnection(DTTP conn) {
        String username = connToUser.remove(conn);
        if (username != null) {
            userToConn.remove(username);
            userMeta.remove(username);
            System.out.println("[DISCONNECTED] " + username);
        }
    }

    // --- Getter ---
    public boolean isOnline(String username) { return userToConn.containsKey(username); }
    public DTTP getConn(String username) { return userToConn.get(username); }
    public String getUsername(DTTP conn) { return connToUser.get(conn); }
    public Map<String, Object> getUserMeta(String username) { return userMeta.get(username); }
    public int count() { return userToConn.size(); }

    // --- Gửi dữ liệu (tầng protocol) ---
    public void sendTo(String username, Map<String,Object> data, String message) {
        DTTP conn = userToConn.get(username);
        if (conn == null) return;
        try {
            conn.send("NOTIFY", data, "INFO", message);
        } catch (Exception e) {
            System.err.println("[ERROR] sendTo(" + username + "): " + e.getMessage());
        }
    }

    public void broadcast(Map<String,Object> data, String message) {
        userToConn.forEach((username, conn) -> {
            try {
                conn.send("NOTIFY", data, "INFO", message);
            } catch (Exception e) {
                System.err.println("[ERROR] broadcast → " + username + ": " + e.getMessage());
            }
        });
    }


    public void debugList() {
        System.out.println("=== ONLINE USERS (" + count() + ") ===");
        userMeta.forEach((u, meta) ->
            System.out.println(" - " + u + " (" + meta.get("role") + "), branch=" + meta.get("branch_id"))
        );
    }
}
