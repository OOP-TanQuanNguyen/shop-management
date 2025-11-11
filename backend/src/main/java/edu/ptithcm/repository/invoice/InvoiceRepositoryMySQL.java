package edu.ptithcm.repository.invoice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ptithcm.model.InvoiceModel;
import edu.ptithcm.repository.BaseRepository;

public class InvoiceRepositoryMySQL extends BaseRepository implements InvoiceRepository {

    private static InvoiceRepositoryMySQL instance;
    private InvoiceRepositoryMySQL() {}
    public static synchronized InvoiceRepositoryMySQL getInstance() {
        if (instance == null) instance = new InvoiceRepositoryMySQL();
        return instance;
    }

    // CREATE
    @Override
    public void create(InvoiceModel inv) throws SQLException {
        String sql = "INSERT INTO invoice(invoice_id, employee_id, branch_id, customer_id, created_at, total, discount, note) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, inv.getId());
            ps.setString(2, inv.getEmployeeId());
            ps.setObject(3, inv.getBranchId(), java.sql.Types.INTEGER);
            ps.setString(4, inv.getCustomerId());
            ps.setTimestamp(5, inv.getCreatedAt());
            ps.setDouble(6, inv.getTotal());
            ps.setDouble(7, inv.getDiscount());
            ps.setString(8, inv.getNote());
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

    // UPDATE
    @Override
    public void update(String id, double total, double discount, String note) throws SQLException {
        String sql = "UPDATE invoice SET total=?, discount=?, note=? WHERE invoice_id=?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setDouble(1, total);
            ps.setDouble(2, discount);
            ps.setString(3, note);
            ps.setString(4, id);
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

    // REMOVE
    @Override
    public void remove(String id) throws SQLException {
        String sql = "DELETE FROM invoice WHERE invoice_id=?";
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

    // FIND BY ID
    @Override
    public InvoiceModel findById(String id) throws SQLException {
        String sql = "SELECT invoice_id, employee_id, branch_id, customer_id, created_at, total, discount, note "
                   + "FROM invoice WHERE invoice_id=?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapInvoice(rs);
            }
        }
        return null;
    }

    // FIND BY CUSTOMER
    @Override
    public List<InvoiceModel> findByCustomer(String cid) throws SQLException {
        String sql = "SELECT invoice_id, employee_id, branch_id, customer_id, created_at, total, discount, note "
                   + "FROM invoice WHERE customer_id=? ORDER BY created_at DESC";
        List<InvoiceModel> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapInvoice(rs));
            }
        }
        return list;
    }

    // GET ALL
    @Override
    public List<InvoiceModel> getAll(int limit) throws SQLException {
        String sql = "SELECT invoice_id, employee_id, branch_id, customer_id, created_at, total, discount, note "
                   + "FROM invoice ORDER BY created_at DESC LIMIT ?";
        List<InvoiceModel> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapInvoice(rs));
            }
        }
        return list;
    }

    // SEARCH
    @Override
    public List<InvoiceModel> search(String keyword) throws SQLException {
        String sql = "SELECT invoice_id, employee_id, branch_id, customer_id, created_at, total, discount, note "
                   + "FROM invoice WHERE note LIKE ? OR invoice_id LIKE ? ORDER BY created_at DESC";
        List<InvoiceModel> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapInvoice(rs));
            }
        }
        return list;
    }

    private InvoiceModel mapInvoice(ResultSet rs) throws SQLException {
        return new InvoiceModel.Builder()
                .employee(rs.getString("employee_id"))
                .branch(rs.getInt("branch_id"))
                .customer(rs.getString("customer_id"))
                .total(rs.getDouble("total"))
                .discount(rs.getDouble("discount"))
                .note(rs.getString("note"))
                .build();
    }
}
