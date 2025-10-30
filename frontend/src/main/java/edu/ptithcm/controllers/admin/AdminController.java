package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.services.admin.AdminService;
import edu.ptithcm.views.admin.AdminForm;

public class AdminController {
    private AdminForm view;
    private AdminService service;

    public AdminController(AdminForm view,AdminService service) {
        this.view = view;
        this.service = service;
        this.registerEvent();
        Store.getInstance().subcribe(this::handleState);
    }

    private void registerEvent(){
        System.out.println("[DEBUG] Register logout button listener");
        view.getLogoutButton().addActionListener(e -> this.handleLogout());
    }

    private void handleLogout(){
        System.out.println("Click LogOut");
        Store.getInstance().dispatch("LOGOUT", null);
        this.view.dispose();
    }

    private void handleState(AppState state){

    }
}
