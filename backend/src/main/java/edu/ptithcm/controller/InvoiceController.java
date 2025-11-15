package edu.ptithcm.controller;

import java.util.List;

import edu.ptithcm.dto.request.invoice.InvoiceRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.InvoiceInfo;
import edu.ptithcm.services.InvoiceService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class InvoiceController {

    private static final InvoiceService service = new InvoiceService();

    // ---------- CREATE ----------
    public ResponseDTO<InvoiceInfo> createInvoice(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> service.createInvoice(req));
    }

    // ---------- UPDATE ----------
    public ResponseDTO<InvoiceInfo> updateInvoice(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> service.updateInvoice(req));
    }

    // ---------- DELETE ----------
    public ResponseDTO<InvoiceInfo> deleteInvoice(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> service.deleteInvoice(req.getInvoiceId()));
    }

    // ---------- GET ALL ----------
    public ResponseDTO<List<InvoiceInfo>> getAllInvoices() {
        return SafeExecutor.run(() -> service.getAllInvoices());
    }

    // ---------- GET BY ID ----------
    public ResponseDTO<InvoiceInfo> getInvoiceById(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> service.getById(req.getInvoiceId()));
    }

    // ---------- GET BY CUSTOMER ----------
    public ResponseDTO<List<InvoiceInfo>> getByCustomer(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> service.getByCustomer(req.getCustomerId()));
    }

    // ---------- GET BY BRANCH ----------
    public ResponseDTO<List<InvoiceInfo>> getByBranch(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> service.getByBranch(req.getBranchId()));
    }

    // ---------- GET BY EMPLOYEE ----------
    public ResponseDTO<List<InvoiceInfo>> getByEmployee(InvoiceRequestDTO req) {
        return SafeExecutor.run(() -> service.getByEmployee(req.getEmployeeId()));
    }
}
