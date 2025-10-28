package edu.ptithcm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.ptithcm.configs.Config;
import edu.ptithcm.model.repository.EmployeeRepo;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.routes.RouteManager;
import edu.ptithcm.services.EmployeeService;

public class Server {
    private static final int port = Config.AppConfig.SERVER_PORT;
    public static void main(String[] args) {
        try{
            boolean existed = !EmployeeRepo.checkEmployeeExists("admin");
            if(existed) {
                EmployeeService.createEmployee(
                "admin",             
                "admin123",          
                "Administrator",     
                "0000000000",        
                "ADMIN"              
                );
                System.out.println("Admin account created with username: admin, password: admin123");
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        try (ServerSocket serverSocket = new ServerSocket(Server.port)) {
            System.out.println("[SERVER] is running in " + port + "....");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] Client mới: " + clientSocket.getInetAddress());
                DTTP server = new DTTP(clientSocket);
            
                RouteManager routeManager = new RouteManager(server);
                routeManager.LoginRoute();   
                server.listen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

