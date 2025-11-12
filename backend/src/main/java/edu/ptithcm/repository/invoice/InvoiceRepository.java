package edu.ptithcm.repository.invoice;

import edu.ptithcm.models.InvoiceModel;
import edu.ptithcm.repository.GenericRepository;
import java.util.List;

public interface InvoiceRepository extends GenericRepository<InvoiceModel, String> {
    List<InvoiceModel> findByCustomer(String customerId);
    List<InvoiceModel> findByBranch(Integer branchId);
}
