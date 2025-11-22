package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

import edu.ptithcm.services.admin.AdminService;
import edu.ptithcm.services.admin.EmployeeService;
import edu.ptithcm.services.admin.ProductService;
import edu.ptithcm.services.admin.BranchService;
import edu.ptithcm.services.admin.CustomerService;

import edu.ptithcm.views.admin.AdminForm;
import edu.ptithcm.views.admin.EmployeePanel;
import edu.ptithcm.views.admin.ProductPanel;
import edu.ptithcm.views.admin.BranchPanel;
import edu.ptithcm.views.admin.CustomerPanel;

public class AdminController {

    private final AdminForm view;
    private final DTTP client;
    private final Store store = Store.getInstance();

    // ✅ Shared services
    private BranchService branchService;

    public AdminController(AdminForm view, DTTP client) {
        this.view = view;
        this.client = client;

        registerEvent();
        initBranchModule();     // ✅ Init Branch trước để Employee có thể dùng
        initEmployeeModule();
        initProductModule();
        initCustomerModule();

        store.subcribe(this::handleState);
    }

    private void registerEvent() {
        view.getLogoutButton().addActionListener(e -> AdminService.handleLogout(this.view));
    }

    // ✅ Init Branch Module trước
    private void initBranchModule() {
        try {
            BranchPanel branchPanel = view.getBranchPanel();
            branchService = new BranchService(client);  // ✅ Lưu instance để dùng chung
            new BranchController(branchPanel, branchService);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init BranchController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Employee Module - Truyền BranchService vào
    private void initEmployeeModule() {
        try {
            EmployeePanel employeePanel = view.getEmployeePanel();
            EmployeeService employeeService = new EmployeeService(client);
            new EmployeeController(employeePanel, employeeService, branchService);  // ✅ Truyền BranchService
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init EmployeeController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initProductModule() {
        try {
            ProductPanel productPanel = view.getProductPanel();
            ProductService productService = new ProductService(client);
            new ProductController(productPanel, productService);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init ProductController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===================================
    //   💠 MODULE CUSTOMER
    // ===================================
    private void initCustomerModule() {
        try {
            CustomerPanel customerPanel = view.getCustomerPanel();
            CustomerService customerService = new CustomerService(client);
            new CustomerController(customerPanel, customerService);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init CustomerController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleState(AppState state) {
        // TODO: handle global admin state changes
    }
}
