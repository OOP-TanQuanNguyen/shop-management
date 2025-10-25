package edu.ptithcm.frontend.services;

import edu.ptithcm.frontend.protocols.DTTP;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class AdminService {

    private final DTTP client;
    private final BiConsumer<String, Map<String, Object>> callback;

    public AdminService(DTTP client, BiConsumer<String, Map<String, Object>> callback) {
        this.client = client;
        this.callback = callback;
        if (client != null) {
            client.on("EMPLOYEE_LIST", data -> callback.accept("EMPLOYEE_LIST", data));
            client.on("PRODUCT_LIST", data -> callback.accept("PRODUCT_LIST", data));
            // ... (Đăng ký các handlers khác)

            // Handlers cho Khách hàng
            client.on("CUSTOMER_LIST", data -> callback.accept("CUSTOMER_LIST", data));
            client.on("CUSTOMER_ACTION_RESPONSE", data -> callback.accept("CUSTOMER_ACTION_RESPONSE", data));
        }
    }

    // ===================== DATA DEMO =====================
    private List<Map<String, Object>> getDemoCustomers() {
        return List.of(
                Map.of("id", 1, "name", "Khách Demo A (Offline)", "phone", "0911xxx", "address", "HCMC"),
                Map.of("id", 2, "name", "Khách Demo B (Offline)", "phone", "0912xxx", "address", "Hà Nội")
        );
    }

    // ⭐️ BỔ SUNG: Demo cho Nhân viên để fix lỗi getEmployees()
    private List<Map<String, Object>> getDemoEmployees() {
        return List.of(
                Map.of("username", "admin", "fullname", "Quản Trị", "role", "ADMIN", "phone", "000", "salary", 1000000),
                Map.of("username", "staff", "fullname", "Nhân Viên", "role", "STAFF", "phone", "111", "salary", 500000)
        );
    }

    // ===================== PHƯƠNG THỨC CHUNG =====================
    // ⭐️ BỔ SUNG: Phương thức getEmployees()
    public void getEmployees() {
        if (client == null) {
            callback.accept("EMPLOYEE_LIST", Map.of("list", getDemoEmployees()));
            return;
        }
        try {
            client.send("GET_EMPLOYEES", new HashMap<>(), "REQUEST", "Yêu cầu danh sách nhân viên");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ===================== PHƯƠNG THỨC KHÁCH HÀNG (Đã có, xác nhận lại) =====================
    // ⭐️ Phương thức getCustomers()
    public void getCustomers() {
        if (client == null) {
            callback.accept("CUSTOMER_LIST", Map.of("list", getDemoCustomers()));
            return;
        }
        try {
            client.send("GET_CUSTOMERS", new HashMap<>(), "REQUEST", "Yêu cầu danh sách khách hàng");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ⭐️ Phương thức addCustomer()
    public void addCustomer(Map<String, Object> customerData) {
        if (client == null) {
            callback.accept("CUSTOMER_ACTION_RESPONSE", Map.of("status", "OK", "message", "[DEMO] Thêm khách hàng thành công (Demo)"));
            return;
        }
        try {
            client.send("ADD_CUSTOMER", customerData, "REQUEST", "Yêu cầu thêm khách hàng");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ⭐️ Phương thức deleteCustomer()
    public void deleteCustomer(int customerId) {
        if (client == null) {
            callback.accept("CUSTOMER_ACTION_RESPONSE", Map.of("status", "OK", "message", "[DEMO] Xóa khách hàng thành công (Demo)"));
            return;
        }
        try {
            client.send("DELETE_CUSTOMER", Map.of("id", customerId), "REQUEST", "Yêu cầu xóa khách hàng");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ... (Các phương thức khác như getProducts() nếu có) ...
}
