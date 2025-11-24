package edu.ptithcm.middleware;

import javax.swing.SwingUtilities;

import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;
import edu.ptithcm.views.components.AppMessageBox;

public class SystemMiddleWare {

    public static void start(DTTP client) {
        client.on("PING",(DTTP.DTTPArgs args)->{
            client.send("PING_RESPONSE", null, "SUCCESS", "");
        });
    }

    public static void handleDifferenceLogin(DTTP client){
        client.on("FORCE_KICK",args -> {
            SwingUtilities.invokeLater(() -> {
                AppMessageBox.showError(args.message);
            });
            Store.getInstance().dispatch("DOUBLE_CONNECTION", args.message);
        });
    }

}

