package edu.ptithcm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.ptithcm.configs.Config;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.routes.RouteManager;

public class Server {
    private static final int port = Config.AppConfig.SERVER_PORT;
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(Server.port)) {
            System.out.println("[SERVER] is running in " + port + "....");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Client mới: " + clientSocket.getInetAddress());
                DTTP server = new DTTP(clientSocket);
                //route
                RouteManager routeManager = new RouteManager(server);
                routeManager.LoginRoute();   
                server.listen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

