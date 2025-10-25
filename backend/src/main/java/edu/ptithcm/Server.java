package edu.ptithcm;

import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.routes.RouteManager;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(2025)) {
            System.out.println("[SERVER] Đang chạy trên cổng 2025...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Client mới: " + clientSocket.getInetAddress());

                DTTP server = new DTTP(clientSocket);
                RouteManager.registerRoutes(server);
                server.listen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

