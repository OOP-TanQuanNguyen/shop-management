package edu.ptithcm.app.reducers;

import edu.ptithcm.app.store.Store;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.app.actions.AdminAction;

public class EmployeeReducer {

    @SuppressWarnings("unchecked")
    public static void register(Store store) {

        // Cập nhật danh sách nhân viên
        store.registerReducer(AdminAction.EMPLOYEE_UPDATE_LIST.toString(), payload -> {
            if (payload instanceof List<?>) {
                List<Map<String, Object>> rawList = (List<Map<String, Object>>) payload;
                List<UserModel> employees = rawList.stream()
                        .map(UserModel::fromMap)
                        .collect(Collectors.toList());
                store.getAppState().set("Employees", employees);
            }
        });

        // Thêm nhân viên thành công
        store.registerReducer(AdminAction.EMPLOYEE_ADD_SUCCESS.toString(), payload -> {
            store.getAppState().set("EmployeeMessage", "Thêm nhân viên thành công!");
        });

        // Cập nhật nhân viên
        store.registerReducer(AdminAction.EMPLOYEE_UPDATE_SUCCESS.toString(), payload -> {
            store.getAppState().set("EmployeeMessage", "Cập nhật nhân viên thành công!");
        });

        // Xóa nhân viên
        store.registerReducer(AdminAction.EMPLOYEE_DELETE_SUCCESS.toString(), payload -> {
            store.getAppState().set("EmployeeMessage", "Xóa nhân viên thành công!");
        });

        // Lỗi
        store.registerReducer(AdminAction.EMPLOYEE_ERROR.toString(), payload -> {
            store.getAppState().set("EmployeeError", payload);
        });
    }
}
