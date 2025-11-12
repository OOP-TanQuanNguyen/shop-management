package edu.ptithcm.services.admin;

import edu.ptithcm.app.actions.AdminAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.UserModel;
import edu.ptithcm.protocols.DTTP;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class EmployeeService {

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String REQUEST = "REQUEST";
    private static final String EMPLOYEES_KEY = "employees";

    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public EmployeeService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    @SuppressWarnings("unchecked")
    private void registerHandlers() {

        // === LẤY DANH SÁCH NHÂN VIÊN ===
        client.on("EMPLOYEE_GET_ALL", args -> {
            logger.info("EMPLOYEE_GET_ALL response: " + args.status);
            if (SUCCESS.equals(args.status)) {
                Map<String, Object> data = args.data;
                List<Map<String, Object>> employees = (List<Map<String, Object>>) data.get(EMPLOYEES_KEY);
                store.dispatch(AdminAction.EMPLOYEE_UPDATE_LIST.toString(), employees);
            } else {
                setError(args.message);
            }
        });

        // === LẤY NHÂN VIÊN ĐANG HOẠT ĐỘNG ===
        client.on("EMPLOYEE_GET_ACTIVE", args -> {
            logger.info("EMPLOYEE_GET_ACTIVE response: " + args.status);
            if (SUCCESS.equals(args.status)) {
                Map<String, Object> data = args.data;
                List<Map<String, Object>> employees = (List<Map<String, Object>>) data.get(EMPLOYEES_KEY);
                store.dispatch(AdminAction.EMPLOYEE_UPDATE_LIST.toString(), employees);
            } else {
                setError(args.message);
            }
        });

        // === THÊM NHÂN VIÊN ===
        client.on("EMPLOYEE_CREATE", args -> {
            logger.info("EMPLOYEE_CREATE response: " + args.status);
            switch (args.status) {
                case SUCCESS:
                    store.dispatch(AdminAction.EMPLOYEE_ADD_SUCCESS.toString(), args.data);
                    setMessage("Thêm nhân viên thành công!");
                    reloadEmployeeList();
                    break;
                case INVALID:
                    setError("Thiếu thông tin bắt buộc!");
                    break;
                case ERROR:
                    setError("Lỗi server: " + args.message);
                    break;
            }
        });

        // === CẬP NHẬT NHÂN VIÊN ===
        client.on("EMPLOYEE_UPDATE", args -> {
            logger.info("EMPLOYEE_UPDATE response: " + args.status);
            switch (args.status) {
                case SUCCESS:
                    store.dispatch(AdminAction.EMPLOYEE_UPDATE_SUCCESS.toString(), null);
                    setMessage("Cập nhật nhân viên thành công!");
                    reloadEmployeeList();
                    break;
                case INVALID:
                    setError("Thiếu ID hoặc dữ liệu cập nhật!");
                    break;
                case ERROR:
                    setError("Lỗi server: " + args.message);
                    break;
            }
        });

        // === XÓA NHÂN VIÊN ===
        client.on("EMPLOYEE_DELETE", args -> {
            logger.info("EMPLOYEE_DELETE response: " + args.status);
            switch (args.status) {
                case SUCCESS:
                    store.dispatch(AdminAction.EMPLOYEE_DELETE_SUCCESS.toString(), null);
                    setMessage("Xóa nhân viên thành công!");
                    reloadEmployeeList();
                    break;
                case INVALID:
                    setError("Thiếu ID nhân viên!");
                    break;
                case ERROR:
                    setError("Lỗi server: " + args.message);
                    break;
            }
        });

        // === LỌC NHÂN VIÊN ===
        client.on("EMPLOYEE_FILTER", args -> {
            logger.info("EMPLOYEE_FILTER response: " + args.status);
            handleListResponse(args, "Lọc dữ liệu");
        });

        // === TÌM KIẾM NHÂN VIÊN ===
        client.on("EMPLOYEE_SEARCH", args -> {
            logger.info("EMPLOYEE_SEARCH response: " + args.status);
            handleListResponse(args, "Tìm kiếm");
        });
    }

    // ============================================================
    // PRIVATE HELPERS
    // ============================================================
    @SuppressWarnings("unchecked")
    private void handleListResponse(DTTP.DTTPArgs args, String actionName) {
        if (SUCCESS.equals(args.status)) {
            Map<String, Object> data = args.data;
            List<Map<String, Object>> employees = (List<Map<String, Object>>) data.get(EMPLOYEES_KEY);
            store.dispatch(AdminAction.EMPLOYEE_UPDATE_LIST.toString(), employees);
            setMessage(actionName + " thành công!");
        } else {
            setError(args.message);
        }
    }

    private void setMessage(String msg) {
        store.getAppState().set("EmployeeMessage", msg);
    }

    private void setError(String err) {
        logger.warning("Employee Error: " + err);
        store.getAppState().set("EmployeeError", err);
        store.dispatch(AdminAction.EMPLOYEE_ERROR.toString(), err);
    }

    private void reloadEmployeeList() {
        try {
            // Delay nhỏ trước khi reload để đảm bảo BE xử lý xong
            Thread.sleep(100);
            getAllEmployees();
        } catch (IOException e) {
            logger.severe("Lỗi khi reload danh sách: " + e.getMessage());
            setError("Không thể tải lại danh sách: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Reload interrupted: " + e.getMessage());
        }
    }

    // ============================================================
    // PUBLIC API — Gửi request lên BE
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

    public void createEmployee(String username, String password, String name, String phone, String role)
            throws IOException {
        logger.info("Sending EMPLOYEE_CREATE request for: " + username);

        Object userObj = store.getAppState().get("user");
        if (!(userObj instanceof UserModel currentUser)) {
            throw new IOException("Chưa đăng nhập!");
        }

        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("name", name);
        data.put("phone", phone);
        data.put("role", role);
        data.put("branchId", currentUser.getBranchId());
        data.put("status", true);

        client.send("EMPLOYEE_CREATE", data, REQUEST, "Tạo nhân viên mới");
    }

    public void updateEmployee(String id, String name, String phone, String role, Boolean status)
            throws IOException {
        logger.info("Sending EMPLOYEE_UPDATE request for ID: " + id);
        checkConnection();

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

        client.send("EMPLOYEE_FILTER", data, REQUEST, "Lọc nhân viên");
    }

    public void searchEmployees(String keyword) throws IOException {
        logger.info("Sending EMPLOYEE_SEARCH request with keyword: " + keyword);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("keyword", keyword);
        client.send("EMPLOYEE_SEARCH", data, REQUEST, "Tìm kiếm nhân viên");
    }

    /**
     * Kiểm tra kết nối trước khi gửi request
     */
    private void checkConnection() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client is null");
        }
        // Thêm kiểm tra nếu DTTP có method isConnected()
        // if (!client.isConnected()) {
        //     throw new IOException("Connection to server is closed");
        // }
    }
}
