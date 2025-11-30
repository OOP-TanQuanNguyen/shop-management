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

        view.getBtnReload().addActionListener(e -> {
            view.getTxtSearch().setText("");
            loadBranches();
        });

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

            String name = ((String) data.get("name")).trim().toLowerCase();

            // ================================
            // CHECK TÊN KHÔNG ĐƯỢC TRÙNG
            // ================================
            if (isDuplicateName(name)) {
                JOptionPane.showMessageDialog(view,
                        "Tên chi nhánh đã tồn tại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                dialog.dispose();
                return;
            }

            // ================================
            // CHECK SĐT KHÔNG ĐƯỢC TRÙNG
            // ================================
            String phone = (String) data.get("phone");
            if (phone != null && !phone.isBlank()) {
                String p = phone.replaceAll("[^0-9]", "");
                if (isDuplicatePhone(p)) {
                    JOptionPane.showMessageDialog(view,
                            "Số điện thoại đã tồn tại cho chi nhánh khác!",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    dialog.dispose();
                    return;
                }
            }

            createBranch(data);
        }

        dialog.dispose();
    }

    // TÊN KHÔNG ĐƯỢC TRÙNG (ADD)
    private boolean isDuplicateName(String name) {
        if (currentBranches == null || name == null) {
            return false;
        }

        name = name.toLowerCase().trim();

        for (BranchInfo b : currentBranches) {
            if (b.getName() != null
                    && b.getName().trim().toLowerCase().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // SĐT KHÔNG ĐƯỢC TRÙNG (ADD)
    private boolean isDuplicatePhone(String phone) {
        if (currentBranches == null || phone == null) {
            return false;
        }

        for (BranchInfo b : currentBranches) {
            if (b.getPhone() != null) {
                String existing = b.getPhone().replaceAll("[^0-9]", "");
                if (existing.equals(phone)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isDuplicatePhoneForUpdate(String phone, String branchId) {
        if (currentBranches == null || phone == null) {
            return false;
        }

        for (BranchInfo b : currentBranches) {
            if (b.getId().equals(branchId)) {
                continue;
            }
            if (b.getPhone() != null) {
                String existing = b.getPhone().replaceAll("[^0-9]", "");
                if (existing.equals(phone)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDuplicateNameForUpdate(String name, String branchId) {
        if (currentBranches == null || name == null) {
            return false;
        }

        name = name.toLowerCase().trim();

        for (BranchInfo b : currentBranches) {
            if (b.getId().equals(branchId)) {
                continue;
            }
            if (b.getName() != null
                    && b.getName().trim().toLowerCase().equals(name)) {
                return true;
            }
        }
        return false;
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

            String name = ((String) data.get("name")).trim().toLowerCase();

            // ================================
            // CHECK TÊN KHÔNG ĐƯỢC TRÙNG (UPDATE)
            // ================================
            if (isDuplicateNameForUpdate(name, branch.getId())) {
                JOptionPane.showMessageDialog(view,
                        "Tên chi nhánh đã tồn tại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                dialog.dispose();
                return;
            }

            // ================================
            // CHECK SĐT KHÔNG ĐƯỢC TRÙNG
            // ================================
            String phone = ((String) data.get("phone")).replaceAll("[^0-9]", "");

            if (!phone.isBlank()
                    && isDuplicatePhoneForUpdate(phone, branch.getId())) {
                JOptionPane.showMessageDialog(view,
                        "Số điện thoại đã tồn tại cho chi nhánh khác!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                dialog.dispose();
                return;
            }

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
            JOptionPane.showMessageDialog(view, m,
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            isShowingMessage = false;
        }

        Object err = state.get("BranchError");
        if (err instanceof String e && !e.isEmpty()) {
            isShowingMessage = true;
            state.set("BranchError", "");
            JOptionPane.showMessageDialog(view, e,
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
