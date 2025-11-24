package edu.ptithcm.services.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.ptithcm.app.actions.InventoryAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

public class InventoryService {

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String REQUEST = "REQUEST";
    private static final String INVENTORIES_KEY = "inventories";
    private static final int RELOAD_DELAY = 120;

    private static final Logger logger = Logger.getLogger(InventoryService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public InventoryService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    private void registerHandlers() {
        client.on("INVENTORY_GET_ALL", this::handleGetAllResponse);
        client.on("INVENTORY_CREATE", this::handleCreateResponse);
        client.on("INVENTORY_UPDATE", this::handleUpdateResponse);
        client.on("INVENTORY_DELETE", this::handleDeleteResponse);
        client.on("INVENTORY_GET_BY_BRANCH", this::handleGetByBranchResponse);
    }

    // ─────────────────────────────────────────────
    // RESPONSE HANDLERS
    // ─────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private void handleGetAllResponse(DTTP.DTTPArgs args) {
        logger.info("INVENTORY_GET_ALL response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> inventories = (List<Map<String, Object>>) args.data.get(INVENTORIES_KEY);
            store.dispatch(InventoryAction.INVENTORY_UPDATE_LIST.toString(), inventories);
        } else {
            setError(args.message);
        }
    }

    private void handleCreateResponse(DTTP.DTTPArgs args) {
        logger.info("INVENTORY_CREATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Thêm kho thành công!");
                reloadInventoryList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleUpdateResponse(DTTP.DTTPArgs args) {
        logger.info("INVENTORY_UPDATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Cập nhật kho thành công!");
                reloadInventoryList();
            }
            case INVALID ->
                setError("Dữ liệu không hợp lệ!");
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    private void handleDeleteResponse(DTTP.DTTPArgs args) {
        logger.info("INVENTORY_DELETE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Xóa kho thành công!");
                reloadInventoryList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR ->
                setError("Lỗi: " + args.message);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleGetByBranchResponse(DTTP.DTTPArgs args) {
        logger.info("INVENTORY_GET_BY_BRANCH response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> inventories = (List<Map<String, Object>>) args.data.get(INVENTORIES_KEY);
            store.dispatch(InventoryAction.INVENTORY_UPDATE_LIST.toString(), inventories);
        } else {
            setError(args.message);
        }
    }

    // ─────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────
    private void setMessage(String msg) {
        store.dispatch(InventoryAction.INVENTORY_MESSAGE.toString(), msg);
    }

    private void setError(String err) {
        logger.warning("Inventory Error: " + err);
        store.dispatch(InventoryAction.INVENTORY_ERROR.toString(), err);
    }

    private void reloadInventoryList() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllInventories();
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
    public void getAllInventories() throws IOException {
        logger.info("Sending INVENTORY_GET_ALL request");
        checkConnection();
        client.send("INVENTORY_GET_ALL", null, REQUEST, "Yêu cầu danh sách kho");
    }

    public void createInventory(String branchId, String productId, Integer quantity) throws IOException {
        logger.info("Sending INVENTORY_CREATE request");
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("branchId", branchId);      // ✅ String (BE sẽ convert sang Integer)
        data.put("productId", productId);    // ✅ String (UUID)
        data.put("quantity", quantity);      // ✅ Integer

        logger.info("Create inventory data: " + data);
        client.send("INVENTORY_CREATE", data, REQUEST, "Tạo kho");
    }

    public void updateInventory(Integer id, String branchId, String productId, Integer quantity) throws IOException {
        logger.info("Sending INVENTORY_UPDATE request for id: " + id);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        if (branchId != null) {
            data.put("branchId", branchId);
        }
        if (productId != null) {
            data.put("productId", productId);
        }
        if (quantity != null) {
            data.put("quantity", quantity);
        }

        client.send("INVENTORY_UPDATE", data, REQUEST, "Cập nhật kho");
    }

    public void deleteInventory(Integer id) throws IOException {
        logger.info("Sending INVENTORY_DELETE request for id: " + id);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);

        client.send("INVENTORY_DELETE", data, REQUEST, "Xóa kho");
    }

    public void getInventoriesByBranch(Integer branchId) throws IOException {
        logger.info("Sending INVENTORY_GET_BY_BRANCH request for branchId: " + branchId);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("branchId", branchId);

        client.send("INVENTORY_GET_BY_BRANCH", data, REQUEST, "Lấy kho theo chi nhánh");
    }
}
