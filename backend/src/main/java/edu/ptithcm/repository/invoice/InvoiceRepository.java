package edu.ptithcm.repository.invoice;

import java.sql.SQLException;
import java.util.List;

import edu.ptithcm.model.InvoiceModel;

public interface InvoiceRepository {
    void create(InvoiceModel invoice) throws SQLException;
    void update(String id, double total, double discount, String note) throws SQLException;
    void remove(String id) throws SQLException;
    InvoiceModel findById(String id) throws SQLException;
    List<InvoiceModel> findByCustomer(String customerId) throws SQLException;
    List<InvoiceModel> getAll(int limit) throws SQLException;
    List<InvoiceModel> search(String keyword) throws SQLException;
}
