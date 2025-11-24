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
    //  INITIAL LOAD
    // ---------------------------------------
    private void initialLoad() {
        try {
            service.getAllInventories();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Không tải được kho: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------
    //  REGISTER EVENTS
    // ---------------------------------------
    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> initialLoad());
    }

    // ---------------------------------------
    //  ADD
    // ---------------------------------------
    private void handleAdd() {

        if (branches == null || products == null
                || branches.isEmpty() || products.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Danh sách chi nhánh hoặc sản phẩm chưa tải!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(view,
                    "Lỗi tạo kho: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------
    //  EDIT
    // ---------------------------------------
    private void handleEdit() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn dòng!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        InventoryModel item = inventories.get(row);

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
            JOptionPane.showMessageDialog(view,
                    "Lỗi cập nhật: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------
    //  DELETE
    // ---------------------------------------
    private void handleDelete() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn dòng!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        InventoryModel item = inventories.get(row);

        InventoryDeleteConfirmDialog dlg
                = new InventoryDeleteConfirmDialog(getFrame(),
                        item.getBranchName(), item.getProductName());

        dlg.showDialog();
        if (!dlg.isConfirmed()) {
            return;
        }

        try {
            service.deleteInventory(item.getId());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi xóa: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------
    //  STATE CHANGE
    // ---------------------------------------
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateInventoryList(state);
            updateBranches(state);
            updateProducts(state);
            showMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateInventoryList(AppState state) {
        Object list = state.get("Inventories");
        if (list instanceof List<?> raw) {
            inventories = (List<InventoryModel>) raw;
            view.updateTable(inventories);
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
    //  MESSAGES
    // ---------------------------------------
    private void showMessages(AppState state) {

        if (isShowingMessage) {
            return;
        }

        String msg = (String) state.get("InventoryMessage");
        if (msg != null && !msg.isEmpty()) {
            isShowingMessage = true;
            state.set("InventoryMessage", "");
            JOptionPane.showMessageDialog(view, msg);
            isShowingMessage = false;
        }

        String err = (String) state.get("InventoryError");
        if (err != null && !err.isEmpty()) {
            isShowingMessage = true;
            state.set("InventoryError", "");
            JOptionPane.showMessageDialog(view, err, "Lỗi", JOptionPane.ERROR_MESSAGE);
            isShowingMessage = false;
        }
    }

    private Frame getFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
