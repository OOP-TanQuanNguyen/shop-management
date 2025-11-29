package edu.ptithcm.app.reducers;

import edu.ptithcm.app.actions.CustomerAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CustomerModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomerReducer {

    @SuppressWarnings("unchecked")
    public static void register(Store store) {

        /* ============================================================
           UPDATE LIST (CUSTOMER_GET_ALL, CUSTOMER_SEARCH)
        ============================================================ */
        store.registerReducer(CustomerAction.CUSTOMER_UPDATE_LIST.toString(), payload -> {
            if (payload instanceof List<?> rawList) {

                List<CustomerModel> list = ((List<Map<String, Object>>) rawList)
                        .stream()
                        .map(CustomerModel::fromMap)
                        .collect(Collectors.toList());

                store.getAppState().set("Customers", list);
            }
        });

        /* ============================================================
           ADD
        ============================================================ */
        store.registerReducer(CustomerAction.CUSTOMER_ADD_SUCCESS.toString(), payload -> {
            store.getAppState().set("CustomerMessage", "Thêm khách hàng thành công!");
        });

        /* ============================================================
           UPDATE
        ============================================================ */
        store.registerReducer(CustomerAction.CUSTOMER_UPDATE_SUCCESS.toString(), payload -> {
            store.getAppState().set("CustomerMessage", "Cập nhật khách hàng thành công!");
        });

        /* ============================================================
           DELETE
        ============================================================ */
        store.registerReducer(CustomerAction.CUSTOMER_DELETE_SUCCESS.toString(), payload -> {
            store.getAppState().set("CustomerMessage", "Xóa khách hàng thành công!");
        });

        /* ============================================================
           ERROR
        ============================================================ */
        store.registerReducer(CustomerAction.CUSTOMER_ERROR.toString(), payload -> {
            store.getAppState().set("CustomerError", payload);
        });
    }
}
