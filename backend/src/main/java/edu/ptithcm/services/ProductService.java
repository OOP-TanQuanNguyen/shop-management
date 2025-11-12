package edu.ptithcm.services;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import edu.ptithcm.dto.response.ProductInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.models.ProductModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.category.CategoryRepository;
import edu.ptithcm.repository.product.ProductRepository;

public class ProductService {
    private static final ProductRepository productRepo = Repository.product();
    private static final CategoryRepository categoryRepo = Repository.category();

    public ResponseDTO<List<ProductInfo>> getAllProducts() {
        try {
            List<ProductModel> list = productRepo.findAll();
            return successList("PRODUCT_GET_ALL", "Lấy toàn bộ sản phẩm", mapList(list));
        } catch (Exception e) {
            return errorList("PRODUCT_GET_ALL", e);
        }
    }

    public ResponseDTO<ProductInfo> createProduct(
            String name, String categoryId, double costPrice,
            double sellPrice, Date expiryDate, Boolean isActive
    ) {
        try {
            CategoryModel category = categoryId != null ? categoryRepo.findById(categoryId) : null;

            ProductModel model = new ProductModel.Builder()
                    .id(UUID.randomUUID().toString())
                    .name(name)
                    .category(category)
                    .costPrice(costPrice)
                    .sellPrice(sellPrice)
                    .expiryDate(expiryDate)
                    .isActive(isActive != null ? isActive : true)
                    .build();

            productRepo.save(model);
            return success("PRODUCT_CREATE", "Thêm sản phẩm thành công", toInfo(model));

        } catch (Exception e) {
            return error("PRODUCT_CREATE", e);
        }
    }

    public ResponseDTO<ProductInfo> updateProduct(
            String productId, String name, String categoryId,
            double costPrice, double sellPrice, Date expiryDate, Boolean isActive
    ) {
        try {
            ProductModel existing = productRepo.findById(productId);
            if (existing == null)
                return invalid("PRODUCT_UPDATE", "Không tìm thấy sản phẩm!");

            if (name != null) existing.setName(name);
            if (categoryId != null) existing.setCategory(categoryRepo.findById(categoryId));
            if (costPrice > 0) existing.setCostPrice(costPrice);
            if (sellPrice > 0) existing.setSellPrice(sellPrice);
            if (expiryDate != null) existing.setExpiryDate(expiryDate);
            if (isActive != null) existing.setActive(isActive);

            productRepo.update(existing);
            return success("PRODUCT_UPDATE", "Cập nhật sản phẩm thành công", toInfo(existing));

        } catch (Exception e) {
            return error("PRODUCT_UPDATE", e);
        }
    }

    public ResponseDTO<ProductInfo> deleteProduct(String productId) {
        try {
            ProductModel existing = productRepo.findById(productId);
            if (existing == null)
                return invalid("PRODUCT_DELETE", "Sản phẩm không tồn tại!");

            productRepo.delete(existing);
            return success("PRODUCT_DELETE", "Xóa sản phẩm thành công", null);
        } catch (Exception e) {
            return error("PRODUCT_DELETE", e);
        }
    }

    public ResponseDTO<ProductInfo> getProductById(String id) {
        try {
            ProductModel p = productRepo.findById(id);
            if (p == null)
                return invalid("PRODUCT_GET_BY_ID", "Không tồn tại!");
            return success("PRODUCT_GET_BY_ID", "Lấy sản phẩm thành công", toInfo(p));
        } catch (Exception e) {
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
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getCostPrice(),
                p.getSellPrice(),
                p.getExpiryDate(),
                p.isActive()
        );
    }

    private ResponseDTO<ProductInfo> success(String type, String msg, ProductInfo data) {
        return new ResponseDTO.Builder<ProductInfo>()
                .type(type).status("SUCCESS").message(msg).data(data).build();
    }

    private ResponseDTO<ProductInfo> invalid(String type, String msg) {
        return new ResponseDTO.Builder<ProductInfo>()
                .type(type).status("INVALID").message(msg).data(null).build();
    }

    private ResponseDTO<ProductInfo> error(String type, Exception e) {
        return new ResponseDTO.Builder<ProductInfo>()
                .type(type).status("ERROR").message(e.getMessage()).data(null).build();
    }

    private ResponseDTO<List<ProductInfo>> successList(String type, String msg, List<ProductInfo> data) {
        return new ResponseDTO.Builder<List<ProductInfo>>()
                .type(type).status("SUCCESS").message(msg).data(data).build();
    }

    private ResponseDTO<List<ProductInfo>> errorList(String type, Exception e) {
        return new ResponseDTO.Builder<List<ProductInfo>>()
                .type(type).status("ERROR").message(e.getMessage()).data(null).build();
    }
}
