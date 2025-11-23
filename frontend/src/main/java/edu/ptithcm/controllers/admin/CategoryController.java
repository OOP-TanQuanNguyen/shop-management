package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CategoryInfo;
import edu.ptithcm.services.admin.CategoryService;
import edu.ptithcm.views.admin.CategoryPanel;
import edu.ptithcm.views.admin.category_dialogs.CategoryAddDialog;
import edu.ptithcm.views.admin.category_dialogs.CategoryEditDialog;
import edu.ptithcm.views.admin.category_dialogs.CategoryDeleteConfirmDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CategoryController {

    private static final Logger logger = Logger.getLogger(CategoryController.class.getName());

    private final CategoryPanel view;
    private final CategoryService service;
    private final Store store = Store.getInstance();

    private List<CategoryInfo> currentCategories;
    private boolean isShowingMessage = false;

    public CategoryController(CategoryPanel view, CategoryService service) {
        this.view = view;
        this.service = service;

        registerEvents();

        store.subcribe(this::onStateChanged);

        loadCategories();
    }

    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> loadCategories());
    }

    // ============================================================
    // Load dữ liệu Category
    // ============================================================
    private void loadCategories() {
        try {
            service.getAllCategories();
        } catch (IOException e) {
            logger.severe("Load categories failed: " + e.getMessage());
            JOptionPane.showMessageDialog(view,
                    "Không thể tải danh sách danh mục: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // Xử lý Add
    // ============================================================
    private void handleAdd() {
        SwingUtilities.invokeLater(() -> {
            CategoryAddDialog dialog = new CategoryAddDialog(getParentFrame());
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Map<String, Object> data = dialog.toMap();
                createCategory(data);
            }

            dialog.dispose();
        });
    }

    private void createCategory(Map<String, Object> data) {
        try {
            service.createCategory(data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm danh mục: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // Xử lý Edit
    // ============================================================
    private void handleEdit() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn danh mục để sửa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CategoryInfo category = currentCategories.get(row);

        SwingUtilities.invokeLater(() -> {
            CategoryEditDialog dialog = new CategoryEditDialog(
                    getParentFrame(),
                    category.getCategoryId(),
                    category.getName()
            );
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Map<String, Object> map = dialog.toMap();
                map.put("categoryId", category.getCategoryId());
                updateCategory(map);
            }

            dialog.dispose();
        });
    }

    private void updateCategory(Map<String, Object> data) {
        try {
            service.updateCategory(data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi cập nhật danh mục: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // Xử lý Delete
    // ============================================================
    private void handleDelete() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn danh mục để xóa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CategoryInfo category = currentCategories.get(row);

        SwingUtilities.invokeLater(() -> {
            CategoryDeleteConfirmDialog dialog
                    = new CategoryDeleteConfirmDialog(getParentFrame(), category.getName());
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                deleteCategory(category.getCategoryId());
            }

            dialog.dispose();
        });
    }

    private void deleteCategory(String id) {
        try {
            service.deleteCategory(id);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi xóa danh mục: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // Store State Change
    // ============================================================
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateCategoryList(state);
            checkMessages(state);
        });
    }

    @SuppressWarnings("unchecked")
    private void updateCategoryList(AppState state) {
        Object obj = state.get("Categories");

        if (obj instanceof List<?> list) {
            currentCategories = (List<CategoryInfo>) list;
            view.updateTable(currentCategories);
        }
    }

    private void checkMessages(AppState state) {

        if (isShowingMessage) {
            return;
        }

        Object msg = state.get("CategoryMessage");
        if (msg instanceof String message && !message.isEmpty()) {
            isShowingMessage = true;

            state.set("CategoryMessage", "");
            JOptionPane.showMessageDialog(view, message,
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            isShowingMessage = false;
        }

        Object err = state.get("CategoryError");
        if (err instanceof String error && !error.isEmpty()) {
            isShowingMessage = true;

            state.set("CategoryError", "");
            JOptionPane.showMessageDialog(view, error,
                    "Lỗi", JOptionPane.ERROR_MESSAGE);

            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
