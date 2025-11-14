package edu.ptithcm.repository.invoice;

import java.util.List;

import edu.ptithcm.models.InvoiceModel;
import edu.ptithcm.repository.GenericRepository;

public interface InvoiceRepository extends GenericRepository<InvoiceModel, String> {
    List<InvoiceModel> findByCustomer(String customerId);
    List<InvoiceModel> findByBranch(Integer branchId);
    List<InvoiceModel> findByEmployee(String employeeId); 
}
