package edu.ptithcm.repository.product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.ptithcm.model.ProductModel;

public interface ProductRepository {
    boolean exists(String name) throws SQLException;
    ProductModel findById(String id) throws SQLException;
    void createProducts(List<ProductModel> products) throws SQLException;
    void update(ProductModel product) throws SQLException;
    void remove(String id) throws SQLException;
    List<ProductModel> getAll(int limit) throws SQLException;
    List<ProductModel> search(String keyword) throws SQLException;
    List<ProductModel> filter(Map<String, Object> filters) throws SQLException;
}
