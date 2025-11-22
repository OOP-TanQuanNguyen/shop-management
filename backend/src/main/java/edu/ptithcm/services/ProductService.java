package edu.ptithcm.services;

import java.util.List;
import java.util.UUID;

import edu.ptithcm.dto.request.product.ProductRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.ProductInfo;
import edu.ptithcm.models.CategoryModel;
import edu.ptithcm.models.ProductModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.category.CategoryRepository;
import edu.ptithcm.repository.product.ProductRepository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;
import edu.ptithcm.utils.SearchUtil;

public class ProductService {

    private static final ProductRepository productRepo = Repository.product();
    private static final CategoryRepository categoryRepo = Repository.category();
    private static final BaseMapper<ProductModel, ProductInfo> mapper = MapperFactory.product();

    // ------------------ Lấy tất cả ------------------
    public ResponseDTO<List<ProductInfo>> getAllProducts() throws RuntimeException {
        return new SuccessResponse<>(
                "Lấy toàn bộ sản phẩm thành công",
                mapper.toDTOList(productRepo.findAll())
        );
    }

    // ------------------ Tạo sản phẩm ------------------
    public ResponseDTO<ProductInfo> createProduct(ProductRequestDTO req) throws RuntimeException {

        if (!req.validForCreate())
            return new InvalidResponse<>("Thiếu tên sản phẩm");

        CategoryModel category = req.getCategoryId() != null
                ? categoryRepo.findById(req.getCategoryId())
                : null;

        ProductModel product = new ProductModel.Builder()
                .id(UUID.randomUUID().toString())
                .name(req.getName())
                .category(category)
                .costPrice(req.getCostPrice() != null ? req.getCostPrice() : 0.0)
                .sellPrice(req.getSellPrice() != null ? req.getSellPrice() : 0.0)
                .expiryDate(req.getExpiryDate())
                .isActive(req.getIsActive() != null ? req.getIsActive() : true)
                .build();

        productRepo.save(product);

        return new SuccessResponse<>("Tạo sản phẩm thành công", mapper.toDTO(product));
    }

    // ------------------ Cập nhật sản phẩm ------------------
    public ResponseDTO<ProductInfo> updateProduct(ProductRequestDTO req) throws RuntimeException {

        if (!req.validForUpdate())
            return new InvalidResponse<>("Thiếu ID sản phẩm");

        CategoryModel category = req.getCategoryId() != null
                ? categoryRepo.findById(req.getCategoryId())
                : null;

        ProductModel temp = new ProductModel.Builder()
                .id(req.getProductId())
                .name(req.getName())
                .category(category)
                .costPrice(req.getCostPrice())
                .sellPrice(req.getSellPrice())
                .expiryDate(req.getExpiryDate())
                .isActive(req.getIsActive() != null ? req.getIsActive() : true)
                .build();

        ProductModel updated = productRepo.update(temp);

        if (updated == null)
            return new NotFoundResponse<>("Không tìm thấy sản phẩm để cập nhật");

        return new SuccessResponse<>("Cập nhật sản phẩm thành công", mapper.toDTO(updated));
    }

    // ------------------ Xóa sản phẩm ------------------
    public ResponseDTO<ProductInfo> deleteProduct(ProductRequestDTO req) throws RuntimeException {

        if (req.getProductId() == null || req.getProductId().isBlank())
            return new InvalidResponse<>("Thiếu ID sản phẩm");

        ProductModel deleted = productRepo.delete(req.getProductId());

        if (deleted == null)
            return new NotFoundResponse<>("Không tồn tại sản phẩm");

        return new SuccessResponse<>("Xóa sản phẩm thành công", mapper.toDTO(deleted));
    }

    // ------------------ Tìm kiếm theo tên ------------------
    public ResponseDTO<List<ProductInfo>> getProductByName(String keyword) throws RuntimeException {

        if (!SearchUtil.isKeywordValid(keyword))
            return new InvalidResponse<>("Thiếu từ khóa tìm kiếm");

        String normalized = SearchUtil.normalize(keyword);

        List<ProductModel> products = productRepo.searchByName(normalized);

        return new SuccessResponse<>(
                "Tìm kiếm sản phẩm thành công",
                mapper.toDTOList(products)
        );
    }


    // ------------------ Lấy theo ID ------------------
    public ResponseDTO<ProductInfo> getProductById(ProductRequestDTO req) throws RuntimeException {

        ProductModel product = productRepo.findById(req.getProductId());

        if (product == null)
            return new NotFoundResponse<>("Không tìm thấy sản phẩm");

        return new SuccessResponse<>("Lấy sản phẩm thành công", mapper.toDTO(product));
    }
}
