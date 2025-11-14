package edu.ptithcm.controller;

import java.util.List;

import edu.ptithcm.dto.request.product.ProductRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.ProductInfo;
import edu.ptithcm.services.ProductService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class ProductController {

    private static final ProductService service = new ProductService();

    public ResponseDTO<ProductInfo> createProduct(ProductRequestDTO req) {
        return SafeExecutor.run(() -> service.createProduct(req));
    }

    public ResponseDTO<ProductInfo> updateProduct(ProductRequestDTO req) {
        return SafeExecutor.run(() -> service.updateProduct(req));
    }

    public ResponseDTO<ProductInfo> deleteProduct(ProductRequestDTO req) {
        return SafeExecutor.run( () -> service.deleteProduct(req));
    }

    public ResponseDTO<List<ProductInfo>> getAllProducts() {
        return SafeExecutor.run( () -> service.getAllProducts());
    }

    public ResponseDTO<ProductInfo> getProductById(ProductRequestDTO req) {
        return SafeExecutor.run( () -> service.getProductById(req));
    }

    public ResponseDTO<List<ProductInfo>> searchProductByName(String keyword){
        return SafeExecutor.run( () -> service.searchByName(keyword));
    }
}
