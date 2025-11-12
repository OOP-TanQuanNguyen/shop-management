package edu.ptithcm.controller;

import java.util.List;
import java.util.Map;

import edu.ptithcm.dto.request.product.ProductRequestDTO;
import edu.ptithcm.dto.response.ProductInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.services.ProductService;

public class ProductController {
    private static final ProductService service = new ProductService();

    // ------------------ CREATE ------------------
    public ResponseDTO<ProductInfo> createProduct(Object data) {
        try {
            Map<String, Object> map = (Map<String, Object>) data;
            ProductRequestDTO req = new ProductRequestDTO(map);

            // 👉 Rã các trường
            String name = req.getName();
            String categoryId = req.getCategoryId();
            double costPrice = req.getCostPrice();
            double sellPrice = req.getSellPrice();
            java.sql.Date expiryDate = req.getExpiryDate();
            Boolean isActive = req.getIsActive();

            // 👉 Gọi service (service KHÔNG ép kiểu nữa)
            return service.createProduct(name, categoryId, costPrice, sellPrice, expiryDate, isActive);
        } catch (Exception e) {
            return new ResponseDTO.Builder<ProductInfo>()
                    .type("PRODUCT_CREATE")
                    .status("ERROR")
                    .message("Lỗi parse dữ liệu: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    // ------------------ UPDATE ------------------
    public ResponseDTO<ProductInfo> updateProduct(Object data) {
        try {
            Map<String, Object> map = (Map<String, Object>) data;
            ProductRequestDTO req = new ProductRequestDTO(map);

            String productId = req.getProductId();
            String name = req.getName();
            String categoryId = req.getCategoryId();
            double costPrice = req.getCostPrice();
            double sellPrice = req.getSellPrice();
            java.sql.Date expiryDate = req.getExpiryDate();
            Boolean isActive = req.getIsActive();

            return service.updateProduct(productId, name, categoryId, costPrice, sellPrice, expiryDate, isActive);
        } catch (Exception e) {
            return new ResponseDTO.Builder<ProductInfo>()
                    .type("PRODUCT_UPDATE")
                    .status("ERROR")
                    .message("Lỗi parse dữ liệu: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    // ------------------ DELETE ------------------
    public ResponseDTO<ProductInfo> deleteProduct(Object data) {
        try {
            Map<String, Object> map = (Map<String, Object>) data;
            String productId = (String) map.get("productId");
            return service.deleteProduct(productId);
        } catch (Exception e) {
            return new ResponseDTO.Builder<ProductInfo>()
                    .type("PRODUCT_DELETE")
                    .status("ERROR")
                    .message("Lỗi parse dữ liệu: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }

    // ------------------ GET ------------------
    public ResponseDTO<List<ProductInfo>> getAllProducts() {
        return service.getAllProducts();
    }

    public ResponseDTO<ProductInfo> getProductById(Object data) {
        try {
            Map<String, Object> map = (Map<String, Object>) data;
            String id = (String) map.get("productId");
            return service.getProductById(id);
        } catch (Exception e) {
            return new ResponseDTO.Builder<ProductInfo>()
                    .type("PRODUCT_GET_BY_ID")
                    .status("ERROR")
                    .message("Lỗi parse dữ liệu: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
