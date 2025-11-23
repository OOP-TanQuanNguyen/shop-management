package edu.ptithcm.app.reducers;

import edu.ptithcm.app.actions.CategoryAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CategoryModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryReducer {

    @SuppressWarnings("unchecked")
    public static void register(Store store) {

        // ========================= UPDATE LIST =========================
        store.registerReducer(CategoryAction.CATEGORY_UPDATE_LIST.toString(), payload -> {

            if (payload instanceof List<?>) {
                List<Map<String, Object>> raw = (List<Map<String, Object>>) payload;

                List<CategoryModel> list = raw.stream()
                        .map(CategoryModel::fromMap)
                        .collect(Collectors.toList());

                store.getAppState().set("Categories", list);
            }
        });

        // ========================= GET BY ID SUCCESS =========================
        store.registerReducer(
                CategoryAction.CATEGORY_GET_BY_ID_SUCCESS.toString(),
                payload -> {
                    if (payload instanceof Map<?, ?> map) {
                        // payload là map từ server (CategoryInfo.toMap())
                        store.getAppState().set("CurrentCategory", map);
                    }
                }
        );

        // ========================= ADD SUCCESS =========================
        store.registerReducer(
                CategoryAction.CATEGORY_ADD_SUCCESS.toString(),
                p -> store.getAppState().set("CategoryMessage", "Thêm danh mục thành công!")
        );

        // ========================= UPDATE SUCCESS =========================
        store.registerReducer(
                CategoryAction.CATEGORY_UPDATE_SUCCESS.toString(),
                p -> store.getAppState().set("CategoryMessage", "Cập nhật danh mục thành công!")
        );

        // ========================= DELETE SUCCESS =========================
        store.registerReducer(
                CategoryAction.CATEGORY_DELETE_SUCCESS.toString(),
                p -> store.getAppState().set("CategoryMessage", "Xóa danh mục thành công!")
        );

        // ========================= ERROR =========================
        store.registerReducer(
                CategoryAction.CATEGORY_ERROR.toString(),
                payload -> store.getAppState().set("CategoryError", payload)
        );
    }
}
