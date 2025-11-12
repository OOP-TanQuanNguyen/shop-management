// sửa file: AdminController.java
package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.services.admin.AdminService;
import edu.ptithcm.services.admin.EmployeeService;
import edu.ptithcm.views.admin.AdminForm;
import edu.ptithcm.views.admin.EmployeePanel;

public class AdminController {

    private final AdminForm view;
    private final DTTP client;
    private final Store store = Store.getInstance();

    public AdminController(AdminForm view, DTTP client) { // ✅ thêm tham số client
        this.view = view;
        this.client = client;

        registerEvent();
        initEmployeeModule();
        store.subcribe(this::handleState);
    }

    private void registerEvent() {
        view.getLogoutButton().addActionListener(e -> AdminService.handleLogout(this.view));
    }

    private void initEmployeeModule() {
        try {
            EmployeePanel employeePanel = view.getEmployeePanel();
            EmployeeService employeeService = new EmployeeService(client);
            new EmployeeController(employeePanel, employeeService);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to init EmployeeController: " + e.getMessage());
        }
    }

    private void handleState(AppState state) {
        // TODO: handle global admin state changes
    }
}
