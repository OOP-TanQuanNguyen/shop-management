package edu.ptithcm.app.reducers;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.actions.LoyaltyAction;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.models.LoyaltyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoyaltyReducer {

    public static void register(edu.ptithcm.app.store.Store store) {

        store.registerReducer(LoyaltyAction.LOYALTY_GET_ALL.toString(),
                payload -> reduce("LOYALTY_GET_ALL", payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_GET_BY_CUSTOMER.toString(),
                payload -> reduce("LOYALTY_GET_BY_CUSTOMER", payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_CREATE.toString(),
                payload -> reduce("LOYALTY_CREATE", payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_UPDATE.toString(),
                payload -> reduce("LOYALTY_UPDATE", payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_DELETE.toString(),
                payload -> reduce("LOYALTY_DELETE", payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_MESSAGE.toString(),
                payload -> reduce("LOYALTY_MESSAGE", payload, store.getAppState()));

        store.registerReducer(LoyaltyAction.LOYALTY_ERROR.toString(),
                payload -> reduce("LOYALTY_ERROR", payload, store.getAppState()));
    }

    @SuppressWarnings("unchecked")
    private static void reduce(String action, Object payload, AppState state) {

        switch (action) {

            /* ============================================================
               GET_ALL → SET LIST + MERGE
            ============================================================ */
            case "LOYALTY_GET_ALL" -> {

                List<LoyaltyInfo> list = new ArrayList<>();

                if (payload instanceof List<?> rawList) {
                    for (Object obj : rawList) {
                        if (obj instanceof Map<?, ?> map) {
                            list.add(LoyaltyInfo.fromMap((Map<String, Object>) map));
                        }
                    }
                }

                state.set("Loyalties", list);

                // merge với customers (dù customers có hay chưa)
                mergeLoyaltyIntoCustomers(state);
            }

            /* ============================================================
               GET_BY_CUSTOMER / CREATE / UPDATE
            ============================================================ */
            case "LOYALTY_GET_BY_CUSTOMER", "LOYALTY_CREATE", "LOYALTY_UPDATE" -> {

                LoyaltyInfo loyalty = null;

                if (payload instanceof Map<?, ?> map) {
                    loyalty = LoyaltyInfo.fromMap((Map<String, Object>) map);
                }

                state.set("CustomerLoyalty", loyalty);

                // merge lại toàn bộ customers
                mergeLoyaltyIntoCustomers(state);
            }

            /* ============================================================
               DELETE
            ============================================================ */
            case "LOYALTY_DELETE" -> {
                state.set("CustomerLoyalty", null);
                mergeLoyaltyIntoCustomers(state);
            }

            /* MESSAGE */
            case "LOYALTY_MESSAGE" ->
                state.set("LoyaltyMessage", payload);

            /* ERROR */
            case "LOYALTY_ERROR" ->
                state.set("LoyaltyError", payload);
        }
    }

    /* ============================================================
       Hàm MERGE CHUẨN — hoạt động cho mọi trường hợp load trước/sau
    ============================================================ */
    @SuppressWarnings("unchecked")
    private static void mergeLoyaltyIntoCustomers(AppState state) {

        Object cusObj = state.get("Customers");
        Object loyObj = state.get("Loyalties");

        // Nếu 1 trong 2 chưa có → chưa merge
        if (!(cusObj instanceof List<?> cusList)) {
            return;
        }
        if (!(loyObj instanceof List<?> loyList)) {
            return;
        }

        List<CustomerModel> customers = (List<CustomerModel>) cusList;
        List<LoyaltyInfo> loyalties = (List<LoyaltyInfo>) loyList;

        // reset trước để tránh dữ liệu cũ
        for (CustomerModel c : customers) {
            c.setPoint(0);
        }

        // merge loyalty → customer
        for (CustomerModel c : customers) {
            for (LoyaltyInfo l : loyalties) {
                if (c.getId().equals(l.getCustomerId())) {
                    c.setPoint(l.getTotalPoints());
                    break;
                }
            }
        }

        // cập nhật lại appState
        state.set("Customers", new ArrayList<>(customers));
    }
}
