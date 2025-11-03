package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.services.admin.AdminService;
import edu.ptithcm.views.admin.AdminForm;

public class AdminController {
    private AdminForm view;

    public AdminController(AdminForm view) {
        this.view = view;
        this.registerEvent();
        Store.getInstance().subcribe(this::handleState);
    }

    private void registerEvent(){
        System.out.println("[DEBUG] Register logout button listener");
        view.getLogoutButton().addActionListener(e -> AdminService.handleLogout(this.view));
    }

    private void handleState(AppState state){

    }
}
