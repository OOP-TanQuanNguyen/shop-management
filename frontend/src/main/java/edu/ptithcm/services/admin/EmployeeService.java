package edu.ptithcm.services.admin;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    private static final int RELOAD_DELAY_MS = 100;

    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public EmployeeService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    private void registerHandlers() {
        client.on("EMPLOYEE_GET_ALL", args -> handleGetAllResponse(args));
        client.on("EMPLOYEE_GET_ACTIVE", args -> handleGetActiveResponse(args));
        client.on("EMPLOYEE_CREATE", args -> handleCreateResponse(args));
        client.on("EMPLOYEE_UPDATE", args -> handleUpdateResponse(args));
        client.on("EMPLOYEE_DELETE", args -> handleDeleteResponse(args));
        client.on("EMPLOYEE_FILTER", args -> handleFilterResponse(args));
        client.on("EMPLOYEE_SEARCH", args -> handleSearchResponse(args));
    }

    // ============================================================
    // Response Handlers
    // ============================================================
    private void handleGetAllResponse(DTTP.DTTPArgs args) {
        LocalTime now = LocalTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println("⏱ Now = " + time);
        logger.info("EMPLOYEE_GET_ALL response: " + args.status);
        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> employees = extractEmployeeList(args.data);
            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), employees);
        } else {
            setError(args.message);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleGetActiveResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_GET_ACTIVE response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> employees = extractEmployeeList(args.data);
            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), employees);
        } else {
            setError(args.message);
        }
    }

    private void handleCreateResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_CREATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(EmployeeAction.EMPLOYEE_ADD_SUCCESS.toString(), args.data);
                setMessage("Thêm nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError("Thiếu thông tin bắt buộc!");
            case ERROR ->
                setError("Lỗi :  " + args.message);
        }
    }

    private void handleUpdateResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_UPDATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_SUCCESS.toString(), null);
                setMessage("Cập nhật nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError("Thiếu ID hoặc dữ liệu cập nhật!");
            case ERROR ->
                setError("Lỗi :  " + args.message);
        }
    }

    private void handleDeleteResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_DELETE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(EmployeeAction.EMPLOYEE_DELETE_SUCCESS.toString(), null);
                setMessage("Xóa nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR -> {
                setError("Lỗi :  " + args.message);
            }
        }
    }

    private void handleFilterResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_FILTER response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> employees = extractEmployeeList(args.data);
            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), employees);
            setMessage("Lọc dữ liệu thành công!");
        } else {
            setError(args.message);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleSearchResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_SEARCH response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> employees = extractEmployeeList(args.data);
            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), employees);
            setMessage("Tìm kiếm thành công!");
        } else {
            setError(args.message);
        }
    }

    // ============================================================
    // Helper Methods
    // ============================================================
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractEmployeeList(Map<String, Object> data) {
        return (List<Map<String, Object>>) data.get(EMPLOYEES_KEY);
    }

    private void setMessage(String msg) {
        store.getAppState().set("EmployeeMessage", msg);
    }

    private void setError(String err) {
        logger.warning("Employee Error: " + err);
        store.getAppState().set("EmployeeError", err);
        store.dispatch(EmployeeAction.EMPLOYEE_ERROR.toString(), err);
    }

    private void reloadEmployeeList() {
        try {
            Thread.sleep(RELOAD_DELAY_MS);
            getAllEmployees();
        } catch (IOException e) {
            logger.severe("Lỗi khi reload danh sách: " + e.getMessage());
            setError("Không thể tải lại danh sách: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Reload interrupted: " + e.getMessage());
        }
    }

    private void checkConnection() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client is null");
        }
    }

    private UserModel getCurrentUser() throws IOException {
        Object userObj = store.getAppState().get("user");
        if (!(userObj instanceof UserModel currentUser)) {
            throw new IOException("Chưa đăng nhập!");
        }
        return currentUser;
    }

    private Map<String, Object> buildEmployeeData(String username, String password, String name,
            String phone, String role, UserModel currentUser) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("name", name);
        data.put("phone", phone);
        data.put("role", role);
        data.put("branchId", currentUser.getBranchId());
        data.put("status", true);
        return data;
    }

    private Map<String, Object> buildUpdateData(String id, String name, String phone,
            String role, Boolean status) {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);

        if (name != null && !name.trim().isEmpty()) {
            data.put("name", name);
        }
        if (phone != null && !phone.trim().isEmpty()) {
            data.put("phone", phone);
        }
        if (role != null && !role.trim().isEmpty()) {
            data.put("role", role);
        }
        if (status != null) {
            data.put("status", status);
        }

        return data;
    }

    private Map<String, Object> buildFilterData(String name, String role,
            Integer branchId, Boolean status) {
        Map<String, Object> data = new HashMap<>();

        if (name != null && !name.trim().isEmpty()) {
            data.put("name", name);
        }
        if (role != null && !role.trim().isEmpty()) {
            data.put("role", role);
        }
        if (branchId != null) {
            data.put("branchId", branchId);
        }
        if (status != null) {
            data.put("status", status);
        }

        return data;
    }

    // ============================================================
    // Public API
    // ============================================================
    public void getAllEmployees() throws IOException {
        logger.info("Sending EMPLOYEE_GET_ALL request");
        checkConnection();
        client.send("EMPLOYEE_GET_ALL", null, REQUEST, "Yêu cầu danh sách nhân viên");
    }

    public void getActiveEmployees() throws IOException {
        logger.info("Sending EMPLOYEE_GET_ACTIVE request");
        checkConnection();
        client.send("EMPLOYEE_GET_ACTIVE", null, REQUEST, "Yêu cầu nhân viên đang hoạt động");
    }

    public void createEmployee(String username, String password, String name,
            String phone, String role) throws IOException {
        logger.info("Sending EMPLOYEE_CREATE request for: " + username);
        checkConnection();

        UserModel currentUser = getCurrentUser();
        Map<String, Object> data = buildEmployeeData(username, password, name, phone, role, currentUser);

        client.send("EMPLOYEE_CREATE", data, REQUEST, "Tạo nhân viên mới");
    }

    public void updateEmployee(String id, String name, String phone,
            String role, Boolean status) throws IOException {
        logger.info("Sending EMPLOYEE_UPDATE request for ID: " + id);
        checkConnection();

        Map<String, Object> data = buildUpdateData(id, name, phone, role, status);
        client.send("EMPLOYEE_UPDATE", data, REQUEST, "Cập nhật nhân viên");
    }

    public void deleteEmployee(String id) throws IOException {
        logger.info("Sending EMPLOYEE_DELETE request for ID: " + id);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);

        client.send("EMPLOYEE_DELETE", data, REQUEST, "Xóa nhân viên");
    }

    public void filterEmployees(String name, String role, Integer branchId, Boolean status)
            throws IOException {
        logger.info("Sending EMPLOYEE_FILTER request");
        checkConnection();

        Map<String, Object> data = buildFilterData(name, role, branchId, status);
        client.send("EMPLOYEE_FILTER", data, REQUEST, "Lọc nhân viên");
    }

    public void searchEmployees(String keyword) throws IOException {
        logger.info("Sending EMPLOYEE_SEARCH request with keyword: " + keyword);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("keyword", keyword);

        client.send("EMPLOYEE_SEARCH", data, REQUEST, "Tìm kiếm nhân viên");
    }
}
