package edu.ptithcm.services.pos;

import edu.ptithcm.app.store.Store;
import edu.ptithcm.views.pos.POSForm;

public class POSServices {
    public static void handleLogout(POSForm view){
        Store.getInstance().dispatch("LOGOUT", null);
        view.dispose();
    }
}
