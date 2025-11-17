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

        // Cập nhật danh sách khách hàng
        store.registerReducer(CustomerAction.CUSTOMER_UPDATE_LIST.toString(), payload -> {
            if (payload instanceof List<?>) {
                List<Map<String, Object>> raw = (List<Map<String, Object>>) payload;

                List<CustomerModel> list = raw.stream()
                        .map(CustomerModel::fromMap)
                        .collect(Collectors.toList());

                store.getAppState().set("Customers", list);
            }
        });

        store.registerReducer(CustomerAction.CUSTOMER_ADD_SUCCESS.toString(), p
                -> store.getAppState().set("CustomerMessage", "Thêm khách hàng thành công!")
        );

        store.registerReducer(CustomerAction.CUSTOMER_UPDATE_SUCCESS.toString(), p
                -> store.getAppState().set("CustomerMessage", "Cập nhật khách hàng thành công!")
        );

        store.registerReducer(CustomerAction.CUSTOMER_DELETE_SUCCESS.toString(), p
                -> store.getAppState().set("CustomerMessage", "Xóa khách hàng thành công!")
        );

        store.registerReducer(CustomerAction.CUSTOMER_ERROR.toString(), payload
                -> store.getAppState().set("CustomerError", payload)
        );
    }
}
