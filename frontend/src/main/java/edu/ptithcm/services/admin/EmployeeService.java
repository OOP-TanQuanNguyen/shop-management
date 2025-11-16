package edu.ptithcm.services.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.ptithcm.app.actions.EmployeeAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.protocols.DTTP;

public class EmployeeService {

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String REQUEST = "REQUEST";
    private static final String EMPLOYEES_KEY = "employees";

    private static final int RELOAD_DELAY = 120;

    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public EmployeeService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    private void registerHandlers() {
        client.on("EMPLOYEE_GET_ALL", this::handleGetAllResponse);
        client.on("EMPLOYEE_GET_ACTIVE", this::handleGetActiveResponse);
        client.on("EMPLOYEE_CREATE", this::handleCreateResponse);
        client.on("EMPLOYEE_UPDATE", this::handleUpdateResponse);
        client.on("EMPLOYEE_DELETE", this::handleDeleteResponse);
        client.on("EMPLOYEE_FILTER", this::handleFilterResponse);
        client.on("EMPLOYEE_SEARCH", this::handleSearchResponse);
    }

    // ─────────────────────────────────────────────
    // RESPONSE HANDLERS
    // ─────────────────────────────────────────────
    private void handleGetAllResponse(DTTP.DTTPArgs args) {
        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> employees = extractList(args.data);
            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), employees);
        } else {
            setError(args.message);
        }
    }

    private void handleGetActiveResponse(DTTP.DTTPArgs args) {
        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> list = extractList(args.data);
            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), list);
        } else {
            setError(args.message);
        }
    }

    private void handleCreateResponse(DTTP.DTTPArgs args) {
        switch (args.status) {
            case SUCCESS -> {
                setMessage("Thêm nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError("Thiếu thông tin bắt buộc!");
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleUpdateResponse(DTTP.DTTPArgs args) {
        switch (args.status) {
            case SUCCESS -> {
                setMessage("Cập nhật nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError("Thiếu ID hoặc dữ liệu không hợp lệ!");
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleDeleteResponse(DTTP.DTTPArgs args) {
        switch (args.status) {
            case SUCCESS -> {
                setMessage("Xóa nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleFilterResponse(DTTP.DTTPArgs args) {
        if (SUCCESS.equals(args.status)) {
            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), extractList(args.data));
        } else {
            setError(args.message);
        }
    }

    private void handleSearchResponse(DTTP.DTTPArgs args) {
        if (SUCCESS.equals(args.status)) {
            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), extractList(args.data));
        } else {
            setError(args.message);
        }
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────
    private List<Map<String, Object>> extractList(Map<String, Object> data) {
        return (List<Map<String, Object>>) data.get(EMPLOYEES_KEY);
    }

    private void setMessage(String msg) {
        store.getAppState().set("EmployeeMessage", msg);
    }

    private void setError(String err) {
        store.getAppState().set("EmployeeError", err);
    }

    private void reloadEmployeeList() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllEmployees();
                } catch (IOException e) {
                    setError("Không thể load lại danh sách: " + e.getMessage());
                }
            }
        }, RELOAD_DELAY);
    }

    private void checkConnection() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client null");
        }
    }

    // ─────────────────────────────────────────────
    // PUBLIC API
    // ─────────────────────────────────────────────
    public void getAllEmployees() throws IOException {
        checkConnection();
        client.send("EMPLOYEE_GET_ALL", null, REQUEST, "");
    }

    public void createEmployee(String username, String password, String name, String phone, String role) throws IOException {
        checkConnection();
        UserModel user = (UserModel) store.getAppState().get("user");

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("name", name);
        data.put("phone", phone);
        data.put("role", role);
        data.put("branchId", user.getBranchId());
        data.put("status", true);

        client.send("EMPLOYEE_CREATE", data, REQUEST, "");
    }

    public void updateEmployee(String id, String name, String phone, String role, Boolean status) throws IOException {
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        if (name != null) {
            data.put("name", name);
        }
        if (phone != null) {
            data.put("phone", phone);
        }
        if (role != null) {
            data.put("role", role);
        }
        if (status != null) {
            data.put("status", status);
        }

        client.send("EMPLOYEE_UPDATE", data, REQUEST, "");
    }

    public void deleteEmployee(String id) throws IOException {
        checkConnection();
        client.send("EMPLOYEE_DELETE", Map.of("id", id), REQUEST, "");
    }

    public void searchEmployees(String keyword) throws IOException {
        checkConnection();
        client.send("EMPLOYEE_SEARCH", Map.of("keyword", keyword), REQUEST, "");
    }

    public void filterEmployees(String name, String role, Integer branchId, Boolean status) throws IOException {
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        if (name != null && !name.isEmpty()) {
            data.put("name", name);
        }
        if (role != null && !role.isEmpty()) {
            data.put("role", role);
        }
        if (branchId != null) {
            data.put("branchId", branchId);
        }
        if (status != null) {
            data.put("status", status);
        }

        client.send("EMPLOYEE_FILTER", data, REQUEST, "");
    }
}
