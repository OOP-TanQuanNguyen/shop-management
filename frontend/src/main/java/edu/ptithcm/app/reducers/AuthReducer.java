package edu.ptithcm.app.reducers;

import edu.ptithcm.app.actions.AuthAction;
import edu.ptithcm.app.actions.SystemAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.UserModel;

public class AuthReducer {
    public static void register(Store store){
        store.registerReducer(AuthAction.LOGIN_SUCCESS, payload -> {
            UserModel user = (UserModel) payload;
            store.getAppState().set("user", user);
            store.getAppState().set("isAuthenticated", true);
        });

        store.registerReducer(AuthAction.LOGIN_FAIL, payload -> {
            store.getAppState().set("isAuthenticated", false);
        });

        store.registerReducer(AuthAction.LOGOUT, payload -> {
            store.getAppState().set("isLogout", true);
        });

        store.registerReducer(SystemAction.LOSS_CONNECTION_SERVER, payload -> {
            store.getAppState().set("isLossConnectionServer",true);
        });
    }
}
