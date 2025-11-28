package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.BranchInfo;
import edu.ptithcm.services.admin.BranchService;
import edu.ptithcm.views.admin.BranchPanel;
import edu.ptithcm.views.admin.branch_dialogs.BranchAddDialog;
import edu.ptithcm.views.admin.branch_dialogs.BranchDeleteConfirmDialog;
import edu.ptithcm.views.admin.branch_dialogs.BranchEditDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BranchController {

    private final BranchPanel view;
    private final BranchService service;
    private final Store store = Store.getInstance();

    private List<BranchInfo> currentBranches;
    private boolean isShowingMessage = false;

    public BranchController(BranchPanel view, BranchService service) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);
        loadBranches();
    }

    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());

        // ====== FIX QUAN TRỌNG: RESET SEARCH KHI RELOAD ======
        view.getBtnReload().addActionListener(e -> {
            view.getTxtSearch().setText(""); // reset text search
            loadBranches();
        });

        // ========= FILTER =========
        view.getBtnFilter().addActionListener(e -> applyFilter());
    }

    private void loadBranches() {
        try {
            service.getAllBranches();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tải danh sách chi nhánh: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // FILTER (Local filtering)
    // ============================================================
    private void applyFilter() {

        if (currentBranches == null) {
            return;
        }

        String keyword = view.getTxtSearch().getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            view.updateTable(currentBranches);
            return;
        }

        List<BranchInfo> filtered = currentBranches.stream()
                .filter(b -> b.getName() != null
                && b.getName().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        view.updateTable(filtered);
    }

    // ============================================================
    // ADD
    // ============================================================
    private void handleAdd() {
        BranchAddDialog dialog = new BranchAddDialog(getParentFrame());
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Map<String, Object> data = dialog.toMap();
            createBranch(data);
        }

        dialog.dispose();
    }

    private void createBranch(Map<String, Object> data) {
        try {
            service.createBranch(data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm chi nhánh: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // EDIT
    // ============================================================
    private void handleEdit() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn chi nhánh để sửa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BranchInfo branch = currentBranches.get(row);

        BranchEditDialog dialog = new BranchEditDialog(
                getParentFrame(),
                branch.getId(),
                branch.getName(),
                branch.getPhone(),
                branch.getAddress()
        );

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Map<String, Object> data = dialog.toMap();
            data.put("branchId", branch.getId());
            updateBranch(data);
        }

        dialog.dispose();
    }

    private void updateBranch(Map<String, Object> data) {
        try {
            service.updateBranch(data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi cập nhật chi nhánh: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // DELETE
    // ============================================================
    private void handleDelete() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn chi nhánh để xóa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BranchInfo branch = currentBranches.get(row);

        BranchDeleteConfirmDialog dialog
                = new BranchDeleteConfirmDialog(getParentFrame(), branch.getName());

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            deleteBranch(branch.getId());
        }

        dialog.dispose();
    }

    private void deleteBranch(String id) {
        try {
            service.deleteBranch(id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi xóa chi nhánh: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // STATE UPDATE
    // ============================================================
    private void onStateChanged(AppState state) {
        updateBranchList(state);
        showMessages(state);
    }

    private void updateBranchList(AppState state) {
        Object obj = state.get("Branches");

        if (obj instanceof List<?> list) {

            @SuppressWarnings("unchecked")
            List<BranchInfo> branches = (List<BranchInfo>) list;

            this.currentBranches = branches;

            // Nếu đang search → giữ filter
            if (view.getTxtSearch().getText().trim().isEmpty()) {
                view.updateTable(branches);
            } else {
                applyFilter();
            }
        }
    }

    private void showMessages(AppState state) {
        if (isShowingMessage) {
            return;
        }

        Object msg = state.get("BranchMessage");
        if (msg instanceof String m && !m.isEmpty()) {
            isShowingMessage = true;
            state.set("BranchMessage", "");
            JOptionPane.showMessageDialog(view, m, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            isShowingMessage = false;
        }

        Object err = state.get("BranchError");
        if (err instanceof String e && !e.isEmpty()) {
            isShowingMessage = true;
            state.set("BranchError", "");
            JOptionPane.showMessageDialog(view, e, "Lỗi", JOptionPane.ERROR_MESSAGE);
            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
