package edu.ptithcm.routes;

import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;

public class RouteManager {
    private final DTTP server;
    private final DTTPStateManager manager;

    public RouteManager(DTTP server, DTTPStateManager manager) {
        this.server = server;
        this.manager = manager;
    }

    public void registerRoutes() {
        // Gọi đăng ký từng route
        new LoginRoute(server, manager).register();
        new EmployeeRoute(server).register();
    }
}
