package edu.ptithcm.app.reducers;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.actions.LoyaltyAction;
import edu.ptithcm.models.LoyaltyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoyaltyReducer {

    /* ============================================================
       REGISTER WITH STORE (bạn đang thiếu function này)
    ============================================================ */
    public static void register(edu.ptithcm.app.store.Store store) {

        store.registerReducer(LoyaltyAction.LOYALTY_GET_ALL.toString(),
                (payload) -> reduce(LoyaltyAction.LOYALTY_GET_ALL.toString(), payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_GET_BY_CUSTOMER.toString(),
                (payload) -> reduce(LoyaltyAction.LOYALTY_GET_BY_CUSTOMER.toString(), payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_CREATE.toString(),
                (payload) -> reduce(LoyaltyAction.LOYALTY_CREATE.toString(), payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_UPDATE.toString(),
                (payload) -> reduce(LoyaltyAction.LOYALTY_UPDATE.toString(), payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_DELETE.toString(),
                (payload) -> reduce(LoyaltyAction.LOYALTY_DELETE.toString(), payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_MESSAGE.toString(),
                (payload) -> reduce(LoyaltyAction.LOYALTY_MESSAGE.toString(), payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_ERROR.toString(),
                (payload) -> reduce(LoyaltyAction.LOYALTY_ERROR.toString(), payload, store.getAppState()));
    }

    /* ============================================================
       REDUCER CORE LOGIC
    ============================================================ */
    @SuppressWarnings("unchecked")
    public static void reduce(String action, Object payload, AppState state) {

        switch (action) {

            case "LOYALTY_GET_ALL" -> {
                if (payload instanceof List<?> list) {
                    List<LoyaltyInfo> loyalties = new ArrayList<>();

                    for (Object obj : list) {
                        if (obj instanceof Map<?, ?> map) {
                            loyalties.add(LoyaltyInfo.fromMap((Map<String, Object>) map));
                        }
                    }

                    state.set("Loyalties", loyalties);
                }
            }

            case "LOYALTY_GET_BY_CUSTOMER", "LOYALTY_CREATE", "LOYALTY_UPDATE" -> {

                if (payload == null) {
                    state.set("CustomerLoyalty", null);
                    return;
                }

                if (payload instanceof Map<?, ?> map) {
                    LoyaltyInfo loyalty = LoyaltyInfo.fromMap((Map<String, Object>) map);
                    state.set("CustomerLoyalty", loyalty);
                }
            }

            case "LOYALTY_DELETE" ->
                state.set("CustomerLoyalty", null);

            case "LOYALTY_MESSAGE" ->
                state.set("LoyaltyMessage", payload);

            case "LOYALTY_ERROR" ->
                state.set("LoyaltyError", payload);
        }
    }
}
