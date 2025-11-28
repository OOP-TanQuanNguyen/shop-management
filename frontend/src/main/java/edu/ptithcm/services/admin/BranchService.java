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
    private static final String NOT_FOUND = "NOT_FOUND";

    private static final String REQUEST = "REQUEST";
    private static final String BRANCHES_KEY = "branches";

    private static final int RELOAD_DELAY = 80;

    private static final Logger logger = Logger.getLogger(BranchService.class.getName());

    private final DTTP client;
    private final Store store = Store.getInstance();

    public BranchService(DTTP client) {
        this.client = client;
        registerHandlers();
    }

    // ============================================================
    // HANDLERS
    // ============================================================
    private void registerHandlers() {
        client.on("BRANCH_GET_ALL", this::handleGetAll);
        client.on("BRANCH_CREATE", this::handleCreate);
        client.on("BRANCH_UPDATE", this::handleUpdate);
        client.on("BRANCH_DELETE", this::handleDelete);
    }

    @SuppressWarnings("unchecked")
    private void handleGetAll(DTTP.DTTPArgs args) {
        System.out.println("[DEBUG][Service] GET_ALL status = " + args.status);

        if (SUCCESS.equals(args.status)) {
            List<Map<String, Object>> branches
                    = (List<Map<String, Object>>) args.data.get(BRANCHES_KEY);

            // ---- FIX QUAN TRỌNG ---
            store.dispatch(BranchAction.BRANCH_UPDATE_LIST.toString(), branches);
        } else {
            setError(args.message);
        }
    }

    private void handleCreate(DTTP.DTTPArgs args) {
        System.out.println("[DEBUG][Service] CREATE status = " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Thêm chi nhánh thành công!");
                reloadBranchList();
            }
            case INVALID, ERROR ->
                setError(args.message);
        }
    }

    private void handleUpdate(DTTP.DTTPArgs args) {
        System.out.println("[DEBUG][Service] UPDATE status = " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Cập nhật chi nhánh thành công!");
                reloadBranchList();
            }
            case INVALID, NOT_FOUND, ERROR ->
                setError(args.message);
        }
    }

    private void handleDelete(DTTP.DTTPArgs args) {
        System.out.println("[DEBUG][Service] DELETE status = " + args.status);

        switch (args.status) {
            case SUCCESS -> {
                setMessage("Xóa chi nhánh thành công!");
                reloadBranchList();
            }
            case INVALID, NOT_FOUND, ERROR ->
                setError(args.message);
        }
    }

    // ============================================================
    // COMMON HELPERS
    // ============================================================
    private void setMessage(String msg) {
        store.getAppState().set("BranchMessage", msg);
    }

    private void setError(String err) {
        store.getAppState().set("BranchError", err);
        store.dispatch(BranchAction.BRANCH_ERROR.toString(), err);
    }

    private void reloadBranchList() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    getAllBranches();
                } catch (IOException e) {
                    setError("Không thể load lại danh sách: " + e.getMessage());
                }
            }
        }, RELOAD_DELAY);
    }

    private void checkConn() throws IOException {
        if (client == null) {
            throw new IOException("DTTP client null");
        }
    }

    // ============================================================
    // PUBLIC API – CHUẨN NHẤT
    // ============================================================
    public void getAllBranches() throws IOException {
        checkConn();
        System.out.println("[DEBUG][Service] SEND GET_ALL");
        client.send("BRANCH_GET_ALL", null, REQUEST, "Load branch list");
    }

    public void createBranch(Map<String, Object> data) throws IOException {
        checkConn();

        System.out.println("[DEBUG][Service] SEND CREATE: " + data);

        client.send("BRANCH_CREATE", data, REQUEST, "Create branch");
    }

    public void updateBranch(Map<String, Object> data) throws IOException {
        checkConn();

        System.out.println("[DEBUG][Service] SEND UPDATE: " + data);

        client.send("BRANCH_UPDATE", data, REQUEST, "Update branch");
    }

    public void deleteBranch(String id) throws IOException {
        checkConn();

        System.out.println("[DEBUG][Service] SEND DELETE id = " + id);

        Map<String, Object> map = new HashMap<>();
        map.put("branchId", id);  // FE luôn gửi String → BE tự parse bằng RequestUtil

        client.send("BRANCH_DELETE", map, REQUEST, "Delete branch");
    }
}
