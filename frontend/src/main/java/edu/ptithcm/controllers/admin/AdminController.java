package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

import edu.ptithcm.services.admin.AdminService;
import edu.ptithcm.services.admin.EmployeeService;
import edu.ptithcm.services.admin.ProductService;
import edu.ptithcm.services.admin.BranchService;
import edu.ptithcm.services.admin.CustomerService;
import edu.ptithcm.services.admin.CategoryService;
import edu.ptithcm.services.admin.InventoryService;
import edu.ptithcm.services.admin.LoyaltyService;   // ★ thêm

import edu.ptithcm.services.pos.InvoiceService;

import edu.ptithcm.views.admin.AdminForm;
import edu.ptithcm.views.admin.EmployeePanel;
import edu.ptithcm.views.admin.ProductPanel;
import edu.ptithcm.views.admin.BranchPanel;
import edu.ptithcm.views.admin.CustomerPanel;
import edu.ptithcm.views.admin.CategoryPanel;
import edu.ptithcm.views.admin.InventoryPanel;
import edu.ptithcm.views.admin.AdminInvoicePanel;

public class AdminController {

    private final AdminForm view;
    private final DTTP client;
    private final Store store = Store.getInstance();

    // Shared services
    private BranchService branchService;
    private CategoryService categoryService;

    // ★ LoyaltyService dùng chung
    private LoyaltyService loyaltyService;

    public AdminController(AdminForm view, DTTP client) {
        this.view = view;
        this.client = client;

        registerEvent();

        // ===== ORDER VERY IMPORTANT =====
        initBranchModule();
        initCategoryModule();
        initEmployeeModule();     // needs Branch
        initProductModule();      // needs Category
        initInventoryModule();    // needs Product & Category
        initLoyaltyModule();      // ★ thêm module Loyalty
        initCustomerModule();     // Customer dùng LoyaltyService
        initInvoiceModule();

        store.subcribe(this::handleState);
    }

    private void registerEvent() {
        view.getLogoutButton().addActionListener(e -> AdminService.handleLogout(this.view));
    }

    // ==============================
    private void initBranchModule() {
        try {
            BranchPanel panel = view.getBranchPanel();
            branchService = new BranchService(client);
            new BranchController(panel, branchService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCategoryModule() {
        try {
            CategoryPanel panel = view.getCategoryPanel();
            categoryService = new CategoryService(client);
            new CategoryController(panel, categoryService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEmployeeModule() {
        try {
            EmployeePanel panel = view.getEmployeePanel();
            EmployeeService service = new EmployeeService(client);
            new EmployeeController(panel, service, branchService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initProductModule() {
        try {
            ProductPanel panel = view.getProductPanel();
            ProductService service = new ProductService(client);
            new ProductController(panel, service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initInventoryModule() {
        try {
            InventoryPanel panel = view.getInventoryPanel();
            InventoryService service = new InventoryService(client);
            new InventoryController(panel, service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // ★ NEW MODULE — LOYALTY SERVICE
    // ======================================================
    private void initLoyaltyModule() {
        try {
            loyaltyService = new LoyaltyService(client);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init LoyaltyService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initCustomerModule() {
        try {
            CustomerPanel panel = view.getCustomerPanel();
            CustomerService service = new CustomerService(client);

            // ★ CustomerController giờ nhận LoyaltyService
            new CustomerController(panel, service, loyaltyService);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================================================
    // INVOICE (không đổi cấu trúc)
    // ======================================================
    private void initInvoiceModule() {
        try {
            AdminInvoicePanel panel = view.getAdminInvoicePanel();
            InvoiceService service = new InvoiceService(client);

            new AdminInvoiceController(panel, service);

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init InvoiceController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleState(AppState state) {
        // nothing here yet
    }
}
