package edu.ptithcm.repository.product;

import edu.ptithcm.model.ProductModel;
import edu.ptithcm.configs.databases.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductRepositoryMySQL implements ProductRepository {

    private static ProductRepository instance;

    private ProductRepositoryMySQL() {}

    public static ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepositoryMySQL();
        }
        return instance;
    }

    @Override
    public void createProduct(List<ProductModel> products) throws SQLException {
        if (products == null || products.isEmpty()) return;

        String sql = "INSERT INTO product (product_id, name, category_id, cost_price, sell_price, expiry_date, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (ProductModel p : products) {
                ps.setString(1, p.getProductId());
                ps.setString(2, p.getName());
                ps.setString(3, p.getCategoryId());
                ps.setDouble(4, p.getCostPrice());
                ps.setDouble(5, p.getSellPrice());
                if (p.getExpiryDate() != null) {
                    ps.setDate(6, new java.sql.Date(p.getExpiryDate().getTime()));
                } else {
                    ps.setNull(6, Types.DATE);
                }
                ps.setBoolean(7, p.getIsActive() != null ? p.getIsActive() : false);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
    
    @Override
    public List<ProductModel> getAllProducts() throws SQLException {
        List<ProductModel> products = new ArrayList<>();
        String sql = "SELECT * FROM product";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        }
        return products;
    }

    @Override
    public ProductModel getProductById(String productId) throws SQLException {
        String sql = "SELECT * FROM product WHERE product_id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }
        }
        return null;
    }

    @Override
public boolean updateProduct(String productId, Map<String, Object> fields) throws SQLException {
    if (fields == null || fields.isEmpty()) return false;

    StringBuilder sql = new StringBuilder("UPDATE product SET ");
    List<Object> params = new ArrayList<>();
    int i = 0;

    for (String key : fields.keySet()) {
        if (i > 0) sql.append(", ");
        sql.append(key).append(" = ?");
        Object value = fields.get(key);
        if ("expiry_date".equals(key) && value instanceof String) {
            // Convert String -> java.sql.Date
            java.sql.Date date = value != null && !((String) value).isEmpty()
                    ? java.sql.Date.valueOf((String) value)
                    : null;
            params.add(date);
        } else {
            params.add(value);
        }
        i++;
    }

    sql.append(" WHERE product_id = ?");
    params.add(productId);

    try (Connection conn = Database.getInstance().getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        for (int j = 0; j < params.size(); j++) {
            Object param = params.get(j);
            if (param == null) {
                ps.setNull(j + 1, Types.NULL);
            } else if (param instanceof String) {
                ps.setString(j + 1, (String) param);
            } else if (param instanceof Double) {
                ps.setDouble(j + 1, (Double) param);
            } else if (param instanceof Boolean) {
                ps.setBoolean(j + 1, (Boolean) param);
            } else if (param instanceof java.sql.Date) {
                ps.setDate(j + 1, (java.sql.Date) param);
            } else {
                ps.setObject(j + 1, param);
            }
        }

        return ps.executeUpdate() > 0;
    }
}

    @Override
    public boolean deleteProduct(String productId) throws SQLException {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, productId);
            return ps.executeUpdate() > 0;
        }
    }

    // ===========================
    // Helper: map ResultSet -> ProductModel
    // ===========================
    private ProductModel mapProduct(ResultSet rs) throws SQLException {
        return new ProductModel.Builder()
                .productId(rs.getString("product_id"))
                .name(rs.getString("name"))
                .categoryId(rs.getString("category_id"))
                .costPrice(rs.getDouble("cost_price"))
                .sellPrice(rs.getDouble("sell_price"))
                .expiryDate(rs.getDate("expiry_date"))
                .isActive(rs.getBoolean("is_active"))
                .build();
    }
}
