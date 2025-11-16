package edu.ptithcm.app.reducers;

import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.app.actions.EmployeeAction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeReducer {

    @SuppressWarnings("unchecked")
    public static void register(Store store) {

        store.registerReducer(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), payload -> {
            if (payload instanceof List<?>) {
                List<Map<String, Object>> raw = (List<Map<String, Object>>) payload;
                List<UserModel> list = raw.stream()
                        .map(UserModel::fromMap)
                        .collect(Collectors.toList());
                store.getAppState().set("Employees", list);
            }
        });

        store.registerReducer(EmployeeAction.EMPLOYEE_ADD_SUCCESS.toString(), payload
                -> store.getAppState().set("EmployeeMessage", "Thêm nhân viên thành công!")
        );

        store.registerReducer(EmployeeAction.EMPLOYEE_UPDATE_SUCCESS.toString(), payload
                -> store.getAppState().set("EmployeeMessage", "Cập nhật nhân viên thành công!")
        );

        store.registerReducer(EmployeeAction.EMPLOYEE_DELETE_SUCCESS.toString(), payload
                -> store.getAppState().set("EmployeeMessage", "Xóa nhân viên thành công!")
        );

        store.registerReducer(EmployeeAction.EMPLOYEE_ERROR.toString(), payload
                -> store.getAppState().set("EmployeeError", payload)
        );
    }
}
