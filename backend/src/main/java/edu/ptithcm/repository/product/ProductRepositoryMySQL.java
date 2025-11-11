package edu.ptithcm.repository.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ptithcm.model.ProductModel;
import edu.ptithcm.repository.BaseRepository;

public class ProductRepositoryMySQL extends BaseRepository implements ProductRepository {

    private static ProductRepositoryMySQL instance;
    private ProductRepositoryMySQL() {}
    public static synchronized ProductRepositoryMySQL getInstance() {
        if (instance == null) instance = new ProductRepositoryMySQL();
        return instance;
    }

    private static final String BASE_SELECT =
        "SELECT p.product_id, p.name, p.category_id, c.name AS category, " +
        "p.cost_price, p.sell_price, p.expiry_date, p.is_active, p.created_at " +
        "FROM product p LEFT JOIN category c ON p.category_id=c.category_id ";

    @Override
    public boolean exists(String name) throws SQLException {
        String sql = "SELECT 1 FROM product WHERE name=? LIMIT 1";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    @Override
    public ProductModel findById(String id) throws SQLException {
        String sql = BASE_SELECT + "WHERE p.product_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    @Override
    public void createProducts(List<ProductModel> list) throws SQLException {
        String sql = "INSERT INTO product(product_id, name, category_id, cost_price, sell_price, expiry_date, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection c = getConnection();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            c.setAutoCommit(false);
            for (ProductModel p : list) {
                ps.setString(1, p.getId());
                ps.setString(2, p.getName());
                ps.setString(3, p.getCategoryId());
                ps.setDouble(4, p.getCostPrice());
                ps.setDouble(5, p.getSellPrice());
                if (p.getExpiryDate() != null) ps.setDate(6, p.getExpiryDate());
                else ps.setNull(6, Types.DATE);
                ps.setBoolean(7, p.isActive());
                ps.addBatch();
            }
            ps.executeBatch();
            c.commit();
        } catch (SQLException e) {
            safeRollback(c);
            throw e;
        } finally {
            c.setAutoCommit(true);
            closeConnection(c);
        }
    }

    @Override
    public List<ProductModel> getAll(int limit) throws SQLException {
        List<ProductModel> list = new ArrayList<>();
        String sql = BASE_SELECT + "ORDER BY p.created_at DESC LIMIT ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public List<ProductModel> search(String keyword) throws SQLException {
        List<ProductModel> list = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE p.name LIKE ? OR c.name LIKE ? ORDER BY p.name";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    @Override
    public List<ProductModel> filter(Map<String, Object> filters) throws SQLException {
        StringBuilder sql = new StringBuilder(BASE_SELECT + "WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (filters.containsKey("category_id")) {
            sql.append("AND p.category_id=? ");
            params.add(filters.get("category_id"));
        }
        if (filters.containsKey("is_active")) {
            sql.append("AND p.is_active=? ");
            params.add(filters.get("is_active"));
        }

        sql.append("ORDER BY p.created_at DESC");

        List<ProductModel> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    private ProductModel map(ResultSet rs) throws SQLException {
        return new ProductModel.Builder()
            .name(rs.getString("name"))
            .categoryId(rs.getString("category_id"))
            .category(rs.getString("category"))
            .costPrice(rs.getDouble("cost_price"))
            .sellPrice(rs.getDouble("sell_price"))
            .expiryDate(rs.getDate("expiry_date"))
            .isActive(rs.getBoolean("is_active"))
            .build();
    }
}
