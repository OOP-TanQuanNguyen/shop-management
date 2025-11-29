package edu.ptithcm.services.admin;

import edu.ptithcm.app.actions.LoyaltyAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoyaltyService {

    private static final String SUCCESS = "SUCCESS";
    private static final String REQUEST = "REQUEST";

    private final DTTP client;
    private final Store store = Store.getInstance();

    public LoyaltyService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    private void registerHandlers() {
        client.on("LOYALTY_GET_ALL", this::handleGetAll);
        client.on("LOYALTY_GET_BY_CUSTOMER", this::handleGetByCustomer);
        client.on("LOYALTY_CREATE", this::handleCreate);
        client.on("LOYALTY_UPDATE", this::handleUpdate);
        client.on("LOYALTY_DELETE", this::handleDelete);
    }

    @SuppressWarnings("unchecked")
    private void handleGetAll(DTTP.DTTPArgs args) {

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> list
                    = args.data == null ? List.of()
                            : (List<Map<String, Object>>) args.data.get("loyalties");

            store.dispatch(LoyaltyAction.LOYALTY_GET_ALL.toString(), list);

        } else {
            store.dispatch(LoyaltyAction.LOYALTY_ERROR.toString(), args.message);
        }
    }

    private void handleGetByCustomer(DTTP.DTTPArgs args) {

        if (SUCCESS.equals(args.status)) {
            store.dispatch(LoyaltyAction.LOYALTY_GET_BY_CUSTOMER.toString(),
                    args.data);
        } else {
            // khách chưa có loyalty → không được coi là lỗi
            store.dispatch(LoyaltyAction.LOYALTY_GET_BY_CUSTOMER.toString(),
                    null);
        }
    }

    private void handleCreate(DTTP.DTTPArgs args) {
        if (SUCCESS.equals(args.status)) {
            store.dispatch(LoyaltyAction.LOYALTY_CREATE.toString(), args.data);
        } else {
            store.dispatch(LoyaltyAction.LOYALTY_ERROR.toString(), args.message);
        }
    }

    private void handleUpdate(DTTP.DTTPArgs args) {
        if (SUCCESS.equals(args.status)) {
            store.dispatch(LoyaltyAction.LOYALTY_UPDATE.toString(), args.data);
        } else {
            store.dispatch(LoyaltyAction.LOYALTY_ERROR.toString(), args.message);
        }
    }

    private void handleDelete(DTTP.DTTPArgs args) {
        if (SUCCESS.equals(args.status)) {
            store.dispatch(LoyaltyAction.LOYALTY_DELETE.toString(), null);
        } else {
            store.dispatch(LoyaltyAction.LOYALTY_ERROR.toString(), args.message);
        }
    }

    // ============================================================
    // REQUEST METHODS
    // ============================================================
    public void getAllLoyalties() throws IOException {
        ensureClient();

        Map<String, Object> req = new HashMap<>();
        client.send("LOYALTY_GET_ALL", req, REQUEST, "Lấy danh sách loyalty");
    }

    public void getLoyaltyByCustomer(String customerId) throws IOException {
        ensureClient();

        Map<String, Object> req = new HashMap<>();
        req.put("customerId", customerId);

        client.send("LOYALTY_GET_BY_CUSTOMER", req, REQUEST,
                "Lấy loyalty theo khách hàng");
    }

    public void createLoyalty(String customerId) throws IOException {
        ensureClient();

        Map<String, Object> req = new HashMap<>();
        req.put("customerId", customerId);

        client.send("LOYALTY_CREATE", req, REQUEST, "Tạo thẻ thành viên");
    }

    public void updateLoyalty(String customerId, int pointsChange) throws IOException {
        ensureClient();

        Map<String, Object> req = new HashMap<>();
        req.put("customerId", customerId);
        req.put("pointsChange", pointsChange);

        client.send("LOYALTY_UPDATE", req, REQUEST, "Cập nhật điểm");
    }

    public void deleteLoyalty(String customerId) throws IOException {
        ensureClient();

        Map<String, Object> req = new HashMap<>();
        req.put("customerId", customerId);

        client.send("LOYALTY_DELETE", req, REQUEST, "Xóa thẻ thành viên");
    }

    private void ensureClient() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client null");
        }
    }
}
