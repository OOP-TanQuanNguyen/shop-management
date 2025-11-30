package edu.ptithcm.app.reducers;

import edu.ptithcm.app.actions.CustomerAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CustomerModel;
import edu.ptithcm.models.LoyaltyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerReducer {

    @SuppressWarnings("unchecked")
    public static void register(Store store) {

        store.registerReducer(CustomerAction.CUSTOMER_UPDATE_LIST.toString(), payload -> {

            if (payload instanceof List<?> rawList) {

                List<CustomerModel> customers = ((List<Map<String, Object>>) rawList)
                        .stream()
                        .map(CustomerModel::fromMap)
                        .collect(Collectors.toList());

                Object loyaltyObj = store.getAppState().get("Loyalties");

                if (loyaltyObj instanceof List<?> loyList) {
                    List<LoyaltyInfo> loyalties = (List<LoyaltyInfo>) loyList;

                    for (CustomerModel c : customers) {
                        for (LoyaltyInfo l : loyalties) {
                            if (c.getId().equals(l.getCustomerId())) {
                                c.setPoint(l.getTotalPoints());
                                break;
                            }
                        }
                    }
                }

                store.getAppState().set("Customers", new ArrayList<>(customers));
            }
        });

        store.registerReducer(CustomerAction.CUSTOMER_ADD_SUCCESS.toString(), payload -> {

        });

        store.registerReducer(CustomerAction.CUSTOMER_UPDATE_SUCCESS.toString(), payload
                -> store.getAppState().set("CustomerMessage", "Cập nhật khách hàng thành công!")
        );

        store.registerReducer(CustomerAction.CUSTOMER_DELETE_SUCCESS.toString(), payload
                -> store.getAppState().set("CustomerMessage", "Xóa khách hàng thành công!")
        );

        store.registerReducer(CustomerAction.CUSTOMER_ERROR.toString(), payload
                -> store.getAppState().set("CustomerError", payload)
        );
    }
}
