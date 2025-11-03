package edu.ptithcm.controllers.pos;

import edu.ptithcm.services.pos.POSServices;
import edu.ptithcm.views.pos.POSForm;

public class POSController {
    private POSForm view;

    public POSController(POSForm view){
        this.view = view;
        this.registerHandler();
    }

    public void registerHandler(){
        this.view.getLogoutButton().addActionListener(e -> POSServices.handleLogout(this.view));
    }
}
