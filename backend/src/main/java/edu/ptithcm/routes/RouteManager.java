package edu.ptithcm.routes;
import java.io.IOException;
import java.util.Map;

import edu.ptithcm.controller.LoginController;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;

public class RouteManager {
    private DTTP server;
    private DTTPStateManager manager;

    public RouteManager(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
    }

    public void LoginRoute() {
        this.server.on("LOGIN", args -> {
            try {
                if (this.manager.isOnline((String)args.data.get("username"))){
                    DTTP oldConnection = this.manager.getConn((String)args.data.get("username"));
                    oldConnection.send("FORCE_KICK", null, "ERROR", "có thằng đăng nhập nơi khác!");
                    this.manager.removeConnection(oldConnection);
                }

                Map<String,Object> response = LoginController.handleLogin(args.data);

                if (response == null) {
                    System.out.println("[ERROR] response null from LoginController");
                    args.reply("LOGIN", null, "ERROR", "Lỗi xử lý đăng nhập");
                    return;
                }

                @SuppressWarnings("unchecked")
                Map<String,Object> dataMap = (Map<String,Object>) response.get("data");
                if (dataMap == null) {
                    System.out.println("[ERROR] dataMap null trong response: " + response);
                    args.reply("LOGIN", null, "ERROR", "Thiếu dữ liệu trả về từ LoginController");
                    return;
                }

                manager.addUser(
                    (String) dataMap.get("username"),
                    this.server,
                    dataMap.get("branch_id") != null ? ((Number)dataMap.get("branch_id")).intValue() : 0,
                    (String) dataMap.get("role")
                );

                System.out.println("[SERVER] Sending response for LOGIN: " + response);

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


        this.server.on("LOGOUT",args -> {
            this.manager.removeConnection(server);
        });
    }
}
