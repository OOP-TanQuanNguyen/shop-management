package edu.ptithcm.controllers.admin;

import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.services.admin.CategoryService;
import edu.ptithcm.views.admin.CategoryPanel;
import edu.ptithcm.views.admin.category_dialogs.CategoryAddDialog;
import edu.ptithcm.views.admin.category_dialogs.CategoryDeleteConfirmDialog;
import edu.ptithcm.views.admin.category_dialogs.CategoryEditDialog;
import edu.ptithcm.views.components.AppMessageBox;

public class CategoryController {

    private final CategoryPanel view;
    private final CategoryService service;
    private final Store store = Store.getInstance();

    private boolean isShowingMessage = false;
    private List<CategoryModel> currentCategories;

    public CategoryController(CategoryPanel view, CategoryService service) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);

        reloadCategories();   // load initial + reset search
    }

    // ================================================================
    // RESET FILTER (chỉ reset khi bấm Tải lại)
    // ================================================================
    private void resetFilter() {
        view.getTxtSearch().setText("");
    }

    // ================================================================
    // REGISTER EVENTS
    // ================================================================
    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());

        // Tải lại → reset filter + reload BE
        view.getBtnReload().addActionListener(e -> reloadCategories());

        // Lọc (local filter)
        view.getBtnFilter().addActionListener(e -> handleFilter());
    }

    // ================================================================
    // RELOAD CATEGORY
    // ================================================================
    private void reloadCategories() {
        resetFilter();     // chỉ reset khi nhấn Tải lại
        loadCategories();
    }

    private void loadCategories() {
        try {
            service.getAllCategories();
        } catch (IOException e) {
            AppMessageBox.showError("Không thể tải danh sách danh mục: " + e.getMessage());
        }
    }

    // ================================================================
    // FILTER LOCAL
    // ================================================================
    private void handleFilter() {

        if (currentCategories == null) {
            return;
        }

        String keyword = view.getTxtSearch().getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            // Nếu rỗng → trả full list, không reset BE
            view.updateTable(currentCategories);
            return;
        }

        List<CategoryModel> filtered = new ArrayList<>();

        for (CategoryModel c : currentCategories) {
            if (c.getName() != null
                    && c.getName().toLowerCase().contains(keyword)) {

                filtered.add(c);
            }
        }

        view.updateTable(filtered);
    }

    // ================================================================
    // CRUD
    // ================================================================
    private void handleAdd() {
        CategoryAddDialog dialog = new CategoryAddDialog(getParentFrame());
        dialog.showDialog();

        if (dialog.isConfirmed()) {
            try {
                service.createCategory(dialog.getCategoryName());
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi thêm danh mục: " + e.getMessage());
            }
        }
    }

    private void handleEdit() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn danh mục để chỉnh sửa!");
            return;
        }

        if (currentCategories == null || row >= currentCategories.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return;
        }

        CategoryModel category = currentCategories.get(row);

        CategoryEditDialog dialog = new CategoryEditDialog(
                getParentFrame(),
                category.getCategoryId(),
                category.getName()
        );
        dialog.showDialog();

        if (dialog.isConfirmed()) {
            try {
                service.updateCategory(dialog.getCategoryId(), dialog.getCategoryName());
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi cập nhật danh mục: " + e.getMessage());
            }
        }
    }

    private void handleDelete() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            AppMessageBox.showWarning("Vui lòng chọn danh mục để xóa!");
            return;
        }

        if (currentCategories == null || row >= currentCategories.size()) {
            AppMessageBox.showError("Dữ liệu không hợp lệ!");
            return;
        }

        CategoryModel category = currentCategories.get(row);

        CategoryDeleteConfirmDialog dialog
                = new CategoryDeleteConfirmDialog(getParentFrame(), category.getName());
        dialog.showDialog();

        if (dialog.isConfirmed()) {
            try {
                service.deleteCategory(category.getCategoryId());
            } catch (IOException e) {
                AppMessageBox.showError("Lỗi xóa danh mục: " + e.getMessage());
            }
        }
    }

    // ================================================================
    // STATE MANAGER
    // ================================================================
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateCategoryList(state);
            showMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateCategoryList(AppState state) {

        Object listObj = state.get("Categories");

        if (listObj instanceof List<?> list) {

            currentCategories = (List<CategoryModel>) list;

            // Không reset filter – chỉ update table
            view.updateTable(currentCategories);
        }
    }

    private void showMessages(AppState state) {

        if (isShowingMessage) {
            return;
        }

        String successMsg = (String) state.get("CategoryMessage");
        if (successMsg != null && !successMsg.isEmpty()) {
            isShowingMessage = true;
            state.set("CategoryMessage", "");
            AppMessageBox.showSuccess(successMsg);
            isShowingMessage = false;
            return;
        }

        String errorMsg = (String) state.get("CategoryError");
        if (errorMsg != null && !errorMsg.isEmpty()) {
            isShowingMessage = true;
            state.set("CategoryError", "");
            AppMessageBox.showError(errorMsg);
            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
