package edu.ptithcm.controllers.pos;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.services.pos.POSServices;
import edu.ptithcm.services.admin.CustomerService;
import edu.ptithcm.views.pos.POSForm;
import edu.ptithcm.views.pos.panels.CustomerPanel;

public class POSController {

    private final POSForm view;
    private final DTTP client;
    private final Store store = Store.getInstance();

    public POSController(POSForm view, DTTP client) {
        this.view = view;
        this.client = client;

        registerEvent();

        store.subcribe(this::handleState);
    }

    private void registerEvent() {
        view.getLogoutButton().addActionListener(e -> POSServices.handleLogout(this.view));
    }

    // -------------------------------
    //      CUSTOMER MODULE
    // -------------------------------
    private void handleState(AppState state) {
        // xử lý state POS nếu cần
    }
}
