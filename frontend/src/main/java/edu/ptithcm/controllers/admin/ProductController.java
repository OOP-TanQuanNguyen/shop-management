package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.models.ProductInfo;
import edu.ptithcm.services.admin.ProductService;
import edu.ptithcm.views.admin.ProductPanel;
import edu.ptithcm.views.admin.product_dialogs.ProductAddDialog;
import edu.ptithcm.views.admin.product_dialogs.ProductDeleteConfirmDialog;
import edu.ptithcm.views.admin.product_dialogs.ProductEditDialog;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductController {

    private static final Logger logger = Logger.getLogger(ProductController.class.getName());

    private final ProductPanel view;
    private final ProductService service;
    private final Store store = Store.getInstance();

    private List<ProductInfo> currentProducts;
    private List<CategoryModel> currentCategories;

    private boolean isShowingMessage = false;

    public ProductController(ProductPanel view, ProductService service) {
        this.view = view;
        this.service = service;

        registerEvents();
        store.subcribe(this::onStateChanged);
        loadProducts();
    }

    private void registerEvents() {
        view.getBtnAdd().addActionListener(e -> handleAdd());
        view.getBtnEdit().addActionListener(e -> handleEdit());
        view.getBtnDelete().addActionListener(e -> handleDelete());
        view.getBtnReload().addActionListener(e -> loadProducts());

        // -------------------------- NEW: FILTER --------------------------
        view.getBtnFilter().addActionListener(e -> handleFilter());
    }

    private void loadProducts() {
        try {
            service.getAllProducts();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Không thể tải danh sách sản phẩm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====================================================================
    // FILTER PRODUCT (FE)
    // ====================================================================
    private void handleFilter() {
        if (currentProducts == null) {
            return;
        }

        String keyword = view.getTxtSearch().getText().trim().toLowerCase();
        String cateName = view.getCbCategory().getSelectedItem().toString();
        String status = view.getCbStatus().getSelectedItem().toString();

        List<ProductInfo> filtered = currentProducts;

        // Lọc từ khóa
        if (!keyword.isEmpty()) {
            filtered = filtered.stream()
                    .filter(p -> p.getName().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        // Lọc theo danh mục
        if (!cateName.equals("Tất cả")) {
            filtered = filtered.stream()
                    .filter(p -> cateName.equals(p.getCategoryName()))
                    .collect(Collectors.toList());
        }

        // Lọc theo trạng thái
        if (!status.equals("Tất cả")) {
            boolean isActive = status.equals("Đang bán");
            filtered = filtered.stream()
                    .filter(p -> p.getIsActive() == isActive)
                    .collect(Collectors.toList());
        }

        view.updateTable(filtered);
    }

    // ====================================================================
    // ADD
    // ====================================================================
    private void handleAdd() {
        if (currentCategories == null || currentCategories.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Không thể thêm sản phẩm! Hãy tạo danh mục trước.",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProductAddDialog dialog = new ProductAddDialog(getParentFrame(), currentCategories);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Map<String, Object> data = dialog.toMap();
            createProduct(data);
        }

        dialog.dispose();
    }

    // ====================================================================
    // EDIT
    // ====================================================================
    private void handleEdit() {
        int row = view.getTable().getSelectedRow();
        if (row == -1 || currentProducts == null || row >= currentProducts.size()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn sản phẩm để sửa!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentCategories == null || currentCategories.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Không thể sửa vì chưa có danh mục!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProductInfo product = currentProducts.get(row);

        ProductEditDialog dialog = new ProductEditDialog(
                getParentFrame(),
                currentCategories,
                product.getId(),
                product.getName(),
                product.getCategoryId(),
                product.getCostPrice(),
                product.getSellPrice(),
                product.getExpiryDate(),
                product.getIsActive()
        );

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Map<String, Object> data = dialog.toMap();
            data.put("productId", product.getId());
            updateProduct(data);
        }

        dialog.dispose();
    }

    // ====================================================================
    // DELETE
    // ====================================================================
    private void handleDelete() {
        int row = view.getTable().getSelectedRow();
        if (row == -1 || currentProducts == null || row >= currentProducts.size()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn sản phẩm để xóa!",
                    "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ProductInfo product = currentProducts.get(row);

        ProductDeleteConfirmDialog dialog
                = new ProductDeleteConfirmDialog(getParentFrame(), product.getName());

        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            deleteProduct(product.getId());
        }

        dialog.dispose();
    }

    // ====================================================================
    // CALL SERVICE
    // ====================================================================
    private void createProduct(Map<String, Object> data) {
        try {
            service.createProduct(data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm sản phẩm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct(Map<String, Object> data) {
        try {
            service.updateProduct(data);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi cập nhật sản phẩm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct(String id) {
        try {
            service.deleteProduct(id);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi xóa sản phẩm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====================================================================
    // STATE MANAGEMENT
    // ====================================================================
    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateProductList(state);
            updateCategoryList(state);
            showMessagesFromState(state);
        });
    }

    private void updateProductList(AppState state) {
        Object obj = state.get("Products");
        if (obj instanceof List<?> list) {
            currentProducts = (List<ProductInfo>) list;
            view.updateTable(currentProducts);
        }
    }

    @SuppressWarnings("unchecked")
    private void updateCategoryList(AppState state) {
        Object obj = state.get("Categories");
        if (obj instanceof List<?> list) {
            currentCategories = (List<CategoryModel>) list;

            // ==== NEW: đẩy danh mục vào Combo Filter ====
            List<String> names = currentCategories.stream()
                    .map(CategoryModel::getName)
                    .toList();
            view.updateCategoryFilter(names);

            logger.info("Loaded categories: " + currentCategories.size());
        }
    }

    private void showMessagesFromState(AppState state) {
        if (isShowingMessage) {
            return;
        }

        String msg = (String) state.get("ProductMessage");
        if (msg != null && !msg.isEmpty()) {
            isShowingMessage = true;
            state.set("ProductMessage", "");
            JOptionPane.showMessageDialog(view, msg);
            isShowingMessage = false;
            return;
        }

        String err = (String) state.get("ProductError");
        if (err != null && !err.isEmpty()) {
            isShowingMessage = true;
            state.set("ProductError", "");
            JOptionPane.showMessageDialog(view, err, "Lỗi", JOptionPane.ERROR_MESSAGE);
            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
