package edu.ptithcm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.ptithcm.configs.Config;
import edu.ptithcm.configs.databases.Database;
import edu.ptithcm.middleware.SystemMiddleWare;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.protocols.DTTPStateManager;
import edu.ptithcm.routes.RouteManager;

public class Server {
    private static final int port = Config.AppConfig.SERVER_PORT;
    private static final DTTPStateManager manager= new DTTPStateManager();
    public static void main(String[] args) {
        // Database
        Database.setDefaultType("MYSQL");
        Database.getInstance().init();

        //server
        try (ServerSocket serverSocket = new ServerSocket(Server.port)) {
            System.out.println("[SERVER] is running in " + port + "....");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Client mới: " + clientSocket.getInetAddress() + "| port = "+clientSocket.getPort());
                DTTP server = new DTTP(clientSocket);
                SystemMiddleWare.replyClientCheck(server);
                RouteManager routeManager = new RouteManager(server,Server.manager);
                server.setOnDisconect(() -> {
                    manager.removeConnection(server);
                });
                routeManager.LoginRoute();
                server.listen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

