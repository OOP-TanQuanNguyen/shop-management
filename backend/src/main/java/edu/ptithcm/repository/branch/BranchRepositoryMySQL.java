package edu.ptithcm.repository.branch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ptithcm.model.BranchModel;
import edu.ptithcm.repository.BaseRepository;

public class BranchRepositoryMySQL extends BaseRepository implements BranchRepository {

    private static BranchRepositoryMySQL instance;
    private BranchRepositoryMySQL() {}
    public static synchronized BranchRepositoryMySQL getInstance() {
        if (instance == null) instance = new BranchRepositoryMySQL();
        return instance;
    }

    @Override
    public void create(BranchModel b) throws SQLException {
        String sql = "INSERT INTO branch(name, phone, address, open_date, is_active) VALUES (?, ?, ?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, b.getName());
            ps.setString(2, b.getPhone());
            ps.setString(3, b.getAddress());
            ps.setDate(4, b.getOpenDate());
            ps.setBoolean(5, b.isActive());
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
    public void update(int id, BranchModel b) throws SQLException {
        String sql = "UPDATE branch SET name=?, phone=?, address=?, is_active=? WHERE branch_id=?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, b.getName());
            ps.setString(2, b.getPhone());
            ps.setString(3, b.getAddress());
            ps.setBoolean(4, b.isActive());
            ps.setInt(5, id);
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
    public void remove(int id) throws SQLException {
        String sql = "DELETE FROM branch WHERE branch_id=?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, id);
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
    public List<BranchModel> getAll() throws SQLException {
        List<BranchModel> list = new ArrayList<>();
        String sql = "SELECT branch_id, name, phone, address, open_date, is_active FROM branch";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new BranchModel.Builder()
                        .id(rs.getInt("branch_id"))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .address(rs.getString("address"))
                        .openDate(rs.getDate("open_date"))
                        .isActive(rs.getBoolean("is_active"))
                        .build());
            }
        }
        return list;
    }

    @Override
    public BranchModel findById(int id) throws SQLException {
        String sql = "SELECT branch_id, name, phone, address, open_date, is_active FROM branch WHERE branch_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new BranchModel.Builder()
                            .id(rs.getInt("branch_id"))
                            .name(rs.getString("name"))
                            .phone(rs.getString("phone"))
                            .address(rs.getString("address"))
                            .openDate(rs.getDate("open_date"))
                            .isActive(rs.getBoolean("is_active"))
                            .build();
                }
            }
        }
        return null;
    }
}
