package edu.ptithcm.controllers.admin;

import edu.ptithcm.app.AppState;
import edu.ptithcm.app.store.Store;
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

public class ProductController {

    private static final Logger logger = Logger.getLogger(ProductController.class.getName());

    private final ProductPanel view;
    private final ProductService service;
    private final Store store = Store.getInstance();
    private List<ProductInfo> currentProducts;
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
    }

    private void loadProducts() {
        try {
            service.getAllProducts();
        } catch (IOException e) {
            logger.severe(String.format("Failed to load products: %s", e.getMessage()));
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(view, "Không thể tải danh sách sản phẩm: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void handleAdd() {
        SwingUtilities.invokeLater(() -> {
            ProductAddDialog dialog = new ProductAddDialog(getParentFrame());
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                Map<String, Object> data = dialog.toMap();
                logger.info("Product data to create: " + data);
                createProduct(data);
            }

            dialog.dispose();
        });
    }

    private void handleEdit() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm để sửa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentProducts == null || row >= currentProducts.size()) {
            JOptionPane.showMessageDialog(view, "Không thể lấy thông tin sản phẩm!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                ProductInfo product = currentProducts.get(row);

                logger.info(String.format("Editing product: ID=%s, Name=%s", product.getId(), product.getName()));

                ProductEditDialog dialog = new ProductEditDialog(
                        getParentFrame(),
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
                    logger.info("Product data to update: " + data);
                    updateProduct(data);
                }

                dialog.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi khi đọc thông tin sản phẩm: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                logger.severe(String.format("Edit error: %s", e.getMessage()));
            }
        });
    }

    private void handleDelete() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm để xóa!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentProducts == null || row >= currentProducts.size()) {
            JOptionPane.showMessageDialog(view, "Không thể lấy thông tin sản phẩm!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                ProductInfo product = currentProducts.get(row);
                String id = product.getId();
                String name = product.getName();

                logger.info(String.format("Attempting to delete product: ID=%s, Name=%s", id, name));

                ProductDeleteConfirmDialog dialog = new ProductDeleteConfirmDialog(getParentFrame(), name);
                dialog.setVisible(true);

                if (dialog.isConfirmed()) {
                    logger.info("Delete confirmed for ID: " + id);
                    deleteProduct(id);
                }

                dialog.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi khi xóa sản phẩm: " + e.getMessage(),
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                logger.severe(String.format("Delete error: %s", e.getMessage()));
            }
        });
    }

    private void createProduct(Map<String, Object> productData) {
        try {
            service.createProduct(productData);
            logger.info(String.format("Create product request sent for: %s", productData.get("name")));
        } catch (IOException e) {
            logger.severe(String.format("Failed to send create request: %s", e.getMessage()));
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(view, "Lỗi khi thêm sản phẩm: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void updateProduct(Map<String, Object> productData) {
        try {
            service.updateProduct(productData);
            String id = productData.containsKey("productId") ? String.valueOf(productData.get("productId")) : "Unknown";
            logger.info(String.format("Update product request sent for ID: %s", id));
        } catch (IOException e) {
            logger.severe(String.format("Failed to send update request: %s", e.getMessage()));
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật sản phẩm: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void deleteProduct(String id) {
        try {
            service.deleteProduct(id);
            logger.info(String.format("Delete product request sent for ID: %s", id));
        } catch (IOException e) {
            logger.severe(String.format("Failed to send delete request: %s", e.getMessage()));
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(view, "Lỗi khi xóa sản phẩm: " + e.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE)
            );
        }
    }

    private void onStateChanged(AppState state) {
        SwingUtilities.invokeLater(() -> {
            updateProductList(state);
            showMessagesFromState(state);
        });
    }

    private void updateProductList(AppState state) {
        Object prodListObj = state.get("Products");
        if (prodListObj instanceof List<?> list) {
            @SuppressWarnings("unchecked")
            List<ProductInfo> products = (List<ProductInfo>) list;
            this.currentProducts = products;
            view.updateTable(products);
            logger.info(String.format("Product list updated: %d items", products.size()));
        }
    }

    private void showMessagesFromState(AppState state) {
        if (isShowingMessage) {
            return;
        }

        Object msg = state.get("ProductMessage");
        if (msg instanceof String message && !message.isEmpty()) {
            isShowingMessage = true;
            state.set("ProductMessage", "");

            if (message.contains("thành công")) {
                JOptionPane.showMessageDialog(view, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

            isShowingMessage = false;
        }

        Object err = state.get("ProductError");
        if (err instanceof String error && !error.isEmpty()) {
            isShowingMessage = true;
            state.set("ProductError", "");

            JOptionPane.showMessageDialog(view, error, "Lỗi", JOptionPane.ERROR_MESSAGE);

            isShowingMessage = false;
        }
    }

    private Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(view);
    }
}
