package edu.ptithcm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            System.err.println("[ORM] Lỗi khởi tạo Hibernate: " + e.getMessage());
            return; // dừng luôn nếu ORM fail
        }

        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Listening on port " + PORT);

            while (running && !serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.printf("[SERVER] Client mới: %s | port=%d%n",
                        clientSocket.getInetAddress(), clientSocket.getPort());

                pool.submit(() -> handleClient(clientSocket));
            }

        } catch (IOException e) {
            System.err.println("[SERVER] Lỗi: " + e.getMessage());
        } finally {
            pool.shutdown();
            HibernateUtil.getInstance().shutdown();
            System.out.println("[SERVER] Đã shutdown thread pool và ORM.");
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            DTTP server = new DTTP(clientSocket);
            SystemMiddleWare.replyClientCheck(server);

            RouteManager routeManager = new RouteManager(server, MANAGER);
            routeManager.registerRoutes();

            server.setOnDisconect(() -> MANAGER.removeConnection(server));
            server.listen();

            System.out.println("Đã đăng kí sự kiện cho client " + server.getConnection().getAddress());
        } catch (IOException e) {
            System.err.println("[SERVER] Lỗi khi xử lý client: " + e.getMessage());
        }
    }

    public static void stop() {
        running = false;
        System.out.println("[SERVER] Đang tắt server...");
    }
}
