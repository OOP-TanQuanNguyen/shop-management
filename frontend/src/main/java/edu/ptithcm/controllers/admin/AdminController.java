package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.services.admin.EmployeeService;
import edu.ptithcm.services.admin.ProductService;
import edu.ptithcm.views.admin.AdminForm;
import edu.ptithcm.views.admin.EmployeePanel;
import edu.ptithcm.views.admin.ProductPanel;
import edu.ptithcm.views.admin.BranchPanel;
import edu.ptithcm.services.admin.BranchService;

import javax.swing.*;
import java.util.logging.Logger;

public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    private final AdminForm view;
    private final DTTP client;
    private final Store store = Store.getInstance();

    public AdminController(AdminForm view, DTTP client) {
        this.view = view;
        this.client = client;

        registerEvent();
        initEmployeeModule();
<<<<<<< Updated upstream
        initProductModule();
=======
        initProductModule(); // The method being called
        initBranchModule(); // ✅ THÊM DÒNG NÀY

>>>>>>> Stashed changes
        store.subcribe(this::handleState);

        logger.info("AdminController initialized successfully");
    }

    private void registerEvent() {
        view.getLogoutButton().addActionListener(e -> handleLogout());
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Bạn có chắc muốn đăng xuất?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Clear store
                store.getAppState().clear();
                logger.info("Store cleared");

                // Đóng AdminForm
                view.dispose();
                logger.info("AdminForm disposed");

                // Mở lại LoginForm
                SwingUtilities.invokeLater(() -> {
                    // TODO: Thêm code mở LoginForm khi cần
                    // new LoginForm().setVisible(true);
                    logger.info("User logged out successfully");
                    System.exit(0); // Tạm thời thoát app, bỏ dòng này khi có LoginForm
                });

            } catch (Exception ex) {
                logger.severe("Logout error: " + ex.getMessage());
                JOptionPane.showMessageDialog(
                        view,
                        "Lỗi khi đăng xuất: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void initEmployeeModule() {
        try {
            EmployeePanel employeePanel = view.getEmployeePanel();
            EmployeeService employeeService = new EmployeeService(client);

            new EmployeeController(employeePanel, employeeService);

            logger.info("EmployeeController initialized successfully");
        } catch (Exception e) {
            logger.severe("Failed to initialize EmployeeController: " + e.getMessage());
            showInitError("Nhân viên", e);
        }
    }

    private void initProductModule() {
        try {
            ProductPanel productPanel = view.getProductPanel();
            ProductService productService = new ProductService(client);

            new ProductController(productPanel, productService);

            logger.info("ProductController initialized successfully");
        } catch (Exception e) {
            logger.severe("Failed to initialize ProductController: " + e.getMessage());
            showInitError("Sản phẩm", e);
        }
    }

<<<<<<< Updated upstream
    private void showInitError(String moduleName, Exception e) {
        SwingUtilities.invokeLater(()
                -> JOptionPane.showMessageDialog(
                        view,
                        String.format("Không thể khởi tạo module %s: %s", moduleName, e.getMessage()),
                        "Lỗi khởi tạo",
                        JOptionPane.ERROR_MESSAGE
                )
        );
=======
    private void initBranchModule() {
        try {
            BranchPanel branchPanel = view.getBranchPanel();
            BranchService branchService = new BranchService(client);
            new BranchController(branchPanel, branchService);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init EmployeeController: " + e.getMessage());
        }
>>>>>>> Stashed changes
    }

    private void handleState(AppState state) {
        // Handle global admin state changes if needed
    }
}
