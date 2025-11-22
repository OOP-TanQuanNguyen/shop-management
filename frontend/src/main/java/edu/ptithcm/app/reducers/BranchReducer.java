package edu.ptithcm.app.reducers;

import edu.ptithcm.app.store.Store;
import edu.ptithcm.app.actions.BranchAction;
import edu.ptithcm.models.BranchInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BranchReducer {

    private BranchReducer() {
    }

    @SuppressWarnings("unchecked")
    public static void register(Store store) {

        // =====================================================================
        // UPDATE LIST
        // =====================================================================
        store.registerReducer(BranchAction.BRANCH_UPDATE_LIST.toString(), payload -> {

            if (payload instanceof List<?> rawList) {

                List<BranchInfo> list = rawList.stream()
                        .filter(o -> o instanceof Map)
                        .map(o -> BranchInfo.fromMap((Map<String, Object>) o))
                        .collect(Collectors.toList());

                store.getAppState().set("Branches", list);
            }
        });

        // =====================================================================
        // ERROR
        // =====================================================================
        store.registerReducer(BranchAction.BRANCH_ERROR.toString(), payload -> {
            if (payload instanceof String msg) {
                store.getAppState().set("BranchError", msg);
            }
        });

        // =====================================================================
        // SUCCESS MESSAGE — không cần payload
        // =====================================================================
        store.registerReducer(BranchAction.BRANCH_ADD_SUCCESS.toString(),
                payload -> store.getAppState().set("BranchMessage", "Thêm chi nhánh thành công!"));

        store.registerReducer(BranchAction.BRANCH_UPDATE_SUCCESS.toString(),
                payload -> store.getAppState().set("BranchMessage", "Cập nhật chi nhánh thành công!"));

        store.registerReducer(BranchAction.BRANCH_DELETE_SUCCESS.toString(),
                payload -> store.getAppState().set("BranchMessage", "Xóa chi nhánh thành công!"));
    }
}
