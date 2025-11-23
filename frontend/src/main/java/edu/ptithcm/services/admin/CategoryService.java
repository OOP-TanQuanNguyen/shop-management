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
    private static final String REQUEST = "REQUEST";

    private static final String CATEGORIES_KEY = "categories";

    private static final Logger logger = Logger.getLogger(CategoryService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public CategoryService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    // ============================================================
    // Register handlers
    // ============================================================
    private void registerHandlers() {

        client.on("CATEGORY_GET_ALL", this::handleGetAll);

        client.on("CATEGORY_CREATE", this::handleCreate);

        client.on("CATEGORY_UPDATE", this::handleUpdate);

        client.on("CATEGORY_DELETE", this::handleDelete);

        client.on("CATEGORY_GET_BY_ID", this::handleGetById);
    }

    // ============================================================
    // Handlers
    // ============================================================
    @SuppressWarnings("unchecked")
    private void handleGetAll(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_GET_ALL response: " + args.status);

        if (SUCCESS.equals(args.status)) {

            List<Map<String, Object>> list
                    = (List<Map<String, Object>>) args.data.get(CATEGORIES_KEY);

            store.dispatch(CategoryAction.CATEGORY_UPDATE_LIST.toString(), list);

        } else {
            setError(args.message);
        }
    }

    private void handleCreate(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_CREATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(CategoryAction.CATEGORY_ADD_SUCCESS.toString(), null);
                setMessage("Thêm danh mục thành công!");
                reloadList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR ->
                setError(args.message);
        }
    }

    private void handleUpdate(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_UPDATE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(CategoryAction.CATEGORY_UPDATE_SUCCESS.toString(), null);
                setMessage("Cập nhật danh mục thành công!");
                reloadList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR ->
                setError(args.message);
        }
    }

    private void handleDelete(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_DELETE response: " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(CategoryAction.CATEGORY_DELETE_SUCCESS.toString(), null);
                setMessage("Xóa danh mục thành công!");
                reloadList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR ->
                setError(args.message);
        }
    }

    private void handleGetById(DTTP.DTTPArgs args) {
        logger.info("CATEGORY_GET_BY_ID response: " + args.status);

        if (!SUCCESS.equals(args.status)) {
            setError(args.message);
        } else {
            // optional: nếu FE cần dùng, thêm action khác
        }
    }

    // ============================================================
    // Helpers
    // ============================================================
    private void reloadList() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllCategories();
                } catch (IOException e) {
                    setError("Không thể tải lại danh sách: " + e.getMessage());
                }
            }
        }, 100);
    }

    private void setMessage(String msg) {
        store.getAppState().set("CategoryMessage", msg);
    }

    private void setError(String msg) {
        store.getAppState().set("CategoryError", msg);
        store.dispatch(CategoryAction.CATEGORY_ERROR.toString(), msg);
    }

    private void checkConnection() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client is null");
        }
    }

    // ============================================================
    // API
    // ============================================================
    public void getAllCategories() throws IOException {
        checkConnection();
        client.send("CATEGORY_GET_ALL", null, REQUEST, "Lấy all category");
    }

    public void createCategory(Map<String, Object> data) throws IOException {
        checkConnection();
        client.send("CATEGORY_CREATE", data, REQUEST, "Tạo category");
    }

    public void updateCategory(Map<String, Object> data) throws IOException {
        checkConnection();
        client.send("CATEGORY_UPDATE", data, REQUEST, "Cập nhật category");
    }

    public void deleteCategory(String id) throws IOException {
        checkConnection();
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", id);

        client.send("CATEGORY_DELETE", map, REQUEST, "Xóa category");
    }

    public void getCategoryById(String id) throws IOException {
        checkConnection();
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", id);

        client.send("CATEGORY_GET_BY_ID", map, REQUEST, "Lấy category theo ID");
    }
}
