package edu.ptithcm.app.reducers;

import edu.ptithcm.app.actions.InventoryAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.InventoryModel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryReducer {

    private InventoryReducer() {}

    @SuppressWarnings("unchecked")
    public static void register(Store store) {
        // Cập nhật danh sách
        store.registerReducer(
            InventoryAction.INVENTORY_UPDATE_LIST.toString(),
            payload -> {
                Object data = payload;

                if (
                    payload instanceof Map<?, ?> m &&
                    m.get("inventories") instanceof List<?>
                ) {
                    data = m.get("inventories");
                }

                if (data instanceof List<?> raw) {
                    List<InventoryModel> list = raw
                        .stream()
                        .map(item ->
                            InventoryModel.fromMap((Map<String, Object>) item)
                        )
                        .collect(Collectors.toList());

                    store.getAppState().set("Inventories", list);
                }
            }
        );

        // SUCCESS
        store.registerReducer(
            InventoryAction.INVENTORY_CREATE_SUCCESS.toString(),
            p ->
                store
                    .getAppState()
                    .set("InventoryMessage", "Tạo kho thành công!")
        );

        store.registerReducer(
            InventoryAction.INVENTORY_UPDATE_SUCCESS.toString(),
            p ->
                store
                    .getAppState()
                    .set("InventoryMessage", "Cập nhật kho thành công!")
        );

        store.registerReducer(
            InventoryAction.INVENTORY_DELETE_SUCCESS.toString(),
            p ->
                store
                    .getAppState()
                    .set("InventoryMessage", "Xóa kho thành công!")
        );

        // ERROR
        store.registerReducer(
            InventoryAction.INVENTORY_ERROR.toString(),
            payload -> store.getAppState().set("InventoryError", payload)
        );
    }
}
