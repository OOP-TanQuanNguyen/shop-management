package edu.ptithcm.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ptithcm.dto.request.invoice.InvoiceRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.InvoiceInfo;
import edu.ptithcm.models.*;
import edu.ptithcm.models.InvoiceModel.InvoiceStatus;
import edu.ptithcm.repository.*;
import edu.ptithcm.repository.branch.BranchRepository;
import edu.ptithcm.repository.customer.CustomerRepository;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.repository.inventory.InventoryRepository;
import edu.ptithcm.repository.invoice.InvoiceRepository;
import edu.ptithcm.repository.product.ProductRepository;
import edu.ptithcm.utils.BigDecimalUtil;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class InvoiceService {

    private static final InvoiceRepository invoiceRepo = Repository.invoice();
    private static final EmployeeRepository employeeRepo = Repository.employee();
    private static final BranchRepository branchRepo = Repository.branch();
    private static final CustomerRepository customerRepo = Repository.customer();
    private static final ProductRepository productRepo = Repository.product();
    private static final InventoryRepository inventoryRepo = Repository.inventory();
    private static final BaseMapper<InvoiceModel, InvoiceInfo> mapper = MapperFactory.invoice();

    // ------------------ Lấy tất cả hóa đơn ------------------
    public ResponseDTO<List<InvoiceInfo>> getAllInvoices() throws RuntimeException {
        List<InvoiceModel> list = invoiceRepo.findAll();
        return new SuccessResponse<>("Lấy tất cả hóa đơn thành công", mapper.toDTOList(list));
    }

    // ------------------ Tạo hóa đơn ------------------
    public ResponseDTO<InvoiceInfo> createInvoice(InvoiceRequestDTO req) throws RuntimeException {
        if (!req.validForCreate()) 
            return new InvalidResponse<>("Dữ liệu hóa đơn không hợp lệ");

         EmployeeModel employee = employeeRepo.findById(req.getEmployeeId());
        if (employee == null) return new NotFoundResponse<>("Nhân viên không tồn tại");

        BranchModel branch = branchRepo.findById(req.getBranchId());
        if (branch == null) return new NotFoundResponse<>("Chi nhánh không tồn tại");

        if (!employee.getBranch().getId().equals(branch.getId()))
            return new InvalidResponse<>("Nhân viên không thuộc chi nhánh này");

        CustomerModel customer = null;
        if (req.getCustomerId() != null)
            customer = customerRepo.findById(req.getCustomerId());

        if (req.getCustomerId() != null && customer == null)
            return new NotFoundResponse<>("Khách hàng không tồn tại");

        List<InvoiceDetailModel> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (InvoiceRequestDTO.InvoiceDetailRequest d : req.getDetails()) {

            if (d.getQuantity() <= 0)
                return new InvalidResponse<>("Số lượng sản phẩm phải > 0");

            ProductModel product = productRepo.findById(d.getProductId());
            if (product == null) 
                return new NotFoundResponse<>("Sản phẩm không tồn tại: " + d.getProductId());

            // Check tồn kho
            InventoryModel inv = inventoryRepo.findByBranchAndProduct(branch.getId(), product.getId());
            if (inv == null || inv.getQuantity() < d.getQuantity())
                return new InvalidResponse<>("Sản phẩm không đủ tồn kho: " + product.getName());

            // Tạm trừ tồn kho
            inv.setQuantity(inv.getQuantity() - d.getQuantity());
            inventoryRepo.update(inv);

            // Tính giá
            BigDecimal unitPrice = BigDecimalUtil.safe(product.getSellPrice());
            InvoiceDetailModel detail = new InvoiceDetailModel(product, null, d.getQuantity(), unitPrice);

            details.add(detail);
            total = total.add(detail.getTotal());
        }

        // ---------- Tính discount ----------
        BigDecimal discount = BigDecimalUtil.safe(req.getDiscount());
        if (discount.compareTo(total) > 0)
            return new InvalidResponse<>("Giảm giá không thể lớn hơn tổng tiền");

        BigDecimal finalTotal = total.subtract(discount);

        // ---------- Tạo invoice ----------
        InvoiceModel invoice = new InvoiceModel.Builder()
                .id(UUID.randomUUID().toString())
                .employee(employee)
                .branch(branch)
                .customer(customer)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .discount(discount)
                .note(req.getNote())
                .details(details)
                .total(finalTotal)
                .status(InvoiceModel.InvoiceStatus.PENDING)
                .build();
    
        // Set invoice cho detail
        for (InvoiceDetailModel d : details)
            d.setInvoice(invoice);

        // ---------- Lưu ----------
        invoiceRepo.save(invoice);
        return new SuccessResponse<>("Tạo hóa đơn thành công", mapper.toDTO(invoice));
    }

    // ------------------ Xác nhận thanh toán ------------------
    public ResponseDTO<InvoiceInfo> confirmInvoice(String invoiceId) {
        InvoiceModel invoice = invoiceRepo.findById(invoiceId);
        if (invoice == null)
            return new NotFoundResponse<>("Hóa đơn không tồn tại");

        if (invoice.getStatus() != InvoiceModel.InvoiceStatus.PENDING)
            return new InvalidResponse<>("Hóa đơn không ở trạng thái chờ xử lý");

        // Trừ kho chính thức
        for (InvoiceDetailModel detail : invoice.getDetails()) {
            InventoryModel inv = inventoryRepo.findByBranchAndProduct(invoice.getBranch().getId(), detail.getProduct().getId());
            if (inv == null || inv.getQuantity() < detail.getQuantity())
                return new InvalidResponse<>("Sản phẩm không đủ tồn kho: " + detail.getProduct().getName());

            inv.setQuantity(inv.getQuantity() - detail.getQuantity());
            inventoryRepo.update(inv);
        }

        invoice.setStatus(InvoiceModel.InvoiceStatus.COMPLETED);
        invoiceRepo.update(invoice);

        return new SuccessResponse<>("Xác nhận thanh toán thành công", mapper.toDTO(invoice));
    }

    // ------------------ Hủy hóa đơn ------------------
    public ResponseDTO<InvoiceInfo> cancelInvoice(String invoiceId) {
        InvoiceModel invoice = invoiceRepo.findById(invoiceId);
        if (invoice == null)
            return new NotFoundResponse<>("Hóa đơn không tồn tại");

        if (invoice.getStatus() == InvoiceStatus.CANCELLED)
            return new InvalidResponse<>("Hóa đơn đã bị hủy");

        invoice.setStatus(InvoiceStatus.CANCELLED);

        // Rollback tồn kho nếu muốn
        if (invoice.getDetails() != null) {
            for (InvoiceDetailModel d : invoice.getDetails()) {
                InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                        invoice.getBranch().getId(),
                        d.getProduct().getId()
                );
                if (inv != null) {
                    inv.setQuantity(inv.getQuantity() + d.getQuantity());
                    inventoryRepo.update(inv);
                }
            }
        }

        InvoiceModel updated = invoiceRepo.update(invoice);
        return new SuccessResponse<>("Hủy hóa đơn thành công", mapper.toDTO(updated));
    }

    // ------------------ Cập nhật hóa đơn ------------------
    public ResponseDTO<InvoiceInfo> updateInvoice(InvoiceRequestDTO req) throws RuntimeException {
        if (!req.validForUpdate()) 
            return new InvalidResponse<>("Thiếu ID hóa đơn");

        InvoiceModel existing = invoiceRepo.findById(req.getInvoiceId());
        if (existing == null) return new NotFoundResponse<>("Hóa đơn không tồn tại");

        if (req.getEmployeeId() != null) existing.setEmployee(employeeRepo.findById(req.getEmployeeId()));
        if (req.getBranchId() != null) existing.setBranch(branchRepo.findById(req.getBranchId()));
        if (req.getCustomerId() != null) existing.setCustomer(customerRepo.findById(req.getCustomerId()));
        if (req.getNote() != null) existing.setNote(req.getNote());
        if (req.getDiscount() != null) {
            BigDecimal discount = BigDecimalUtil.safe(req.getDiscount());
            if (discount.compareTo(existing.getTotal()) > 0)
                return new InvalidResponse<>("Giảm giá lớn hơn tổng tiền");
            existing.setDiscount(discount);
        }

        // Cập nhật chi tiết nếu có
        if (req.getDetails() != null && !req.getDetails().isEmpty()) {
            List<InvoiceDetailModel> details = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (InvoiceRequestDTO.InvoiceDetailRequest d : req.getDetails()) {
                ProductModel product = productRepo.findById(d.getProductId());
                if (product == null) return new NotFoundResponse<>("Sản phẩm không tồn tại: " + d.getProductId());

                BigDecimal unitPrice = BigDecimalUtil.safe(
                        product.getSellPrice() != null ? BigDecimal.valueOf(product.getSellPrice()) : BigDecimal.ZERO
                );
                InvoiceDetailModel detail = new InvoiceDetailModel(product, existing, d.getQuantity(), unitPrice);
                details.add(detail);
                total = total.add(BigDecimalUtil.safe(detail.getTotal()));
            }

            existing.setDetails(details);
            existing.setTotal(total.subtract(BigDecimalUtil.safe(existing.getDiscount())));
        } else {
            // Nếu không update chi tiết, vẫn cần cập nhật tổng dựa trên discount
            existing.setTotal(existing.getTotal().subtract(BigDecimalUtil.safe(existing.getDiscount())));
        }

        InvoiceModel updated = invoiceRepo.update(existing);
        return new SuccessResponse<>("Cập nhật hóa đơn thành công", mapper.toDTO(updated));
    }

    // ------------------ Xóa hóa đơn ------------------
    public ResponseDTO<InvoiceInfo> deleteInvoice(String invoiceId) throws RuntimeException {
        if (invoiceId == null || invoiceId.isBlank()) 
            return new InvalidResponse<>("Thiếu ID hóa đơn");

        InvoiceModel deleted = invoiceRepo.delete(invoiceId);
        if (deleted == null) return new NotFoundResponse<>("Hóa đơn không tồn tại");

        return new SuccessResponse<>("Xóa hóa đơn thành công", mapper.toDTO(deleted));
    }

    // ------------------ Lấy hóa đơn theo khách hàng ------------------
    public ResponseDTO<List<InvoiceInfo>> getInvoiceByCustomer(String customerId) throws RuntimeException {
        List<InvoiceModel> list = invoiceRepo.findByCustomer(customerId);
        return new SuccessResponse<>("Lấy hóa đơn theo khách hàng thành công", mapper.toDTOList(list));
    }

    // ------------------ Lấy hóa đơn theo chi nhánh ------------------
    public ResponseDTO<List<InvoiceInfo>> getInvoiceByBranch(Integer branchId) throws RuntimeException {
        List<InvoiceModel> list = invoiceRepo.findByBranch(branchId);
        return new SuccessResponse<>("Lấy hóa đơn theo chi nhánh thành công", mapper.toDTOList(list));
    }

    // ------------------ Lấy hóa đơn theo nhân viên ------------------
    public ResponseDTO<List<InvoiceInfo>> getInvoiceByEmployee(String employeeId) throws RuntimeException {
        List<InvoiceModel> list = invoiceRepo.findByEmployee(employeeId);
        return new SuccessResponse<>("Lấy hóa đơn theo nhân viên thành công", mapper.toDTOList(list));
    }

    // ------------------ Lấy hóa đơn theo ID ------------------
    public ResponseDTO<InvoiceInfo> getInvoiceById(String invoiceId) throws RuntimeException {
        InvoiceModel invoice = invoiceRepo.findById(invoiceId);
        if (invoice == null) return new NotFoundResponse<>("Hóa đơn không tồn tại");
        return new SuccessResponse<>("Lấy hóa đơn thành công", mapper.toDTO(invoice));
    }
}
