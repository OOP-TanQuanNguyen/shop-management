package edu.ptithcm.routes.authentication;

import java.io.IOException;

import edu.ptithcm.controller.LoginController;
import edu.ptithcm.dto.request.login.LoginRequestDTO;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.dto.response.UserLoginInfo;
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
                    oldConn.send("FORCE_KICK",null, "ERROR", "Tài khoản đăng nhập nơi khác!");
                    manager.removeConnection(oldConn);
                }
                String password = (String)args.data.get("password");

                LoginRequestDTO request = new LoginRequestDTO(username, password);

                ResponseDTO<UserLoginInfo> response = LoginController.handleLogin(request);

                if (response.getData() == null) {
                    args.reply(response.getType(), null, (String)response.getStatus(),response.getMessage());
                    return;
                }

                manager.addUser(
                    response.getData().getUsername(),
                    this.server,
                    (response.getData().getBranchId() != null?(response.getData().getBranchId()):0),
                    response.getData().getRole()
                );

                args.reply(
                    response.getType(),
                    response.getData().toMap(),
                    response.getStatus(),
                    response.getMessage()
                );

            } catch (IOException e) {
                System.err.print("Lỗi server : "+e);
            } catch (Exception e) {
                System.err.print("Lỗi server : "+e);
                try {
                    args.reply("LOGIN", null, "ERROR", "Lỗi nội bộ server: " + e.getMessage());
                }catch(IOException ex) {
                    System.err.print("Lỗi server : "+ex);
                }
            }
        });

        server.on("LOGOUT", args -> {
            manager.removeConnection(server);
        });
    }
}
