package edu.ptithcm.routes;

import java.io.IOException;
import java.util.Map;
import edu.ptithcm.controller.LoginController;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;

public class LoginRoute {
    private final DTTP server;
    private final DTTPStateManager manager;

    public LoginRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
    }

    public void register() {
        server.on("LOGIN", args -> {
            try {
                String username = (String) args.data.get("username");

                if (manager.isOnline(username)) {
                    DTTP oldConn = manager.getConn(username);
                    oldConn.send("FORCE_KICK", null, "ERROR", "Tài khoản đăng nhập nơi khác!");
                    manager.removeConnection(oldConn);
                }

                Map<String,Object> response = LoginController.handleLogin(args.data);

                if (response == null) {
                    args.reply("LOGIN", null, "ERROR", "Lỗi xử lý đăng nhập");
                    return;
                }

                @SuppressWarnings("unchecked")
                Map<String,Object> dataMap = (Map<String,Object>) response.get("data");
                if (dataMap == null) {
                    args.reply("LOGIN", null, "ERROR", "Thiếu dữ liệu trả về từ LoginController");
                    return;
                }

                manager.addUser(
                    (String) dataMap.get("username"),
                    this.server,
                    dataMap.get("branch_id") != null ? ((Number)dataMap.get("branch_id")).intValue() : 0,
                    (String) dataMap.get("role")
                );

                args.reply(
                    (String) response.getOrDefault("type", "LOGIN"),
                    dataMap,
                    (String) response.getOrDefault("status", "UNKNOWN"),
                    (String) response.getOrDefault("message", "")
                );

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    args.reply("LOGIN", null, "ERROR", "Lỗi nội bộ server: " + ex.getMessage());
                } catch (IOException ignored) {}
            }
        });

        server.on("LOGOUT", args -> {
            manager.removeConnection(server);
        });
    }
}
