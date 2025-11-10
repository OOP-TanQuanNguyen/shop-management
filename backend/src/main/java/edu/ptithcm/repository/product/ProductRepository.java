package edu.ptithcm.repository.product;

import edu.ptithcm.model.ProductModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ProductRepository {
    void createProduct(List<ProductModel> products) throws SQLException;
    boolean updateProduct(String productId, Map<String, Object> fields) throws SQLException;
    boolean deleteProduct(String productId) throws SQLException;
    ProductModel getProductById(String productId) throws SQLException;
    List<ProductModel> getAllProducts() throws SQLException;
}
