package edu.ptithcm.routes;
import java.io.IOException;
import java.util.Map;

import edu.ptithcm.controller.LoginController;
import edu.ptithcm.protocols.DTTP;

public class RouteManager {
    private DTTP server;

    public RouteManager(DTTP server) {
        this.server = server;
    }

    public void LoginRoute() {
        this.server.on("LOGIN", (Map<String, Object> data) -> {
            try {
                Map<String,Object> response = LoginController.handleLogin(data);
                @SuppressWarnings("unchecked")
                Map<String,Object> dataMap = (Map<String,Object>) response.get("data");

                System.out.println("Sending response for LOGIN: " + response);
                
                this.server.send(
                    (String) response.get("type"),
                    dataMap,
                    (String) response.get("status"),
                    (String) response.get("message")
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
