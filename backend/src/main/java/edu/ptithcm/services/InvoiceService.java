package edu.ptithcm.services;

import edu.ptithcm.dto.request.invoice.InvoiceRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.info_models.InvoiceInfo;
import edu.ptithcm.dto.response.success.SuccessResponse;
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
import edu.ptithcm.utils.DraftCacheUtil;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceService {

    private static final InvoiceRepository invoiceRepo = Repository.invoice();
    private static final EmployeeRepository employeeRepo =
        Repository.employee();
    private static final BranchRepository branchRepo = Repository.branch();
    private static final CustomerRepository customerRepo =
        Repository.customer();
    private static final ProductRepository productRepo = Repository.product();
    private static final InventoryRepository inventoryRepo =
        Repository.inventory();
    private static final BaseMapper<InvoiceModel, InvoiceInfo> mapper =
        MapperFactory.invoice();

    // Draft cache 5 phút
    private static final DraftCacheUtil<InvoiceModel> draftCache =
        new DraftCacheUtil<>(5 * 60 * 1000, draft -> {
            // rollback stock khi draft hết hạn
            if (draft.getDetails() != null) {
                for (InvoiceDetailModel d : draft.getDetails()) {
                    InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                        draft.getBranch().getId(),
                        d.getProduct().getId()
                    );
                    if (inv != null) {
                        inv.setQuantity(inv.getQuantity() + d.getQuantity());
                        inventoryRepo.update(inv);
                    }
                }
            }
        });

    // =====================================================================
    // LẤY TẤT CẢ HÓA ĐƠN
    // =====================================================================
    public ResponseDTO<List<InvoiceInfo>> getAllInvoices() {
        List<InvoiceModel> list = invoiceRepo.findAll();
        System.out.println("Services get all invoice: " + list);
        return new SuccessResponse<>(
            "Lấy tất cả hóa đơn thành công",
            mapper.toDTOList(list)
        );
    }

    public ResponseDTO<InvoiceInfo> createInvoice(InvoiceRequestDTO req) {
        if (req == null) {
            return new InvalidResponse<>("Dữ liệu hóa đơn trống");
        }

        if (!req.validForCreate()) {
            return new InvalidResponse<>("Dữ liệu hóa đơn không hợp lệ");
        }

        EmployeeModel employee = employeeRepo.findById(req.getEmployeeId());
        if (employee == null) {
            return new NotFoundResponse<>("Nhân viên không tồn tại");
        }

        BranchModel branch = branchRepo.findById(req.getBranchId());
        if (branch == null) {
            return new NotFoundResponse<>("Chi nhánh không tồn tại");
        }

        if (!employee.getBranch().getId().equals(branch.getId())) {
            return new InvalidResponse<>("Nhân viên không thuộc chi nhánh này");
        }

        CustomerModel customer = null;
        if (req.getCustomerId() != null) {
            customer = customerRepo.findById(req.getCustomerId());
            if (customer == null) {
                return new NotFoundResponse<>("Khách hàng không tồn tại");
            }
        }

        InvoiceModel draft = new InvoiceModel.Builder()
            .id(UUID.randomUUID().toString())
            .employee(employee)
            .branch(branch)
            .customer(customer)
            .createdAt(new Timestamp(System.currentTimeMillis()))
            .discount(BigDecimalUtil.safe(req.getDiscount()))
            .note(req.getNote())
            .status(InvoiceStatus.PENDING)
            .build();

        List<InvoiceDetailModel> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // Chỉ check tồn kho — KHÔNG TRỪ KHO
        for (InvoiceRequestDTO.InvoiceDetailRequest d : req.getDetails()) {
            if (d.getQuantity() <= 0) {
                return new InvalidResponse<>("Số lượng sản phẩm phải > 0");
            }

            ProductModel product = productRepo.findById(d.getProductId());
            if (product == null) {
                return new NotFoundResponse<>(
                    "Sản phẩm không tồn tại: " + d.getProductId()
                );
            }

            InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                branch.getId(),
                product.getId()
            );
            if (inv == null) {
                return new InvalidResponse<>(
                    "Kho không tồn tại cho sản phẩm: " + product.getName()
                );
            }

            if (inv.getQuantity() < d.getQuantity()) {
                return new InvalidResponse<>(
                    "Sản phẩm không đủ tồn kho: " + product.getName()
                );
            }

            BigDecimal unitPrice = BigDecimal.valueOf(product.getSellPrice());
            InvoiceDetailModel detail = new InvoiceDetailModel(
                product,
                draft,
                d.getQuantity(),
                unitPrice
            );

            details.add(detail);
            total = total.add(detail.getTotal());
        }

        BigDecimal discount = BigDecimalUtil.safe(req.getDiscount());
        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            return new InvalidResponse<>("Giảm giá không thể âm");
        }
        if (discount.compareTo(total) > 0) {
            return new InvalidResponse<>(
                "Giảm giá không thể lớn hơn tổng tiền"
            );
        }

        draft.setDetails(details);
        draft.setTotal(total.subtract(discount));

        draftCache.addDraft(draft.getId(), draft);

        return new SuccessResponse<>(
            "Tạo hóa đơn thành công",
            mapper.toDTO(draft)
        );
    }

    // =====================================================================
    // XÁC NHẬN THANH TOÁN — CHỈ ở đây mới TRỪ TỒN KHO
    // =====================================================================
    public ResponseDTO<InvoiceInfo> confirmInvoice(String invoiceId) {
        InvoiceModel draft = draftCache.confirmDraft(invoiceId);
        if (draft == null) {
            return new NotFoundResponse<>("Hóa đơn không tồn tại");
        }

        if (draft.getStatus() != InvoiceStatus.PENDING) {
            return new InvalidResponse<>(
                "Hóa đơn không ở trạng thái chờ xử lý"
            );
        }

        // ---- CHECK & TRỪ TỒN KHO ----
        for (InvoiceDetailModel d : draft.getDetails()) {
            InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                draft.getBranch().getId(),
                d.getProduct().getId()
            );

            if (inv == null) {
                draftCache.addDraft(draft.getId(), draft);
                return new InvalidResponse<>(
                    "Kho không tồn tại cho sản phẩm: " +
                        d.getProduct().getName()
                );
            }

            if (inv.getQuantity() < d.getQuantity()) {
                draftCache.addDraft(draft.getId(), draft);
                return new InvalidResponse<>(
                    "Không đủ tồn kho: " + d.getProduct().getName()
                );
            }

            // ===== TRỪ TỒN KHO CHÍNH XÁC =====
            inv.setQuantity(inv.getQuantity() - d.getQuantity());
            inventoryRepo.update(inv);
        }

        // ---- LƯU HÓA ĐƠN HOÀN TẤT ----
        draft.setStatus(InvoiceStatus.COMPLETED);
        invoiceRepo.save(draft);

        return new SuccessResponse<>(
            "Xác nhận thanh toán thành công",
            mapper.toDTO(draft)
        );
    }

    // =====================================================================
    // HỦY HÓA ĐƠN — HOÀN TỒN KHO
    // =====================================================================
    public ResponseDTO<InvoiceInfo> cancelInvoice(String invoiceId) {
        InvoiceModel draft = draftCache.getDraft(invoiceId);
        if (draft != null) {
            // hoàn kho draft
            for (InvoiceDetailModel d : draft.getDetails()) {
                InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                    draft.getBranch().getId(),
                    d.getProduct().getId()
                );
                if (inv != null) {
                    inv.setQuantity(inv.getQuantity() + d.getQuantity());
                    inventoryRepo.update(inv);
                }
            }
            draftCache.confirmDraft(invoiceId);
            return new SuccessResponse<>(
                "Hủy hóa đơn thành công",
                mapper.toDTO(draft)
            );
        }

        InvoiceModel invoice = invoiceRepo.findById(invoiceId);
        if (invoice == null) {
            return new NotFoundResponse<>("Hóa đơn không tồn tại");
        }

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            return new InvalidResponse<>("Hóa đơn đã bị hủy");
        }

        if (invoice.getStatus() == InvoiceStatus.COMPLETED) {
            return new InvalidResponse<>("Không thể hủy hóa đơn đã xác nhận");
        }

        // hoàn kho hóa đơn DB
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

        invoice.setStatus(InvoiceStatus.CANCELLED);
        InvoiceModel updated = invoiceRepo.update(invoice);

        return new SuccessResponse<>(
            "Hủy hóa đơn thành công",
            mapper.toDTO(updated)
        );
    }

    // =====================================================================
    // UPDATE DRAFT — KHÔNG trừ kho
    // =====================================================================
    public ResponseDTO<InvoiceInfo> updateInvoice(InvoiceRequestDTO req) {
        if (!req.validForUpdate()) {
            return new InvalidResponse<>("Thiếu ID hóa đơn");
        }

        InvoiceModel draft = draftCache.getDraft(req.getInvoiceId());
        if (draft == null) {
            return new NotFoundResponse<>("Hóa đơn draft không tồn tại");
        }

        // Không trừ kho ở update draft !!!
        // Chỉ check thôi.
        if (req.getBranchId() != null) {
            draft.setBranch(branchRepo.findById(req.getBranchId()));
        }
        if (req.getCustomerId() != null) {
            draft.setCustomer(customerRepo.findById(req.getCustomerId()));
        }
        if (req.getEmployeeId() != null) {
            draft.setEmployee(employeeRepo.findById(req.getEmployeeId()));
        }
        if (req.getNote() != null) {
            draft.setNote(req.getNote());
        }

        if (req.getDiscount() != null) {
            BigDecimal d = BigDecimalUtil.safe(req.getDiscount());
            if (d.compareTo(BigDecimal.ZERO) < 0) {
                return new InvalidResponse<>("Giảm giá không thể âm");
            }
            draft.setDiscount(d);
        }

        if (req.getDetails() != null && !req.getDetails().isEmpty()) {
            List<InvoiceDetailModel> newDetails = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (InvoiceRequestDTO.InvoiceDetailRequest d : req.getDetails()) {
                ProductModel product = productRepo.findById(d.getProductId());
                if (product == null) {
                    return new NotFoundResponse<>("Sản phẩm không tồn tại");
                }

                InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                    draft.getBranch().getId(),
                    product.getId()
                );

                if (inv == null || inv.getQuantity() < d.getQuantity()) {
                    return new InvalidResponse<>(
                        "Không đủ tồn kho: " + product.getName()
                    );
                }

                // KHÔNG TRỪ KHO — draft không bao giờ trừ kho
                BigDecimal unitPrice = BigDecimal.valueOf(
                    product.getSellPrice()
                );
                InvoiceDetailModel detail = new InvoiceDetailModel(
                    product,
                    draft,
                    d.getQuantity(),
                    unitPrice
                );

                newDetails.add(detail);
                total = total.add(detail.getTotal());
            }

            draft.setDetails(newDetails);
            draft.setTotal(
                total.subtract(BigDecimalUtil.safe(draft.getDiscount()))
            );
        }

        draftCache.addDraft(draft.getId(), draft);

        return new SuccessResponse<>(
            "Cập nhật hóa đơn draft thành công",
            mapper.toDTO(draft)
        );
    }

    // =====================================================================
    // DELETE
    // =====================================================================
    public ResponseDTO<InvoiceInfo> deleteInvoice(String invoiceId) {
        if (invoiceId == null || invoiceId.isBlank()) {
            return new InvalidResponse<>("Thiếu ID hóa đơn");
        }

        InvoiceModel deleted = invoiceRepo.delete(invoiceId);
        if (deleted == null) {
            return new NotFoundResponse<>("Hóa đơn không tồn tại");
        }

        return new SuccessResponse<>("OK", mapper.toDTO(deleted));
    }

    // =====================================================================
    // GET BY ID / CUSTOMER / BRANCH / EMPLOYEE
    // =====================================================================
    public ResponseDTO<InvoiceInfo> getInvoiceById(String invoiceId) {
        InvoiceModel invoice = invoiceRepo.findById(invoiceId);
        if (invoice == null) {
            return new NotFoundResponse<>("Hóa đơn không tồn tại");
        }
        return new SuccessResponse<>(
            "Lấy hóa đơn thành công",
            mapper.toDTO(invoice)
        );
    }

    public ResponseDTO<List<InvoiceInfo>> getInvoiceByCustomer(
        String customerId
    ) {
        List<InvoiceModel> list = invoiceRepo.findByCustomer(customerId);
        return new SuccessResponse<>(
            "Lấy hóa đơn theo khách hàng thành công",
            mapper.toDTOList(list)
        );
    }

    public ResponseDTO<List<InvoiceInfo>> getInvoiceByBranch(Integer branchId) {
        List<InvoiceModel> list = invoiceRepo.findByBranch(branchId);
        return new SuccessResponse<>(
            "Lấy hóa đơn theo chi nhánh thành công",
            mapper.toDTOList(list)
        );
    }

    public ResponseDTO<List<InvoiceInfo>> getInvoiceByEmployee(
        String employeeId
    ) {
        List<InvoiceModel> list = invoiceRepo.findByEmployee(employeeId);
        return new SuccessResponse<>(
            "Lấy hóa đơn theo nhân viên thành công",
            mapper.toDTOList(list)
        );
    }
}
