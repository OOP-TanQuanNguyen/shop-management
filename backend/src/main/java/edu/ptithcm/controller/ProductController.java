package edu.ptithcm.controller;

import edu.ptithcm.dto.request.product.ProductRequestDTO;
import edu.ptithcm.dto.response.ProductInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.services.ProductService;

import java.util.List;

public class ProductController {
    private static final ProductService service = new ProductService();

    public ResponseDTO<ProductInfo> createProduct(ProductRequestDTO req) {
        return service.createProduct(req);
    }

    public ResponseDTO<List<ProductInfo>> getAllProducts() {
        return service.getAllProducts();
    }

    public ResponseDTO<ProductInfo> updateProduct(ProductRequestDTO req) {
        return service.updateProduct(req);
    }

    public ResponseDTO<ProductInfo> deleteProduct(ProductRequestDTO req) {
        return service.deleteProduct(req);
    }
}
