package edu.ptithcm.services;

import edu.ptithcm.dto.request.product.ProductRequestDTO;
import edu.ptithcm.dto.response.ProductInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.model.ProductModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.product.ProductRepository;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    private static final ProductRepository productRepo = Repository.p();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // ------------------ Lấy tất cả sản phẩm ------------------
    public ResponseDTO<List<ProductInfo>> getAllProducts() {
        try {
            List<ProductModel> list = productRepo.getAllProducts();
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
            // Chuyển String → Date
            Date expiry = null;
            if (req.getExpiryDate() != null && !req.getExpiryDate().isEmpty()) {
                try {
                    expiry = sdf.parse(req.getExpiryDate());
                } catch (ParseException e) {
                    return invalid("PRODUCT_CREATE", "Định dạng ngày không hợp lệ (yyyy-MM-dd)");
                }
            }

            ProductModel model = new ProductModel.Builder()
                    .name(req.getName())
                    .categoryId(req.getCategoryId())
                    .costPrice(req.getCostPrice())
                    .sellPrice(req.getSellPrice())
                    .expiryDate(expiry)
                    .isActive(req.getIsActive() != null ? req.getIsActive() : true)
                    .build();

            productRepo.createProduct(List.of(model));

            ProductInfo info = toInfo(model);
            return success("PRODUCT_CREATE", "Thêm sản phẩm thành công", info);
        } catch (SQLException e) {
            return error("PRODUCT_CREATE", e);
        }
    }

    // ------------------ Cập nhật sản phẩm ------------------
    public ResponseDTO<ProductInfo> updateProduct(ProductRequestDTO req) {
        if (!req.validForUpdate())
            return invalid("PRODUCT_UPDATE", "Thiếu ID hoặc dữ liệu cập nhật!");

        try {
            Map<String, Object> fields = new HashMap<>();
            if (req.getName() != null) fields.put("name", req.getName());
            if (req.getCategoryId() != null) fields.put("category_id", req.getCategoryId());
            if (req.getCostPrice() != 0) fields.put("cost_price", req.getCostPrice());
            if (req.getSellPrice() != 0) fields.put("sell_price", req.getSellPrice());
            if (req.getExpiryDate() != null) fields.put("expiry_date", sdf.parse(req.getExpiryDate()));
            if (req.getIsActive() != null) fields.put("is_active", req.getIsActive());

            boolean updated = productRepo.updateProduct(req.getProductId(), fields);
            if (!updated)
                return error("PRODUCT_UPDATE", new Exception("Cập nhật sản phẩm thất bại hoặc không tìm thấy sản phẩm"));

            return success("PRODUCT_UPDATE", "Cập nhật sản phẩm thành công", null);
        } catch (SQLException | ParseException e) {
            return error("PRODUCT_UPDATE", e);
        }
    }

    // ------------------ Xóa sản phẩm ------------------
    public ResponseDTO<ProductInfo> deleteProduct(ProductRequestDTO req) {
        if (req.getProductId() == null)
            return invalid("PRODUCT_DELETE", "Thiếu ID sản phẩm!");

        try {
            boolean deleted = productRepo.deleteProduct(req.getProductId());
            if (!deleted)
                return error("PRODUCT_DELETE", new Exception("Xóa sản phẩm thất bại hoặc không tìm thấy sản phẩm"));

            return success("PRODUCT_DELETE", "Xóa sản phẩm thành công", null);
        } catch (SQLException e) {
            return error("PRODUCT_DELETE", e);
        }
    }

    // ------------------ Lấy sản phẩm theo ID ------------------
    public ResponseDTO<ProductInfo> getProductById(String productId) {
        try {
            ProductModel model = productRepo.getProductById(productId);
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
        String expiryStr = p.getExpiryDate() != null ? sdf.format(p.getExpiryDate()) : null;
        return new ProductInfo(
                p.getProductId(),
                p.getName(),
                p.getCategoryId(),
                p.getCostPrice(),
                p.getSellPrice(),
                expiryStr,
                p.getIsActive()
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
