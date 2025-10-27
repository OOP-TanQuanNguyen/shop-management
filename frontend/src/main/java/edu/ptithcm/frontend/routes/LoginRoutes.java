package edu.ptithcm.frontend.routes;

import edu.ptithcm.frontend.protocols.DTTP;
import java.util.Map;

/**
 * Xử lý các route khác LOGIN (ví dụ: REGISTER, LOGOUT, SYNC, ...). Riêng LOGIN
 * đã được AuthService đảm nhiệm.
 */
public class LoginRoutes {

    private final DTTP client;

    public LoginRoutes(DTTP client) {
        this.client = client;
    }

    /**
     * Đăng ký các route lắng nghe từ server
     */
    public void register() {
        System.out.println("[ROUTES] ⚙️ Registering non-login routes...");
        client.on("REGISTER", this::handleRegister);
        client.on("PING", this::handlePing);
        System.out.println("[ROUTES] ✅ Routes registered (LOGIN handled by AuthService)");
    }

    private void handleRegister(Map<String, Object> data) {
        System.out.println("[ROUTES] 📨 Received REGISTER event: " + data);
        // TODO: hiển thị thông báo hoặc cập nhật UI
    }

    private void handlePing(Map<String, Object> data) {
        System.out.println("[ROUTES] 🏓 Ping response: " + data);
    }
}
