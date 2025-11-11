package edu.ptithcm.repository.invoice;

import java.sql.*;
import java.util.*;
import edu.ptithcm.model.InvoiceDetailModel;
import edu.ptithcm.repository.BaseRepository;

public class InvoiceDetailRepositoryMySQL extends BaseRepository implements InvoiceDetailRepository {

    private static InvoiceDetailRepositoryMySQL instance;
    private InvoiceDetailRepositoryMySQL() {}
    public static synchronized InvoiceDetailRepositoryMySQL getInstance() {
        if (instance == null) instance = new InvoiceDetailRepositoryMySQL();
        return instance;
    }

    // CREATE BATCH (rollback-safe)
    @Override
    public void createBatch(List<InvoiceDetailModel> details) throws SQLException {
        String sql = "INSERT INTO invoice_detail(product_id, invoice_id, quantity, unit_price, total) VALUES (?, ?, ?, ?, ?)";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (InvoiceDetailModel d : details) {
                ps.setString(1, d.getProductId());
                ps.setString(2, d.getInvoiceId());
                ps.setInt(3, d.getQuantity());
                ps.setDouble(4, d.getUnitPrice());
                ps.setDouble(5, d.getTotal());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (SQLException ex) {
            safeRollback(conn);
            throw ex;
        } finally {
            conn.setAutoCommit(true);
            closeConnection(conn);
        }
    }

    // FIND BY INVOICE
    @Override
    public List<InvoiceDetailModel> findByInvoice(String invoiceId) throws SQLException {
        String sql = "SELECT product_id, invoice_id, quantity, unit_price, total FROM invoice_detail WHERE invoice_id=?";
        List<InvoiceDetailModel> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new InvoiceDetailModel.Builder()
                            .product(rs.getString("product_id"))
                            .invoice(rs.getString("invoice_id"))
                            .quantity(rs.getInt("quantity"))
                            .unitPrice(rs.getDouble("unit_price"))
                            .build());
                }
            }
        }
        return list;
    }

    // REMOVE BY INVOICE
    @Override
    public void removeByInvoice(String invoiceId) throws SQLException {
        String sql = "DELETE FROM invoice_detail WHERE invoice_id=?";
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, invoiceId);
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
}
