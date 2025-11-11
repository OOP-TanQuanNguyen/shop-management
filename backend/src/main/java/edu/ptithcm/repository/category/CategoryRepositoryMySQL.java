package edu.ptithcm.repository.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ptithcm.model.CategoryModel;
import edu.ptithcm.repository.BaseRepository;

public class CategoryRepositoryMySQL extends BaseRepository implements CategoryRepository {

    private static CategoryRepositoryMySQL instance;
    private CategoryRepositoryMySQL() {}
    public static synchronized CategoryRepositoryMySQL getInstance() {
        if (instance == null) instance = new CategoryRepositoryMySQL();
        return instance;
    }

    @Override
    public void create(CategoryModel c) throws SQLException {
        String sql = "INSERT INTO category(category_id, name) VALUES (?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, c.getId());
            ps.setString(2, c.getName());
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            safeRollback(conn);
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            closeConnection(conn);
        }
    }

    @Override
    public void update(String id, String name) throws SQLException {
        String sql = "UPDATE category SET name=? WHERE category_id=?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, name);
            ps.setString(2, id);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            safeRollback(conn);
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            closeConnection(conn);
        }
    }

    @Override
    public void remove(String id) throws SQLException {
        String sql = "DELETE FROM category WHERE category_id=?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, id);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException ex) {
            safeRollback(conn);
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            closeConnection(conn);
        }
    }

    @Override
    public List<CategoryModel> getAll() throws SQLException {
        List<CategoryModel> list = new ArrayList<>();
        String sql = "SELECT category_id, name FROM category ORDER BY name";
        try (Connection c = getConnection(); 
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new CategoryModel(
                    rs.getString("category_id"),
                    rs.getString("name")
                ));
            }
        }
        return list;
    }

    @Override
    public CategoryModel findById(String id) throws SQLException {
        String sql = "SELECT category_id, name FROM category WHERE category_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CategoryModel(
                        rs.getString("category_id"),
                        rs.getString("name")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean exists(String name) throws SQLException {
        String sql = "SELECT 1 FROM category WHERE name = ? LIMIT 1";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
