package edu.ptithcm.controller;

import java.util.List;

import edu.ptithcm.dto.request.invoice.InvoiceRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.InvoiceInfo;
import edu.ptithcm.services.InvoiceService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class InvoiceController {

    private static final InvoiceService invoiceService = new InvoiceService();

    public ResponseDTO<List<InvoiceInfo>> getAll() {
        return SafeExecutor.run(() -> invoiceService.getAllInvoices());
    }

    public ResponseDTO<InvoiceInfo> getById(String invoiceId) {
        return SafeExecutor.run(() -> invoiceService.getById(invoiceId));
    }

    public ResponseDTO<List<InvoiceInfo>> getByCustomer(String customerId) {
        return SafeExecutor.run(() -> invoiceService.getByCustomer(customerId));
    }

    public ResponseDTO<List<InvoiceInfo>> getByBranch(Integer branchId) {
        return SafeExecutor.run(() -> invoiceService.getByBranch(branchId));
    }

    public ResponseDTO<List<InvoiceInfo>> getByEmployee(String employeeId) {
        return SafeExecutor.run(() -> invoiceService.getByEmployee(employeeId));
    }

    public ResponseDTO<InvoiceInfo> create(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> invoiceService.createInvoice(req));
    }

    public ResponseDTO<InvoiceInfo> update(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> invoiceService.updateInvoice(req));
    }

    public ResponseDTO<InvoiceInfo> delete(String invoiceId) {
        return SafeExecutor.run(() -> invoiceService.deleteInvoice(invoiceId));
    }
}
