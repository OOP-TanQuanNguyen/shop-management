package edu.ptithcm.app.reducers;

import edu.ptithcm.app.store.Store;
import edu.ptithcm.app.actions.BranchAction;
import edu.ptithcm.models.BranchInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BranchReducer {

    @SuppressWarnings("unchecked")
    public static void register(Store store) {
        store.registerReducer(BranchAction.BRANCH_UPDATE_LIST.toString(), payload -> {
            if (payload instanceof List<?>) {
                List<Map<String, Object>> raw = (List<Map<String, Object>>) payload;
                List<BranchInfo> list = raw.stream()
                        .map(BranchInfo::new)
                        .collect(Collectors.toList());
                store.getAppState().set("Branches", list);
            }
        });

        store.registerReducer(BranchAction.BRANCH_ADD_SUCCESS.toString(), payload -> {
            store.getAppState().set("BranchMessage", "Thêm chi nhánh thành công!");
        });

        store.registerReducer(BranchAction.BRANCH_UPDATE_SUCCESS.toString(), payload -> {
            store.getAppState().set("BranchMessage", "Cập nhật chi nhánh thành công!");
        });

        store.registerReducer(BranchAction.BRANCH_DELETE_SUCCESS.toString(), payload -> {
            store.getAppState().set("BranchMessage", "Xóa chi nhánh thành công!");
        });

        store.registerReducer(BranchAction.BRANCH_ERROR.toString(), payload -> {
            store.getAppState().set("BranchError", payload);
        });
    }
}
