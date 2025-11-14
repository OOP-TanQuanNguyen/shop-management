package edu.ptithcm.routes.authentication;

import java.io.IOException;

import edu.ptithcm.configs.TypeDTTP;
import edu.ptithcm.controller.LoginController;
import edu.ptithcm.dto.request.login.LoginRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.UserLoginInfo;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;

public class LoginRoute {
    private final DTTP server;
    private final DTTPStateManager manager;
    private final LoginController controller;
    public LoginRoute(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
        this.controller = LoginController.getInstance();
    }

    public void register() {
        server.on(TypeDTTP.LOGIN.getValue(), args -> {
            try {
                String username = (String) args.data.get("username");
                if (manager.isOnline(username)) {
                    DTTP oldConn = manager.getConn(username);
                    oldConn.send(TypeDTTP.FORCE_KICK.getValue(),null, "ERROR", "Tài khoản đăng nhập nơi khác!");
                    manager.removeConnection(oldConn);
                }
                String password = (String)args.data.get("password");

                LoginRequestDTO request = new LoginRequestDTO(username, password);

                ResponseDTO<UserLoginInfo> response = controller.handleLogin(request);

                if (response.getData() == null) {
                    args.reply(TypeDTTP.LOGIN.getValue(), null,response.getStatus(),response.getMessage());
                    return;
                }

                manager.addUser(
                    response.getData().getUsername(),
                    this.server,
                    (response.getData().getBranchId() != null?(response.getData().getBranchId()):0),
                    response.getData().getRole()
                );

                args.reply(
                    TypeDTTP.LOGIN.getValue(),
                    response.getData().toMap(),
                    response.getStatus(),
                    response.getMessage()
                );

            } catch (IOException e) {
                System.err.print("Lỗi server : "+e);
            } catch (Exception e) {
                System.err.print("Lỗi server : "+e);
                try {
                    args.reply(TypeDTTP.LOGIN.getValue(), null, "ERROR", "Lỗi nội bộ server: " + e.getMessage());
                }catch(IOException ex) {
                    System.err.print("Lỗi server : "+ex);
                }
            }
        });

        server.on(TypeDTTP.LOGOUT.getValue(), args -> {
            manager.removeConnection(server);
        });
    }
}
