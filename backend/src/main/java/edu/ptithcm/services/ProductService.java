package edu.ptithcm.services;

import edu.ptithcm.dto.request.product.ProductRequestDTO;
import edu.ptithcm.dto.response.ProductInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.model.ProductModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.product.ProductRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    private static final ProductRepository productRepo = Repository.product();

    // ------------------ Lấy tất cả sản phẩm ------------------
    public ResponseDTO<List<ProductInfo>> getAllProducts(int limit) {
        try {
            List<ProductModel> list = productRepo.getAll(limit);
            return successList("PRODUCT_GET_ALL", "Lấy toàn bộ sản phẩm", mapList(list));
        } catch (SQLException e) {
            return errorList("PRODUCT_GET_ALL", e);
        }
    }

    // ------------------ Tạo sản phẩm ------------------
    public ResponseDTO<ProductInfo> createProduct(ProductRequestDTO req) {
        if (!req.validForCreate())
            return invalid("PRODUCT_CREATE", "Thiếu thông tin bắt buộc!");

        try {
            // Check trùng tên
            if (productRepo.exists(req.getName())) {
                return invalid("PRODUCT_CREATE", "Tên sản phẩm đã tồn tại!");
            }

            ProductModel model = new ProductModel.Builder()
                    .id(UUID.randomUUID().toString())
                    .name(req.getName())
                    .categoryId(req.getCategoryId())
                    .costPrice(req.getCostPrice())
                    .sellPrice(req.getSellPrice())
                    .expiryDate(req.getExpiryDate()) // giữ nguyên kiểu Date
                    .isActive(req.getIsActive() != null ? req.getIsActive() : true)
                    .build();

            productRepo.createProducts(List.of(model));
            return success("PRODUCT_CREATE", "Thêm sản phẩm thành công", toInfo(model));
        } catch (SQLException e) {
            return error("PRODUCT_CREATE", e);
        }
    }

    // ------------------ Cập nhật sản phẩm ------------------
    public ResponseDTO<ProductInfo> updateProduct(ProductRequestDTO req) {
        if (!req.validForUpdate())
            return invalid("PRODUCT_UPDATE", "Thiếu ID hoặc dữ liệu cập nhật!");

        try {
            ProductModel existing = productRepo.findById(req.getProductId());
            if (existing == null)
                return error("PRODUCT_UPDATE", new Exception("Không tìm thấy sản phẩm để cập nhật"));

            ProductModel updated = new ProductModel.Builder()
                    .id(req.getProductId())
                    .name(req.getName() != null ? req.getName() : existing.getName())
                    .categoryId(req.getCategoryId() != null ? req.getCategoryId() : existing.getCategoryId())
                    .costPrice(req.getCostPrice() != 0 ? req.getCostPrice() : existing.getCostPrice())
                    .sellPrice(req.getSellPrice() != 0 ? req.getSellPrice() : existing.getSellPrice())
                    .expiryDate(req.getExpiryDate() != null ? req.getExpiryDate() : existing.getExpiryDate())
                    .isActive(req.getIsActive() != null ? req.getIsActive() : existing.isActive())
                    .build();

            productRepo.update(updated);
            return success("PRODUCT_UPDATE", "Cập nhật sản phẩm thành công", toInfo(updated));
        } catch (SQLException e) {
            return error("PRODUCT_UPDATE", e);
        }
    }

    // ------------------ Xóa sản phẩm ------------------
    public ResponseDTO<ProductInfo> deleteProduct(ProductRequestDTO req) {
        if (req.getProductId() == null || req.getProductId().isEmpty())
            return invalid("PRODUCT_DELETE", "Thiếu ID sản phẩm!");

        try {
            productRepo.remove(req.getProductId());
            return success("PRODUCT_DELETE", "Xóa sản phẩm thành công", null);
        } catch (SQLException e) {
            return error("PRODUCT_DELETE", e);
        }
    }

    // ------------------ Lấy sản phẩm theo ID ------------------
    public ResponseDTO<ProductInfo> getProductById(String productId) {
        try {
            ProductModel model = productRepo.findById(productId);
            if (model == null)
                return error("PRODUCT_GET_BY_ID", new Exception("Sản phẩm không tồn tại"));
            return success("PRODUCT_GET_BY_ID", "Lấy sản phẩm thành công", toInfo(model));
        } catch (SQLException e) {
            return error("PRODUCT_GET_BY_ID", e);
        }
    }

    // ================= Helper =================
    private List<ProductInfo> mapList(List<ProductModel> models) {
        if (models == null) return Collections.emptyList();
        return models.stream().map(this::toInfo).collect(Collectors.toList());
    }

    private ProductInfo toInfo(ProductModel p) {
        return new ProductInfo(
                p.getId(),
                p.getName(),
                p.getCategoryId(),
                p.getCostPrice(),
                p.getSellPrice(),
                p.getExpiryDate(),
                p.isActive()
        );
    }

    private ResponseDTO<ProductInfo> success(String type, String msg, ProductInfo data) {
        return new ResponseDTO.Builder<ProductInfo>()
                .type(type)
                .status("SUCCESS")
                .message(msg)
                .data(data)
                .build();
    }

    private ResponseDTO<ProductInfo> invalid(String type, String msg) {
        return new ResponseDTO.Builder<ProductInfo>()
                .type(type)
                .status("INVALID")
                .message(msg)
                .data(null)
                .build();
    }

    private ResponseDTO<ProductInfo> error(String type, Exception e) {
        return new ResponseDTO.Builder<ProductInfo>()
                .type(type)
                .status("ERROR")
                .message(e.getMessage())
                .data(null)
                .build();
    }

    private ResponseDTO<List<ProductInfo>> successList(String type, String msg, List<ProductInfo> data) {
        return new ResponseDTO.Builder<List<ProductInfo>>()
                .type(type)
                .status("SUCCESS")
                .message(msg)
                .data(data)
                .build();
    }

    private ResponseDTO<List<ProductInfo>> errorList(String type, Exception e) {
        return new ResponseDTO.Builder<List<ProductInfo>>()
                .type(type)
                .status("ERROR")
                .message(e.getMessage())
                .data(null)
                .build();
    }
}
