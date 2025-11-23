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

import edu.ptithcm.views.admin.AdminForm;
import edu.ptithcm.views.admin.EmployeePanel;
import edu.ptithcm.views.admin.ProductPanel;
import edu.ptithcm.views.admin.BranchPanel;
import edu.ptithcm.views.admin.CustomerPanel;
import edu.ptithcm.views.admin.CategoryPanel;

public class AdminController {

    private final AdminForm view;
    private final DTTP client;
    private final Store store = Store.getInstance();

    // Shared services
    private BranchService branchService;
    private CategoryService categoryService;

    public AdminController(AdminForm view, DTTP client) {
        this.view = view;
        this.client = client;

        registerEvent();

        // ORDER VERY IMPORTANT
        initBranchModule();
        initCategoryModule();   // must run BEFORE Product + Employee
        initEmployeeModule();
        initProductModule();
        initCustomerModule();

        store.subcribe(this::handleState);
    }

    private void registerEvent() {
        view.getLogoutButton().addActionListener(e -> AdminService.handleLogout(this.view));
    }

    // ==============================
    // MODULE BRANCH
    // ==============================
    private void initBranchModule() {
        try {
            BranchPanel branchPanel = view.getBranchPanel();
            branchService = new BranchService(client);
            new BranchController(branchPanel, branchService);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init BranchController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==============================
    // MODULE CATEGORY
    // ==============================
    private void initCategoryModule() {
        try {
            CategoryPanel categoryPanel = view.getCategoryPanel();
            categoryService = new CategoryService(client);
            new CategoryController(categoryPanel, categoryService);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init CategoryController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==============================
    // MODULE EMPLOYEE (NEEDS BRANCH)
    // ==============================
    private void initEmployeeModule() {
        try {
            EmployeePanel employeePanel = view.getEmployeePanel();
            EmployeeService employeeService = new EmployeeService(client);

            // pass BranchService to EmployeeController
            new EmployeeController(employeePanel, employeeService, branchService);

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init EmployeeController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==============================
    // MODULE PRODUCT (NEEDS CATEGORY)
    // ==============================
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

    // ==============================
    // MODULE CUSTOMER
    // ==============================
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
        // you can handle global states here
    }
}
