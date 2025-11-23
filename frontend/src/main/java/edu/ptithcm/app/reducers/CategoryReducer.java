package edu.ptithcm.app.reducers;

import edu.ptithcm.app.actions.CategoryAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CategoryInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryReducer {

    @SuppressWarnings("unchecked")
    public static void register(Store store) {

        store.registerReducer(CategoryAction.CATEGORY_UPDATE_LIST.toString(), payload -> {
            if (payload instanceof List<?>) {
                List<Map<String, Object>> raw = (List<Map<String, Object>>) payload;

                List<CategoryInfo> list = raw.stream()
                        .map(CategoryInfo::new)
                        .collect(Collectors.toList());

                store.getAppState().set("Categories", list);
            }
        });

        store.registerReducer(CategoryAction.CATEGORY_ADD_SUCCESS.toString(), p
                -> store.getAppState().set("CategoryMessage", "Thêm danh mục thành công!"));

        store.registerReducer(CategoryAction.CATEGORY_UPDATE_SUCCESS.toString(), p
                -> store.getAppState().set("CategoryMessage", "Cập nhật danh mục thành công!"));

        store.registerReducer(CategoryAction.CATEGORY_DELETE_SUCCESS.toString(), p
                -> store.getAppState().set("CategoryMessage", "Xóa danh mục thành công!"));

        store.registerReducer(CategoryAction.CATEGORY_ERROR.toString(), payload
                -> store.getAppState().set("CategoryError", payload));
    }
}
