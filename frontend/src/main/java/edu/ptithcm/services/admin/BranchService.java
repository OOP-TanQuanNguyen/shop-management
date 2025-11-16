package edu.ptithcm.services.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.ptithcm.app.actions.BranchAction;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.protocols.DTTP;

public class BranchService {

    private static final String SUCCESS = "SUCCESS";
    private static final String INVALID = "INVALID";
    private static final String ERROR = "ERROR";
    private static final String REQUEST = "REQUEST";
    private static final String BRANCHES_KEY = "branches";
    private static final int RELOAD_DELAY_MS = 100;

    private static final Logger logger = Logger.getLogger(BranchService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public BranchService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    private void registerHandlers() {
        client.on("BRANCH_GET_ALL", this::handleGetAllResponse);
        client.on("BRANCH_CREATE", this::handleCreateResponse);
        client.on("BRANCH_UPDATE", this::handleUpdateResponse);
        client.on("BRANCH_DELETE", this::handleDeleteResponse);
    }

    @SuppressWarnings("unchecked")
    private void handleGetAllResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("BRANCH_GET_ALL response: %s", args.status));

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> branches = extractBranchList(args.data);
            store.dispatch(BranchAction.BRANCH_UPDATE_LIST.toString(), branches);
        } else {
            setError(args.message);
        }
    }

    private void handleCreateResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("BRANCH_CREATE response: %s", args.status));

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(BranchAction.BRANCH_ADD_SUCCESS.toString(), args.data);
                setMessage("Thêm chi nhánh thành công!");
                reloadBranchList();
            }
            case INVALID ->
                setError("Thiếu thông tin bắt buộc!");
            case ERROR ->
                setError(String.format("Lỗi: %s", args.message));
        }
    }

    private void handleUpdateResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("BRANCH_UPDATE response: %s", args.status));

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(BranchAction.BRANCH_UPDATE_SUCCESS.toString(), null);
                setMessage("Cập nhật chi nhánh thành công!");
                reloadBranchList();
            }
            case INVALID ->
                setError("Thiếu ID hoặc dữ liệu không hợp lệ!");
            case ERROR ->
                setError(String.format("Lỗi: %s", args.message));
        }
    }

    private void handleDeleteResponse(DTTP.DTTPArgs args) {
        logger.info(String.format("BRANCH_DELETE response: %s", args.status));

        switch (args.status) {
            case SUCCESS -> {
                store.dispatch(BranchAction.BRANCH_DELETE_SUCCESS.toString(), null);
                setMessage("Xóa chi nhánh thành công!");
                reloadBranchList();
            }
            case INVALID ->
                setError(args.message);
            case ERROR ->
                setError(String.format("Lỗi: %s", args.message));
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractBranchList(Map<String, Object> data) {
        return (List<Map<String, Object>>) data.get(BRANCHES_KEY);
    }

    private void setMessage(String msg) {
        store.getAppState().set("BranchMessage", msg);
    }

    private void setError(String err) {
        logger.warning(String.format("Branch Error: %s", err));
        store.getAppState().set("BranchError", err);
        store.dispatch(BranchAction.BRANCH_ERROR.toString(), err);
    }

    private void reloadBranchList() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllBranches();
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

    public void getAllBranches() throws IOException {
        logger.info("Sending BRANCH_GET_ALL request");
        checkConnection();
        client.send("BRANCH_GET_ALL", null, REQUEST, "Yêu cầu danh sách chi nhánh");
    }

    public void createBranch(Map<String, Object> branchData) throws IOException {
        logger.info(String.format("Sending BRANCH_CREATE request for: %s", branchData.get("name")));
        checkConnection();
        client.send("BRANCH_CREATE", branchData, REQUEST, "Tạo chi nhánh mới");
    }

    public void updateBranch(Map<String, Object> branchData) throws IOException {
        logger.info(String.format("Sending BRANCH_UPDATE request for ID: %s", branchData.get("branchId")));
        checkConnection();
        client.send("BRANCH_UPDATE", branchData, REQUEST, "Cập nhật chi nhánh");
    }

    public void deleteBranch(Integer id) throws IOException {
        logger.info(String.format("Sending BRANCH_DELETE request for ID: %s", id));
        checkConnection();

        Map<String, Object> data = new HashMap<>();
        data.put("branchId", id);

        client.send("BRANCH_DELETE", data, REQUEST, "Xóa chi nhánh");
    }
}
