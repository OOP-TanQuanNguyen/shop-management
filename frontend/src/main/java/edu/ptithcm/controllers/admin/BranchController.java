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
import java.util.logging.Logger;

public class BranchController {

    private static final Logger logger = Logger.getLogger(BranchController.class.getName());

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
        view.getBtnReload().addActionListener(e -> loadBranches());
    }

    private void loadBranches() {
        try {
            service.getAllBranches();
        } catch (IOException e) {
            logger.severe(String.format("Failed to load branches: %s", e.getMessage()));
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(view, "Không thể tải danh sách chi nhánh: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void handleAdd() {
        SwingUtilities.invokeLater(() -> {
            BranchAddDialog dialog = new BranchAddDialog(getParentFrame());
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Map<String, Object> data = dialog.toMap();
                logger.info("Branch data to create: " + data);
                createBranch(data);
            }

            dialog.dispose();
        });
    }

    private void handleEdit() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn chi nhánh để sửa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentBranches == null || row >= currentBranches.size()) {
            JOptionPane.showMessageDialog(view, "Không thể lấy thông tin chi nhánh!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                BranchInfo branch = currentBranches.get(row);

                logger.info(String.format("Editing branch: ID=%s, Name=%s", branch.getId(), branch.getName()));

                BranchEditDialog dialog = new BranchEditDialog(
                        getParentFrame(),
                        branch.getId(),
                        branch.getName(),
                        branch.getPhone(),
                        branch.getAddress(),
                        branch.getIsActive()
                );
                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    Map<String, Object> data = dialog.toMap();
                    data.put("branchId", branch.getId());
                    logger.info("Branch data to update: " + data);
                    updateBranch(data);
                }

                dialog.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi khi đọc thông tin chi nhánh: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                logger.severe(String.format("Edit error: %s", e.getMessage()));
            }
        });
    }

    private void handleDelete() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn chi nhánh để xóa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentBranches == null || row >= currentBranches.size()) {
            JOptionPane.showMessageDialog(view, "Không thể lấy thông tin chi nhánh!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                BranchInfo branch = currentBranches.get(row);
                Integer id = branch.getId();
                String name = branch.getName();

                logger.info(String.format("Attempting to delete branch: ID=%s, Name=%s", id, name));

                BranchDeleteConfirmDialog dialog = new BranchDeleteConfirmDialog(getParentFrame(), name);
                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    logger.info("Delete confirmed for ID: " + id);
                    deleteBranch(id);
                }

                dialog.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi khi xóa chi nhánh: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                logger.severe(String.format("Delete error: %s", e.getMessage()));
            }
        });
    }

    private void createBranch(Map<String, Object> branchData) {
        try {
            service.createBranch(branchData);
            logger.info(String.format("Create branch request sent for: %s", branchData.get("name")));
        } catch (IOException e) {
            logger.severe(String.format("Failed to send create request: %s", e.getMessage()));
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(view, "Lỗi khi thêm chi nhánh: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void updateBranch(Map<String, Object> branchData) {
        try {
            service.updateBranch(branchData);
            Integer id = branchData.containsKey("branchId") ? (Integer) branchData.get("branchId") : null;
            logger.info(String.format("Update branch request sent for ID: %s", id));
        } catch (IOException e) {
            logger.severe(String.format("Failed to send update request: %s", e.getMessage()));
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật chi nhánh: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void deleteBranch(Integer id) {
        try {
            service.deleteBranch(id);
            logger.info(String.format("Delete branch request sent for ID: %s", id));
        } catch (IOException e) {
            logger.severe(String.format("Failed to send delete request: %s", e.getMessage()));
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(view, "Lỗi khi xóa chi nhánh: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateBranchList(state);
            showMessagesFromState(state);
        });
    }

    private void updateBranchList(AppState state) {
        Object branchListObj = state.get("Branches");
        if (branchListObj instanceof List<?> list) {
            @SuppressWarnings("unchecked")
            List<BranchInfo> branches = (List<BranchInfo>) list;
            this.currentBranches = branches;
            view.updateTable(branches);
            logger.info(String.format("Branch list updated: %d items", branches.size()));
        }
    }

    private void showMessagesFromState(AppState state) {
        if (isShowingMessage) {
            return;
        }

        Object msg = state.get("BranchMessage");
        if (msg instanceof String message && !message.isEmpty()) {
            isShowingMessage = true;
            state.set("BranchMessage", "");

            if (message.contains("thành công")) {
                JOptionPane.showMessageDialog(view, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

            isShowingMessage = false;
        }

        Object err = state.get("BranchError");
        if (err instanceof String error && !error.isEmpty()) {
            isShowingMessage = true;
            state.set("BranchError", "");

            JOptionPane.showMessageDialog(view, error, "Lỗi", JOptionPane.ERROR_MESSAGE);

            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
