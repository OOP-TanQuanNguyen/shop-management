package edu.ptithcm.frontend;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.ptithcm.frontend.routes.LoginRoutes;
import edu.ptithcm.frontend.protocols.DTTP;
import edu.ptithcm.frontend.services.AuthService;
import edu.ptithcm.frontend.controllers.LoginController;
import edu.ptithcm.frontend.view.LoginForm;

/**
 * Main entry point cho ứng dụng client. Hiển thị UI trước, kết nối server khi
 * cần.
 */
public class ClientMain {

    private static final String SERVER_HOST = "0.tcp.ap.ngrok.io";
    private static final int SERVER_PORT = 16495;

    private static DTTP client;
    private static AuthService authService;
    private static LoginController loginController;
    private static boolean connected = false;

    public static void main(String[] args) {
        // Hiển thị UI ngay lập tức
        SwingUtilities.invokeLater(() -> {
            initializeUI();
        });

        // Thử kết nối server trong background
        new Thread(() -> {
            connectToServer();
        }).start();
    }

    private static void initializeUI() {
        // Khởi tạo controller tạm (sẽ inject service sau khi connect)
        loginController = new LoginController(null);

        // Hiển thị login form
        LoginForm loginForm = new LoginForm(loginController);
        loginForm.setVisible(true);

        System.out.println("[CLIENT] ✅ UI initialized");
    }

    private static void connectToServer() {
        System.out.println("[CLIENT] 🔄 Connecting to " + SERVER_HOST + ":" + SERVER_PORT + "...");

        try {
            // 1. Khởi tạo DTTP client
            client = new DTTP(SERVER_HOST, SERVER_PORT);

            // 2. Khởi tạo services
            authService = new AuthService(client);

            // 3. Inject service vào controller
            loginController.setAuthService(authService);

            // 4. Setup routes
            new LoginRoutes(client);

            // 5. Bắt đầu lắng nghe
            client.listen();

            connected = true;
            System.out.println("[CLIENT] ✅ Connected successfully!");

            // 6. Shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("[CLIENT] 🔒 Shutting down...");
                if (client != null) {
                    client.stop();
                }
            }));

        } catch (Exception e) {
            connected = false;
            System.err.println("[CLIENT] ❌ Connection failed: " + e.getMessage());

            // Thông báo cho user nhưng vẫn để UI mở
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null,
                        "Không thể kết nối đến server!\n"
                        + "Server: " + SERVER_HOST + ":" + SERVER_PORT + "\n"
                        + "Lỗi: " + e.getMessage() + "\n\n"
                        + "Vui lòng kiểm tra:\n"
                        + "1. Server đã chạy chưa?\n"
                        + "2. Địa chỉ server đúng chưa?\n"
                        + "3. Kết nối mạng ổn định không?",
                        "Lỗi kết nối",
                        JOptionPane.ERROR_MESSAGE
                );
            });
        }
    }

    public static boolean isConnected() {
        return connected;
    }
}
