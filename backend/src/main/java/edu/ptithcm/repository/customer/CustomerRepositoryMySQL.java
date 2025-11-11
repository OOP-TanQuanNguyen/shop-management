package edu.ptithcm.repository.customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ptithcm.model.CustomerModel;
import edu.ptithcm.repository.BaseRepository;

public class CustomerRepositoryMySQL extends BaseRepository implements CustomerRepository {

    private static CustomerRepositoryMySQL instance;
    private CustomerRepositoryMySQL() {}

    public static synchronized CustomerRepositoryMySQL getInstance() {
        if (instance == null) instance = new CustomerRepositoryMySQL();
        return instance;
    }

    @Override
    public void create(CustomerModel ctm) throws SQLException {
        final String sql = "INSERT INTO customer(customer_id, name, phone, created_at) VALUES (?, ?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, ctm.getId());
            ps.setString(2, ctm.getName());
            ps.setString(3, ctm.getPhone());
            ps.setTimestamp(4, ctm.getCreatedAt());
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
    public void update(String id, String name, String phone) throws SQLException {
        final String sql = "UPDATE customer SET name = ?, phone = ? WHERE customer_id = ?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, id);
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
        final String sql = "DELETE FROM customer WHERE customer_id = ?";
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
    public boolean exists(String phone) throws SQLException {
        final String sql = "SELECT 1 FROM customer WHERE phone = ? LIMIT 1";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public CustomerModel findById(String id) throws SQLException {
        final String sql = "SELECT customer_id, name, phone, created_at FROM customer WHERE customer_id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CustomerModel.Builder()
                            .id(rs.getString("customer_id"))
                            .name(rs.getString("name"))
                            .phone(rs.getString("phone"))
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public List<CustomerModel> getAll(int limit) throws SQLException {
        final String sql = "SELECT customer_id, name, phone, created_at FROM customer ORDER BY created_at DESC LIMIT ?";
        List<CustomerModel> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CustomerModel.Builder()
                            .id(rs.getString("customer_id"))
                            .name(rs.getString("name"))
                            .phone(rs.getString("phone"))
                            .build());
                }
            }
        }
        return list;
    }

    @Override
    public List<CustomerModel> search(String keyword) throws SQLException {
        final String sql = "SELECT customer_id, name, phone, created_at " +
                           "FROM customer WHERE name LIKE ? OR phone LIKE ? ORDER BY created_at DESC";
        List<CustomerModel> list = new ArrayList<>();
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CustomerModel.Builder()
                            .id(rs.getString("customer_id"))
                            .name(rs.getString("name"))
                            .phone(rs.getString("phone"))
                            .build());
                }
            }
        }
        return list;
    }
}
