package edu.ptithcm.services.admin;

import edu.ptithcm.app.store.Store;
import edu.ptithcm.views.admin.AdminForm;

public class AdminService {
    public static void handleLogout(AdminForm view){
        Store.getInstance().dispatch("LOGOUT", null);
        view.dispose();
    }
}
