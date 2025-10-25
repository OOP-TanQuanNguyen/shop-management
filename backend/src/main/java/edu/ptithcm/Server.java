package edu.ptithcm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import edu.ptithcm.protocols.DTTP;

public class Server {

    public static void main(String[] args) {
        int port = 2025;
        System.out.println("[SERVER] Starting DTTP server on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[SERVER] New client connected: " + clientSocket.getInetAddress());

                DTTP session = new DTTP(clientSocket);

                session.on("ping", data -> {
                    System.out.println("[SERVER] Received: " + data);
                    try {
                        session.send("pong", Map.of("msg", "Pong from server!"),"OK","ConCac");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                session.listen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
