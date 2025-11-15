package edu.ptithcm.app.reducers;

import edu.ptithcm.app.store.Store;
import edu.ptithcm.app.actions.ProductAction;
import edu.ptithcm.models.ProductInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductReducer {

    @SuppressWarnings("unchecked")
    public static void register(Store store) {
        store.registerReducer(ProductAction.PRODUCT_UPDATE_LIST.toString(), payload -> {
            if (payload instanceof List<?>) {
                // payload is expected List<Map<String,Object>> from BE
                List<Map<String, Object>> raw = (List<Map<String, Object>>) payload;
                List<ProductInfo> list = raw.stream()
                        .map(ProductInfo::new) // assumes ProductInfo has constructor from Map
                        .collect(Collectors.toList());
                store.getAppState().set("Products", list);
            }
        });

        store.registerReducer(ProductAction.PRODUCT_ADD_SUCCESS.toString(), payload -> {
            store.getAppState().set("ProductMessage", "Thêm sản phẩm thành công!");
        });

        store.registerReducer(ProductAction.PRODUCT_UPDATE_SUCCESS.toString(), payload -> {
            store.getAppState().set("ProductMessage", "Cập nhật sản phẩm thành công!");
        });

        store.registerReducer(ProductAction.PRODUCT_DELETE_SUCCESS.toString(), payload -> {
            store.getAppState().set("ProductMessage", "Xóa sản phẩm thành công!");
        });

        store.registerReducer(ProductAction.PRODUCT_ERROR.toString(), payload -> {
            store.getAppState().set("ProductError", payload);
        });
    }
}
