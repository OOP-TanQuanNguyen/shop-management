package edu.ptithcm.repository.inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ptithcm.model.InventoryModel;
import edu.ptithcm.repository.BaseRepository;

public class InventoryRepositoryMySQL extends BaseRepository implements InventoryRepository {

    private static InventoryRepositoryMySQL instance;
    private InventoryRepositoryMySQL() {}
    public static synchronized InventoryRepositoryMySQL getInstance() {
        if (instance == null) instance = new InventoryRepositoryMySQL();
        return instance;
    }

    @Override
    public void create(InventoryModel inv) throws SQLException {
        String sql = "INSERT INTO inventory(branch_id, product_id, quantity, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, inv.getBranchId());
            ps.setString(2, inv.getProductId());
            ps.setInt(3, inv.getQuantity());
            ps.setTimestamp(4, inv.getCreatedAt());
            ps.setTimestamp(5, inv.getUpdatedAt());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateQuantity(int branchId, String productId, int newQty) throws SQLException {
        String sql = "UPDATE inventory SET quantity=?, updated_at=CURRENT_TIMESTAMP WHERE branch_id=? AND product_id=?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, newQty);
            ps.setInt(2, branchId);
            ps.setString(3, productId);
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
    public InventoryModel findByProduct(String productId, int branchId) throws SQLException {
        String sql = "SELECT inventory_id, branch_id, product_id, quantity, created_at, updated_at " +
                     "FROM inventory WHERE product_id=? AND branch_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.setInt(2, branchId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new InventoryModel.Builder()
                            .id(rs.getInt("inventory_id"))
                            .branch(rs.getInt("branch_id"))
                            .product(rs.getString("product_id"))
                            .quantity(rs.getInt("quantity"))
                            .created(rs.getTimestamp("created_at"))
                            .updated(rs.getTimestamp("updated_at"))
                            .build();
                }
            }
        }
        return null;
    }

    @Override
    public List<InventoryModel> getAllByBranch(int branchId) throws SQLException {
        List<InventoryModel> list = new ArrayList<>();
        String sql = "SELECT inventory_id, branch_id, product_id, quantity, created_at, updated_at " +
                     "FROM inventory WHERE branch_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, branchId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new InventoryModel.Builder()
                            .id(rs.getInt("inventory_id"))
                            .branch(rs.getInt("branch_id"))
                            .product(rs.getString("product_id"))
                            .quantity(rs.getInt("quantity"))
                            .created(rs.getTimestamp("created_at"))
                            .updated(rs.getTimestamp("updated_at"))
                            .build());
                }
            }
        }
        return list;
    }

    @Override
    public void remove(int id) throws SQLException {
        String sql = "DELETE FROM inventory WHERE inventory_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
