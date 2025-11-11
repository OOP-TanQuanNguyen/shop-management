package edu.ptithcm.repository.invoice;

import java.sql.SQLException;
import java.util.List;

import edu.ptithcm.model.InvoiceDetailModel;

public interface InvoiceDetailRepository {
    void createBatch(List<InvoiceDetailModel> details) throws SQLException;
    List<InvoiceDetailModel> findByInvoice(String invoiceId) throws SQLException;
    void removeByInvoice(String invoiceId) throws SQLException;
}
