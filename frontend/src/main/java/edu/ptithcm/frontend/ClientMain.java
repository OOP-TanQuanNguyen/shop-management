package edu.ptithcm.frontend;

import edu.ptithcm.frontend.protocols.DTTP;
import edu.ptithcm.frontend.routes.LoginRoutes;
import edu.ptithcm.frontend.services.AuthService;
import edu.ptithcm.frontend.controllers.LoginController;
import edu.ptithcm.frontend.view.LoginForm;

public class ClientMain {

    private static DTTP client;
    private static AuthService authService;
    private static LoginController loginController;

    public static void main(String[] args) {
        try {
            System.out.println("??????????????????????????????");
            System.out.println("[CLIENT] 🚀 Starting Client...");
            System.out.println("??????????????????????????????");

            String host = "localhost";
            int port = 2025;
            System.out.println("[CLIENT] 🌐 Connecting to " + host + ":" + port + "...");

            // Tạo kết nối client
            client = new DTTP(host, port);
            System.out.println("[CLIENT] ✅ DTTP client created");

            // Khởi tạo routes (ngoài LOGIN)
            new LoginRoutes(client);
            System.out.println("[CLIENT] ✅ Routes registered (LOGIN handled by AuthService)");

            // Tạo AuthService để xử lý LOGIN
            authService = new AuthService(client);
            System.out.println("[CLIENT] ✅ AuthService created & login handler registered");

            // Tạo LoginController và gán AuthService
            loginController = new LoginController(authService);
            System.out.println("[CLIENT] ✅ LoginController created");

            // Tạo giao diện LoginForm
            javax.swing.SwingUtilities.invokeLater(() -> {
                LoginForm loginForm = new LoginForm(loginController);
                loginController.setLoginForm(loginForm);
                loginForm.setVisible(true);
                System.out.println("[CLIENT] 🖥️ UI initialized successfully!");
            });

            // Bắt đầu lắng nghe phản hồi từ server
            client.listen();
            System.out.println("[CLIENT] 🔊 Connected & listening for server messages!");

        } catch (Exception e) {
            System.err.println("[CLIENT] ❌ Error initializing client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * ✅ Dùng khi người dùng logout — khởi động lại toàn bộ client.
     */
    public static void restartClient() {
        System.out.println("[CLIENT] 🔄 Restarting client after logout...");
        try {
            if (client != null) {
                client.stop();
                System.out.println("[CLIENT] 🔌 Connection closed.");
            }

            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    ClientMain.main(new String[]{}); // Gọi lại main()
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
