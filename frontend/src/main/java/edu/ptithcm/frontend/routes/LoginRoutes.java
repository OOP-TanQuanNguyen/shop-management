package edu.ptithcm.frontend.routes;

import edu.ptithcm.frontend.protocols.DTTP;
import java.util.Map;

public class LoginRoutes {

    public LoginRoutes(DTTP client) {
        registerHandlers(client);
    }

    private void registerHandlers(DTTP client) {
        // Handler cho LOGIN response từ server
        client.on("LOGIN", this::handleLoginResponse);

        System.out.println("[ROUTES] ✅ Routes registered");
    }

    private void handleLoginResponse(Map<String, Object> data) {
        System.out.println("[ROUTES] 📥 LOGIN response received");
        if (data != null) {
            System.out.println("   Full response: " + data);
        }
    }

}
