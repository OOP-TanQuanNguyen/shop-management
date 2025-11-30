package edu.ptithcm.services.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.ptithcm.app.actions.CustomerAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

public class CustomerService {

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String REQUEST = "REQUEST";
    private static final String CUSTOMERS_KEY = "customers";

    private static final int RELOAD_DELAY = 120;

    private static final Logger logger = Logger.getLogger(CustomerService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public CustomerService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    // ====================================================
    // REGISTER EVENT HANDLERS
    // ====================================================
    private void registerHandlers() {
        client.on("CUSTOMER_GET_ALL", this::handleGetAllResponse);
        client.on("CUSTOMER_CREATE", this::handleCreateResponse);
        client.on("CUSTOMER_UPDATE", this::handleUpdateResponse);
        client.on("CUSTOMER_DELETE", this::handleDeleteResponse);
        client.on("CUSTOMER_SEARCH", this::handleSearchResponse);
    }

    // ====================================================
    // RESPONSE HANDLERS
    // ====================================================
    @SuppressWarnings("unchecked")
    private void handleGetAllResponse(DTTP.DTTPArgs args) {

        logger.info("CUSTOMER_GET_ALL response: " + args.status);

        if (SUCCESS.equals(args.status)) {

            List<Map<String, Object>> customers
                    = (List<Map<String, Object>>) args.data.get(CUSTOMERS_KEY);

            store.dispatch(CustomerAction.CUSTOMER_UPDATE_LIST.toString(), customers);

        } else {
            setError(args.message);
        }
    }

    private void handleCreateResponse(DTTP.DTTPArgs args) {

        logger.info("CUSTOMER_CREATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                reloadCustomerList();
            }
            case INVALID ->
                setError("Thiếu thông tin bắt buộc!");
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleUpdateResponse(DTTP.DTTPArgs args) {

        logger.info("CUSTOMER_UPDATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Cập nhật khách hàng thành công!");
                reloadCustomerList();
            }
            case INVALID ->
                setError("Thiếu ID hoặc dữ liệu không hợp lệ!");
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleDeleteResponse(DTTP.DTTPArgs args) {

        logger.info("CUSTOMER_DELETE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Xóa khách hàng thành công!");
                reloadCustomerList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleSearchResponse(DTTP.DTTPArgs args) {

        logger.info("CUSTOMER_SEARCH response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> customers
                    = (List<Map<String, Object>>) args.data.get(CUSTOMERS_KEY);

            store.dispatch(CustomerAction.CUSTOMER_UPDATE_LIST.toString(), customers);

        } else {
            setError(args.message);
        }
    }

    // ====================================================
    // HELPERS
    // ====================================================
    private void setMessage(String msg) {
        store.getAppState().set("CustomerMessage", msg);
    }

    private void setError(String err) {
        logger.warning("Customer error: " + err);
        store.getAppState().set("CustomerError", err);
    }

    private void checkConnection() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client is null");
        }
    }

    private void reloadCustomerList() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllCustomers();
                } catch (IOException e) {
                    setError("Không thể tải lại danh sách: " + e.getMessage());
                }
            }
        }, RELOAD_DELAY);
    }

    // ====================================================
    // PUBLIC API
    // ====================================================
    public void getAllCustomers() throws IOException {
        checkConnection();
        client.send("CUSTOMER_GET_ALL", null, REQUEST, "Load customer list");
    }

    public void createCustomer(String name, String phone, Integer point) throws IOException {
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("phone", phone);
        data.put("point", point != null ? point : 0);

        logger.info("Sending CUSTOMER_CREATE: " + data);

        client.send("CUSTOMER_CREATE", data, REQUEST, "Create customer");
    }

    public void updateCustomer(String customerId, String name, String phone, Integer point) throws IOException {
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("customerId", customerId);

        if (name != null) {
            data.put("name", name);
        }
        if (phone != null) {
            data.put("phone", phone);
        }
        if (point != null) {
            data.put("point", point);
        }

        logger.info("Sending CUSTOMER_UPDATE: " + data);

        client.send("CUSTOMER_UPDATE", data, REQUEST, "Update customer");
    }

    public void deleteCustomer(String customerId) throws IOException {
        checkConnection();
        logger.info("Sending CUSTOMER_DELETE id=" + customerId);

        client.send("CUSTOMER_DELETE",
                Map.of("customerId", customerId),
                REQUEST,
                "Delete customer"
        );
    }

    public void searchCustomers(String keyword) throws IOException {
        checkConnection();
        logger.info("Sending CUSTOMER_SEARCH keyword=" + keyword);

        client.send("CUSTOMER_SEARCH",
                Map.of("keyword", keyword),
                REQUEST,
                "Search customer"
        );
    }
}
