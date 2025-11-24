package edu.ptithcm.services.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.ptithcm.app.actions.CategoryAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

public class CategoryService {

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String NOT_FOUND = "NOT_FOUND";
    private static final String UNAUTHORIZED = "UNAUTHORIZED";

    private static final String REQUEST = "REQUEST";
    private static final String CATEGORIES_KEY = "categories";

    // Tăng delay lên 150ms cho chắc chắn server xử lý xong
    private static final int RELOAD_DELAY = 150;

    private static final Logger logger = Logger.getLogger(CategoryService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public CategoryService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    private void registerHandlers() {
        client.on("CATEGORY_GET_ALL", this::handleGetAllResponse);
        client.on("CATEGORY_CREATE", this::handleCreateResponse);
        client.on("CATEGORY_UPDATE", this::handleUpdateResponse);
        client.on("CATEGORY_DELETE", this::handleDeleteResponse);
        client.on("CATEGORY_GET_BY_ID", this::handleGetByIdResponse);
    }

    // ==========================================================
    // RESPONSE HANDLERS
    // ==========================================================
    @SuppressWarnings("unchecked")
    private void handleGetAllResponse(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_GET_ALL response: " + args.status);

        if (SUCCESS.equals(args.status)) {

            if (args.data == null || args.data.get(CATEGORIES_KEY) == null) {
                setError("Dữ liệu trả về từ server không hợp lệ.");
                return;
            }

            List<Map<String, Object>> categories
                    = (List<Map<String, Object>>) args.data.get(CATEGORIES_KEY);

            store.dispatch(CategoryAction.CATEGORY_UPDATE_LIST.toString(), categories);

        } else if (UNAUTHORIZED.equals(args.status)) {
            setError(nonEmpty(args.message,
                    "Bạn không có quyền xem danh sách danh mục."));
        } else {
            // INVALID, ERROR, NOT_FOUND hoặc status khác
            setError(nonEmpty(args.message,
                    "Không thể tải danh sách danh mục (status=" + args.status + ")."));
        }
    }

    private void handleCreateResponse(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_CREATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                // Dispatch để reducer set message chuẩn
                store.dispatch(CategoryAction.CATEGORY_ADD_SUCCESS.toString(), null);
                // Ưu tiên message từ server
                setMessage(nonEmpty(args.message, "Thêm danh mục thành công!"));
                reloadCategoryList();
            }
            case INVALID ->
                setError(nonEmpty(args.message, "Thiếu thông tin bắt buộc!"));
            case ERROR ->
                setError(nonEmpty(args.message, "Lỗi tạo danh mục!"));
            case NOT_FOUND ->
                setError(nonEmpty(args.message, "Không tìm thấy dữ liệu để tạo/cập nhật!"));
            case UNAUTHORIZED ->
                setError(nonEmpty(args.message, "Bạn không có quyền thêm danh mục!"));
            default ->
                setError("Phản hồi không xác định từ server (status=" + args.status + ")");
        }
    }

    private void handleUpdateResponse(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_UPDATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(CategoryAction.CATEGORY_UPDATE_SUCCESS.toString(), null);
                setMessage(nonEmpty(args.message, "Cập nhật danh mục thành công!"));
                reloadCategoryList();
            }
            case INVALID ->
                setError(nonEmpty(args.message, "Thiếu ID hoặc dữ liệu không hợp lệ!"));
            case NOT_FOUND ->
                setError(nonEmpty(args.message, "Không tìm thấy danh mục để cập nhật!"));
            case UNAUTHORIZED ->
                setError(nonEmpty(args.message, "Bạn không có quyền cập nhật danh mục!"));
            case ERROR ->
                setError(nonEmpty(args.message, "Lỗi cập nhật danh mục!"));
            default ->
                setError("Phản hồi không xác định từ server (status=" + args.status + ")");
        }
    }

    private void handleDeleteResponse(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_DELETE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(CategoryAction.CATEGORY_DELETE_SUCCESS.toString(), null);
                setMessage(nonEmpty(args.message, "Xóa danh mục thành công!"));
                reloadCategoryList();
            }
            case INVALID ->
                setError(nonEmpty(args.message, "Thiếu ID danh mục cần xóa!"));
            case NOT_FOUND ->
                setError(nonEmpty(args.message, "Không tồn tại danh mục cần xóa!"));
            case UNAUTHORIZED ->
                setError(nonEmpty(args.message, "Bạn không có quyền xóa danh mục!"));
            case ERROR ->
                setError(nonEmpty(args.message, "Lỗi xóa danh mục!"));
            default ->
                setError("Phản hồi không xác định từ server (status=" + args.status + ")");
        }
    }

    private void handleGetByIdResponse(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_GET_BY_ID response: " + args.status);

        if (SUCCESS.equals(args.status)) {
            // Đưa qua reducer, không set state trực tiếp
            store.dispatch(
                    CategoryAction.CATEGORY_GET_BY_ID_SUCCESS.toString(),
                    args.data
            );
        } else if (NOT_FOUND.equals(args.status)) {
            setError(nonEmpty(args.message, "Không tìm thấy danh mục cần lấy thông tin!"));
        } else if (UNAUTHORIZED.equals(args.status)) {
            setError(nonEmpty(args.message, "Bạn không có quyền truy cập danh mục này!"));
        } else {
            setError(nonEmpty(args.message,
                    "Lỗi khi lấy thông tin danh mục (status=" + args.status + ")"));
        }
    }

    // ==========================================================
    // HELPERS
    // ==========================================================
    private void setMessage(String msg) {
        store.getAppState().set("CategoryMessage", msg);
    }

    private void setError(String err) {
        logger.warning("Category Error: " + err);
        // Chuẩn Redux: dispatch action, để reducer xử lý
        store.dispatch(CategoryAction.CATEGORY_ERROR.toString(), err);
    }

    private void reloadCategoryList() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllCategories();
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

    private String nonEmpty(String msg, String fallback) {
        if (msg == null || msg.isBlank()) {
            return fallback;
        }
        return msg;
    }

    // ==========================================================
    // PUBLIC API
    // ==========================================================
    public void getAllCategories() throws IOException {
        logger.info("Sending CATEGORY_GET_ALL request");
        checkConnection();
        client.send("CATEGORY_GET_ALL", null, REQUEST, "Yêu cầu danh sách danh mục");
    }

    public void createCategory(String name) throws IOException {
        logger.info("Sending CATEGORY_CREATE request");
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);

        client.send("CATEGORY_CREATE", data, REQUEST, "Tạo danh mục");
    }

    public void updateCategory(String id, String name) throws IOException {
        logger.info("Sending CATEGORY_UPDATE request for id: " + id);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("categoryId", id);
        data.put("name", name);

        client.send("CATEGORY_UPDATE", data, REQUEST, "Cập nhật danh mục");
    }

    public void deleteCategory(String id) throws IOException {
        logger.info("Sending CATEGORY_DELETE request for id: " + id);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("categoryId", id);

        client.send("CATEGORY_DELETE", data, REQUEST, "Xóa danh mục");
    }

    public void getCategoryById(String id) throws IOException {
        logger.info("Sending CATEGORY_GET_BY_ID request for id: " + id);
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("categoryId", id);

        client.send("CATEGORY_GET_BY_ID", data, REQUEST, "Lấy thông tin danh mục");
    }
}
