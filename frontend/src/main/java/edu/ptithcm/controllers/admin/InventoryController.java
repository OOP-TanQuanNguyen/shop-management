package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;

import edu.ptithcm.models.BranchInfo;
import edu.ptithcm.models.ProductInfo;
import edu.ptithcm.models.InventoryModel;

import edu.ptithcm.services.admin.InventoryService;

import edu.ptithcm.views.admin.InventoryPanel;
import edu.ptithcm.views.admin.inventory_dialogs.InventoryAddDialog;
import edu.ptithcm.views.admin.inventory_dialogs.InventoryEditDialog;
import edu.ptithcm.views.admin.inventory_dialogs.InventoryDeleteConfirmDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryController {

    private final InventoryPanel view;
    private final InventoryService service;
    private final Store store = Store.getInstance();

    private List<InventoryModel> inventories;
    private List<BranchInfo> branches;
    private List<ProductInfo> products;

    private boolean isShowingMessage = false;

    public InventoryController(InventoryPanel view, InventoryService service) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);

        initialLoad();
    }

    // ---------------------------------------
    // INITIAL LOAD
    // ---------------------------------------
    private void initialLoad() {
        try {
            service.getAllInventories();
        } catch (IOException e) {
            view.showMessage("⚠ Không tải được dữ liệu kho: " + e.getMessage());
        }
    }

    // ---------------------------------------
    // REGISTER EVENTS
    // ---------------------------------------
    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> reloadFullData());

        // Filter – LOCAL only
        view.getBtnFilter().addActionListener(e -> applyFilter());
    }

    // ---------------------------------------
    // RELOAD (RESET FILTER)
    // ---------------------------------------
    private void reloadFullData() {
        // Reset filter UI về mặc định
        view.getCbBranchFilter().setSelectedIndex(0);
        view.getCbProductFilter().setSelectedIndex(0);

        try {
            service.getAllInventories();
        } catch (IOException e) {
            view.showMessage("⚠ Không thể tải lại: " + e.getMessage());
        }
    }

    // ---------------------------------------
    // FILTERING (LOCAL)
    // ---------------------------------------
    private void applyFilter() {

        if (inventories == null) {
            return;
        }

        String branchSel = (String) view.getCbBranchFilter().getSelectedItem();
        String productSel = (String) view.getCbProductFilter().getSelectedItem();

        List<InventoryModel> filtered = inventories.stream()
                .filter(i
                        -> (branchSel.equals("Tất cả chi nhánh")
                || i.getBranchName().equalsIgnoreCase(branchSel))
                && (productSel.equals("Tất cả sản phẩm")
                || i.getProductName().equalsIgnoreCase(productSel))
                )
                .collect(Collectors.toList());

        view.updateTable(filtered);
    }

    // ---------------------------------------
    // ADD
    // ---------------------------------------
    private void handleAdd() {

        if (branches == null || products == null || branches.isEmpty() || products.isEmpty()) {
            view.showMessage("⚠ Danh sách chi nhánh hoặc sản phẩm chưa tải!");
            return;
        }

        InventoryAddDialog dlg = new InventoryAddDialog(getFrame(), branches, products);
        dlg.showDialog();

        if (!dlg.isConfirmed()) {
            return;
        }

        try {
            service.createInventory(
                    dlg.getBranchId(),
                    dlg.getProductId(),
                    dlg.getQuantity()
            );
        } catch (IOException ex) {
            view.showMessage("⚠ Lỗi tạo kho: " + ex.getMessage());
        }
    }

    // ---------------------------------------
    // EDIT
    // ---------------------------------------
    private void handleEdit() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            view.showMessage("⚠ Vui lòng chọn dòng để sửa!");
            return;
        }

        InventoryModel item = getInventoryFromTable(row);
        if (item == null) {
            view.showMessage("⚠ Không tìm thấy dữ liệu dòng này!");
            return;
        }

        InventoryEditDialog dlg = new InventoryEditDialog(
                getFrame(),
                item.getId(),
                item.getBranchName(),
                item.getProductName(),
                item.getQuantity()
        );

        dlg.showDialog();
        if (!dlg.isConfirmed()) {
            return;
        }

        try {
            service.updateInventory(
                    item.getId(),
                    item.getBranchId(),
                    item.getProductId(),
                    dlg.getQuantity()
            );
        } catch (IOException ex) {
            view.showMessage("⚠ Lỗi cập nhật: " + ex.getMessage());
        }
    }

    private InventoryModel getInventoryFromTable(int row) {
        String branchName = (String) view.getTable().getValueAt(row, 0);
        String productName = (String) view.getTable().getValueAt(row, 1);

        return inventories.stream()
                .filter(i -> i.getBranchName().equals(branchName)
                && i.getProductName().equals(productName))
                .findFirst()
                .orElse(null);
    }

    // ---------------------------------------
    // DELETE
    // ---------------------------------------
    private void handleDelete() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            view.showMessage("⚠ Vui lòng chọn dòng để xóa!");
            return;
        }

        InventoryModel item = getInventoryFromTable(row);
        if (item == null) {
            view.showMessage("⚠ Không tìm thấy dữ liệu để xóa!");
            return;
        }

        InventoryDeleteConfirmDialog dlg = new InventoryDeleteConfirmDialog(
                getFrame(),
                item.getBranchName(),
                item.getProductName()
        );

        dlg.showDialog();
        if (!dlg.isConfirmed()) {
            return;
        }

        try {
            service.deleteInventory(item.getId());
        } catch (IOException ex) {
            view.showMessage("⚠ Lỗi xóa: " + ex.getMessage());
        }
    }

    // ---------------------------------------
    // STATE (Redux-like)
    // ---------------------------------------
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateInventoryList(state);
            updateBranches(state);
            updateProducts(state);
            updateFilterOptions();
            showMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateInventoryList(AppState state) {
        Object list = state.get("Inventories");
        if (list instanceof List<?> raw) {
            inventories = (List<InventoryModel>) raw;
            applyFilter(); // tự động áp filter hiện tại
        }
    }

    @SuppressWarnings("unchecked")
    private void updateBranches(AppState state) {
        Object br = state.get("Branches");
        if (br instanceof List<?> raw) {
            branches = (List<BranchInfo>) raw;
        }
    }

    @SuppressWarnings("unchecked")
    private void updateProducts(AppState state) {
        Object pr = state.get("Products");
        if (pr instanceof List<?> raw) {
            products = (List<ProductInfo>) raw;
        }
    }

    // ---------------------------------------
    // UPDATE FILTER COMBOBOXES
    // ---------------------------------------
    private void updateFilterOptions() {
        if (branches != null) {
            view.updateBranchFilter(
                    branches.stream().map(BranchInfo::getName).collect(Collectors.toList())
            );
        }

        if (products != null) {
            view.updateProductFilter(
                    products.stream().map(ProductInfo::getName).collect(Collectors.toList())
            );
        }
    }

    // ---------------------------------------
    // MESSAGES
    // ---------------------------------------
    private void showMessages(AppState state) {

        if (isShowingMessage) {
            return;
        }

        String msg = (String) state.get("InventoryMessage");
        if (msg != null && !msg.isEmpty()) {
            isShowingMessage = true;
            state.set("InventoryMessage", "");
            view.showMessage(msg);

            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> view.clearMessage());
                }
            }, 3000);

            isShowingMessage = false;
        }

        String err = (String) state.get("InventoryError");
        if (err != null && !err.isEmpty()) {
            isShowingMessage = true;
            state.set("InventoryError", "");
            view.showMessage("⚠ " + err);

            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> view.clearMessage());
                }
            }, 5000);

            isShowingMessage = false;
        }
    }

    private Frame getFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
