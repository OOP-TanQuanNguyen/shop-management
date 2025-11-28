package edu.ptithcm.services.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.ptithcm.app.actions.EmployeeAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

public class EmployeeService {

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String REQUEST = "REQUEST";
    private static final String EMPLOYEES_KEY = "employees";
    private static final int RELOAD_DELAY = 12;

    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public EmployeeService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    // =============================================================
    // REGISTER RESPONSE HANDLERS
    // =============================================================
    private void registerHandlers() {
        client.on("EMPLOYEE_GET_ALL", this::handleGetAllResponse);
        client.on("EMPLOYEE_GET_ACTIVE", this::handleGetActiveResponse);
        client.on("EMPLOYEE_CREATE", this::handleCreateResponse);
        client.on("EMPLOYEE_UPDATE", this::handleUpdateResponse);
        client.on("EMPLOYEE_DELETE", this::handleDeleteResponse);
        client.on("EMPLOYEE_FILTER", this::handleFilterResponse);
    }

    // =============================================================
    // RESPONSE HANDLERS
    // =============================================================
    @SuppressWarnings("unchecked")
    private void handleGetAllResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_GET_ALL response = " + args.status);

        if (SUCCESS.equals(args.status) && args.data != null) {
            List<Map<String, Object>> list
                    = (List<Map<String, Object>>) args.data.get(EMPLOYEES_KEY);

            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), list);
        } else {
            setError(args.message);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleGetActiveResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_GET_ACTIVE response = " + args.status);

        if (SUCCESS.equals(args.status) && args.data != null) {
            List<Map<String, Object>> list
                    = (List<Map<String, Object>>) args.data.get(EMPLOYEES_KEY);

            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), list);
        } else {
            setError(args.message);
        }
    }

    private void handleCreateResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_CREATE response = " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Thêm nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError(args.message);
            default ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleUpdateResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_UPDATE response = " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Cập nhật nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError(args.message);
            default ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleDeleteResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_DELETE response = " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Xóa nhân viên thành công!");
                reloadEmployeeList();
            }
            case INVALID ->
                setError(args.message);
            default ->
                setError("Lỗi: " + args.message);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleFilterResponse(DTTP.DTTPArgs args) {
        logger.info("EMPLOYEE_FILTER response = " + args.status);

        if (SUCCESS.equals(args.status) && args.data != null) {
            List<Map<String, Object>> list
                    = (List<Map<String, Object>>) args.data.get(EMPLOYEES_KEY);

            store.dispatch(EmployeeAction.EMPLOYEE_UPDATE_LIST.toString(), list);
        } else {
            setError(args.message);
        }
    }

    // =============================================================
    // HELPERS
    // =============================================================
    private void setMessage(String msg) {
        store.getAppState().set("EmployeeMessage", msg);
    }

    private void setError(String err) {
        logger.warning("Employee Error: " + err);
        store.getAppState().set("EmployeeError", err);
    }

    private void reloadEmployeeList() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllEmployees();
                } catch (IOException e) {
                    setError("Không thể tải lại danh sách: " + e.getMessage());
                }
            }
        }, RELOAD_DELAY);
    }

    private void checkConnection() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client null");
        }
    }

    // =============================================================
    // PUBLIC API (CREATE / UPDATE / DELETE)
    // =============================================================
    public void getAllEmployees() throws IOException {
        logger.info("Sending EMPLOYEE_GET_ALL");
        checkConnection();
        client.send("EMPLOYEE_GET_ALL", null, REQUEST, "Yêu cầu danh sách nhân viên");
    }

    public void getActiveEmployees() throws IOException {
        logger.info("Sending EMPLOYEE_GET_ACTIVE");
        checkConnection();
        client.send("EMPLOYEE_GET_ACTIVE", null, REQUEST, "Yêu cầu nhân viên đang hoạt động");
    }

    public void createEmployee(String username, String password, String name,
            String phone, String role, Integer branchId) throws IOException {

        logger.info("Sending EMPLOYEE_CREATE");
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("name", name);
        data.put("phone", phone);
        data.put("role", role);
        data.put("branchId", branchId);
        data.put("status", true);

        client.send("EMPLOYEE_CREATE", data, REQUEST, "Tạo nhân viên");
    }

    public void updateEmployee(String id, String name, String phone,
            String role, Integer branchId, Boolean status) throws IOException {

        logger.info("Sending EMPLOYEE_UPDATE id=" + id);
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
        if (branchId != null) {
            data.put("branchId", branchId);
        }
        if (status != null) {
            data.put("status", status);
        }

        client.send("EMPLOYEE_UPDATE", data, REQUEST, "Cập nhật nhân viên");
    }

    public void deleteEmployee(String id) throws IOException {
        logger.info("Sending EMPLOYEE_DELETE id=" + id);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);

        client.send("EMPLOYEE_DELETE", data, REQUEST, "Xóa nhân viên");
    }

    // =============================================================
    // FILTER CHUẨN (THEO BE)
    // =============================================================
    public void filterEmployees(Map<String, Object> filters) throws IOException {
        logger.info("Sending EMPLOYEE_FILTER " + filters);
        checkConnection();

        // Lọc bỏ những key BE không hỗ trợ
        filters.remove("keyword");
        filters.remove("name");

        client.send("EMPLOYEE_FILTER", filters, REQUEST, "Lọc nhân viên");
    }

    // =============================================================
    // SEARCH — BE không hỗ trợ keyword nên dùng filter rỗng
    // =============================================================
    public void searchEmployees() throws IOException {
        logger.info("Sending EMPLOYEE_FILTER (search all)");
        checkConnection();

        Map<String, Object> filters = new HashMap<>();
        // không gửi keyword

        client.send("EMPLOYEE_FILTER", filters, REQUEST, "Tìm kiếm nhân viên");
    }
}
