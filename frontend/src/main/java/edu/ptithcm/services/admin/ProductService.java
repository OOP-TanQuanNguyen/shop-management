package edu.ptithcm.services.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.ptithcm.app.actions.ProductAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

public class ProductService {

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String REQUEST = "REQUEST";
    private static final String PRODUCTS_KEY = "products";
    private static final int RELOAD_DELAY_MS = 100;

    private static final Logger logger = Logger.getLogger(ProductService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public ProductService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    private void registerHandlers() {
        client.on("PRODUCT_GET_ALL", this::handleGetAllResponse);
        client.on("PRODUCT_CREATE", this::handleCreateResponse);
        client.on("PRODUCT_UPDATE", this::handleUpdateResponse);
        client.on("PRODUCT_DELETE", this::handleDeleteResponse);
        client.on("PRODUCT_SEARCH", this::handleSearchResponse);
    }

    // ============================================================
    // Response Handlers
    // ============================================================
    @SuppressWarnings("unchecked")
    private void handleGetAllResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("PRODUCT_GET_ALL response: %s", args.status));

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> products = extractProductList(args.data);
            store.dispatch(ProductAction.PRODUCT_UPDATE_LIST.toString(), products);
        } else {
            setError(args.message);
        }
    }

    private void handleCreateResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("PRODUCT_CREATE response: %s", args.status));

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(ProductAction.PRODUCT_ADD_SUCCESS.toString(), args.data);
                setMessage("Thêm sản phẩm thành công!");
                reloadProductList();
            }
            case INVALID ->
                setError("Thiếu thông tin bắt buộc hoặc giá không hợp lệ!");
            case ERROR ->
                setError(String.format("Lỗi: %s", args.message));
        }
    }

    private void handleUpdateResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("PRODUCT_UPDATE response: %s", args.status));

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(ProductAction.PRODUCT_UPDATE_SUCCESS.toString(), null);
                setMessage("Cập nhật sản phẩm thành công!");
                reloadProductList();
            }
            case INVALID ->
                setError("Thiếu ID hoặc dữ liệu cập nhật không hợp lệ!");
            case ERROR ->
                setError(String.format("Lỗi: %s", args.message));
        }
    }

    private void handleDeleteResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("PRODUCT_DELETE response: %s", args.status));

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(ProductAction.PRODUCT_DELETE_SUCCESS.toString(), null);
                setMessage("Xóa sản phẩm thành công!");
                reloadProductList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR -> {
                setError(String.format("Lỗi: %s", args.message));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleSearchResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("PRODUCT_SEARCH response: %s", args.status));

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> products = extractProductList(args.data);
            store.dispatch(ProductAction.PRODUCT_UPDATE_LIST.toString(), products);
            setMessage("Tìm kiếm thành công!");
        } else {
            setError(args.message);
        }
    }

    // ============================================================
    // Helper Methods
    // ============================================================
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractProductList(Map<String, Object> data) {
        return (List<Map<String, Object>>) data.get(PRODUCTS_KEY);
    }

    private void setMessage(String msg) {
        store.getAppState().set("ProductMessage", msg);
    }

    private void setError(String err) {
        logger.warning(String.format("Product Error: %s", err));
        store.getAppState().set("ProductError", err);
        store.dispatch(ProductAction.PRODUCT_ERROR.toString(), err);
    }

    private void reloadProductList() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllProducts();
                } catch (IOException e) {
                    logger.severe(String.format("Lỗi khi reload danh sách: %s", e.getMessage()));
                    setError("Không thể tải lại danh sách: " + e.getMessage());
                }
            }
        },
                RELOAD_DELAY_MS
        );
    }

    private void checkConnection() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client is null");
        }
    }

    // ============================================================
    // Public API
    // ============================================================
    public void getAllProducts() throws IOException {
        logger.info("Sending PRODUCT_GET_ALL request");
        checkConnection();
        client.send("PRODUCT_GET_ALL", null, REQUEST, "Yêu cầu danh sách sản phẩm");
    }

    public void createProduct(Map<String, Object> productData) throws IOException {
        logger.info(String.format("Sending PRODUCT_CREATE request for: %s", productData.get("name")));
        checkConnection();
        // ⭐ Chú ý: BE phải chấp nhận các field đã gửi từ dialog (name, categoryId, costPrice, sellPrice, expiryDate, isActive)
        client.send("PRODUCT_CREATE", productData, REQUEST, "Tạo sản phẩm mới");
    }

    public void updateProduct(Map<String, Object> productData) throws IOException {
        logger.info(String.format("Sending PRODUCT_UPDATE request for ID: %s", productData.get("id")));
        checkConnection();
        client.send("PRODUCT_UPDATE", productData, REQUEST, "Cập nhật sản phẩm");
    }

    public void deleteProduct(String id) throws IOException {
        logger.info(String.format("Sending PRODUCT_DELETE request for ID: %s", id));
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("productId", id); // Sử dụng id cho delete

        client.send("PRODUCT_DELETE", data, REQUEST, "Xóa sản phẩm");
    }

    public void searchProducts(String keyword) throws IOException {
        logger.info(String.format("Sending PRODUCT_SEARCH request with keyword: %s", keyword));
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("keyword", keyword);

        client.send("PRODUCT_SEARCH", data, REQUEST, "Tìm kiếm sản phẩm");
    }
}
