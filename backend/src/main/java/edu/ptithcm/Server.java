package edu.ptithcm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.ptithcm.configs.Config;
import edu.ptithcm.configs.databases.HibernateUtil;
import edu.ptithcm.middleware.SystemMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.routes.RouteManager;

public class Server {

    private static final int PORT = Config.AppConfig.SERVER_PORT;
    private static final DTTPStateManager MANAGER = new DTTPStateManager();
    private static volatile boolean running = true;

    public static void main(String[] args) {

        try {
            HibernateUtil.getInstance().init();
        } catch (Exception e) {
            System.err.println("[ORM] Init error: " + e.getMessage());
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Listening on " + PORT);

            while (running && !serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] New client: " + clientSocket.getRemoteSocketAddress());

                handleClient(clientSocket);
            }

        } catch (IOException e) {
            System.err.println("[SERVER] IO ERROR: " + e.getMessage());
        } finally {
            HibernateUtil.getInstance().shutdown();
            System.out.println("[SERVER] Shutdown complete.");
        }
    }


    private static void handleClient(Socket clientSocket) {
        try {
            DTTP dttp = new DTTP(clientSocket);

            // System middleware
            SystemMiddleWare.replyClientCheck(dttp);

            // Register routes
            RouteManager routeManager = new RouteManager(dttp, MANAGER);
            routeManager.registerRoutes();

            // Disconnect callback
            dttp.setOnDisconnect(() -> MANAGER.removeConnection(dttp));

            // Start listener thread
            dttp.listen();

            System.out.println("[SERVER] Listener ready for " + dttp.getConnection().getAddress());

        } catch (IOException e) {
            System.err.println("[SERVER] Client handler failed: " + e.getMessage());
        }
    }


    public static void stop() {
        running = false;
        System.out.println("[SERVER] Stopping server...");
    }
}
