package edu.ptithcm.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.ptithcm.utils.DraftCacheUtil;
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

    // Cache 5 phút
    private static final DraftCacheUtil<InvoiceModel> draftCache = new DraftCacheUtil<>(
            5 * 60 * 1000,
            draft -> {
                System.out.println("[DRAFT-EXPIRE] Draft expired → Rollback inventory!");
                if (draft.getDetails() != null) {
                    for (InvoiceDetailModel d : draft.getDetails()) {
                        InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                                draft.getBranch().getId(),
                                d.getProduct().getId()
                        );

                        System.out.println("[ROLLBACK] Product = " + d.getProduct().getName()
                                + " | + Quantity = " + d.getQuantity());

                        if (inv != null) {
                            inv.setQuantity(inv.getQuantity() + d.getQuantity());
                            inventoryRepo.update(inv);
                        }
                    }
                }
            }
    );

    // =====================================================================
    // GET ALL
    // =====================================================================
    public ResponseDTO<List<InvoiceInfo>> getAllInvoices() {
        System.out.println("\n[INVOICE] GET ALL");
        List<InvoiceModel> list = invoiceRepo.findAll();
        return new SuccessResponse<>("Lấy tất cả hóa đơn thành công", mapper.toDTOList(list));
    }

    // =====================================================================
    // CREATE INVOICE DRAFT – FULL DEBUG
    // =====================================================================
    public ResponseDTO<InvoiceInfo> createInvoice(InvoiceRequestDTO req) {

        System.out.println("\n\n======================= [DEBUG] CREATE INVOICE =======================");
        System.out.println("[REQ] = " + req);

        try {
            if (req == null) {
                System.out.println("[ERROR] req is null");
                return new InvalidResponse<>("Dữ liệu hóa đơn trống");
            }

            System.out.println("[DEBUG] Validate create request...");
            if (!req.validForCreate()) {
                System.out.println("[ERROR] req.validForCreate() = false");
                return new InvalidResponse<>("Dữ liệu hóa đơn không hợp lệ");
            }

            // EMPLOYEE
            System.out.println("[DEBUG] EmployeeID = " + req.getEmployeeId());
            EmployeeModel employee = employeeRepo.findById(req.getEmployeeId());
            System.out.println("[DEBUG] EmployeeModel = " + employee);

            if (employee == null) {
                return new NotFoundResponse<>("Nhân viên không tồn tại");
            }

            // BRANCH
            System.out.println("[DEBUG] BranchID = " + req.getBranchId());
            BranchModel branch = branchRepo.findById(req.getBranchId());
            System.out.println("[DEBUG] BranchModel = " + branch);

            if (branch == null) {
                return new NotFoundResponse<>("Chi nhánh không tồn tại");
            }

            // CHECK EMPLOYEE – BRANCH MAPPING
            System.out.println("[DEBUG] Checking employee-branch relation...");
            if (!employee.getBranch().getId().equals(branch.getId())) {
                System.out.println("[ERROR] Employee does not belong to branch!");
                return new InvalidResponse<>("Nhân viên không thuộc chi nhánh này");
            }

            // CUSTOMER
            CustomerModel customer = null;
            if (req.getCustomerId() != null) {
                System.out.println("[DEBUG] CustomerID = " + req.getCustomerId());
                customer = customerRepo.findById(req.getCustomerId());
                System.out.println("[DEBUG] CustomerModel = " + customer);

                if (customer == null) {
                    return new NotFoundResponse<>("Khách hàng không tồn tại");
                }
            }

            // ---------------- CREATE DRAFT ----------------
            System.out.println("[DEBUG] Creating draft...");
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

            System.out.println("[DEBUG] DraftID = " + draft.getId());

            List<InvoiceDetailModel> details = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            // ================= PROCESS ALL DETAILS =================
            for (InvoiceRequestDTO.InvoiceDetailRequest d : req.getDetails()) {

                System.out.println("---------- PROCESS DETAIL ----------");
                System.out.println("[DETAIL] productId = " + d.getProductId());
                System.out.println("[DETAIL] quantity = " + d.getQuantity());

                if (d.getQuantity() <= 0) {
                    return new InvalidResponse<>("Số lượng sản phẩm phải > 0");
                }

                ProductModel product = productRepo.findById(d.getProductId());
                System.out.println("[DEBUG] ProductModel = " + product);

                if (product == null) {
                    return new NotFoundResponse<>("Sản phẩm không tồn tại: " + d.getProductId());
                }

                InventoryModel inv = inventoryRepo.findByBranchAndProduct(branch.getId(), product.getId());
                System.out.println("[DEBUG] InventoryModel = " + inv);

                if (inv == null) {
                    return new InvalidResponse<>("Kho không tồn tại cho sản phẩm: " + product.getName());
                }

                if (inv.getQuantity() < d.getQuantity()) {
                    System.out.println("[ERROR] Inventory not enough! inv=" + inv.getQuantity());
                    return new InvalidResponse<>("Sản phẩm không đủ tồn kho: " + product.getName());
                }

                // Subtract temporary inventory
                System.out.println("[DEBUG] Subtract inventory: " + inv.getQuantity() + " -> " + (inv.getQuantity() - d.getQuantity()));
                inv.setQuantity(inv.getQuantity() - d.getQuantity());
                inventoryRepo.update(inv);

                // Create detail
                BigDecimal unitPrice = BigDecimal.valueOf(product.getSellPrice());
                InvoiceDetailModel detail = new InvoiceDetailModel(product, draft, d.getQuantity(), unitPrice);

                details.add(detail);
                total = total.add(detail.getTotal());

                System.out.println("[DEBUG] Detail total = " + detail.getTotal());
            }

            // DISCOUNT
            BigDecimal discount = BigDecimalUtil.safe(req.getDiscount());
            System.out.println("[DEBUG] Discount = " + discount);

            if (discount.compareTo(BigDecimal.ZERO) < 0) {
                return new InvalidResponse<>("Giảm giá không thể âm");
            }

            if (discount.compareTo(total) > 0) {
                return new InvalidResponse<>("Giảm giá không thể lớn hơn tổng tiền");
            }

            draft.setDetails(details);
            draft.setTotal(total.subtract(discount));

            System.out.println("[DEBUG] FINAL TOTAL = " + draft.getTotal());

            // Save draft to cache
            System.out.println("[DEBUG] Save draft to cache...");
            draftCache.addDraft(draft.getId(), draft);

            System.out.println("[SUCCESS] Draft created!");
            System.out.println("==================================================================");

            return new SuccessResponse<>("Tạo hóa đơn thành công", mapper.toDTO(draft));

        } catch (Exception e) {
            System.out.println("[EXCEPTION] createInvoice() has thrown exception:");
            e.printStackTrace();
            return new InvalidResponse<>("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // =====================================================================
    // CONFIRM INVOICE
    // =====================================================================
    public ResponseDTO<InvoiceInfo> confirmInvoice(String invoiceId) {

        System.out.println("\n[DEBUG] CONFIRM INVOICE: " + invoiceId);

        InvoiceModel draft = draftCache.confirmDraft(invoiceId);

        if (draft == null) {
            System.out.println("[ERROR] Draft not found in cache");
            return new NotFoundResponse<>("Hóa đơn không tồn tại");
        }

        if (draft.getStatus() != InvoiceStatus.PENDING) {
            return new InvalidResponse<>("Hóa đơn không ở trạng thái chờ xử lý");
        }

        // Final check inventory (safe check)
        for (InvoiceDetailModel d : draft.getDetails()) {

            InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                    draft.getBranch().getId(),
                    d.getProduct().getId()
            );

            System.out.println("[DEBUG] Final check inventory: " + inv);

            if (inv == null || inv.getQuantity() < 0) {
                draftCache.addDraft(draft.getId(), draft);
                return new InvalidResponse<>("Sản phẩm không đủ tồn kho: " + d.getProduct().getName());
            }
        }

        draft.setStatus(InvoiceStatus.COMPLETED);
        invoiceRepo.save(draft);

        System.out.println("[SUCCESS] Invoice confirmed!");

        return new SuccessResponse<>("Xác nhận thanh toán thành công", mapper.toDTO(draft));
    }

    // =====================================================================
    // CANCEL INVOICE
    // =====================================================================
    public ResponseDTO<InvoiceInfo> cancelInvoice(String invoiceId) {

        System.out.println("\n[DEBUG] CANCEL INVOICE: " + invoiceId);

        InvoiceModel draft = draftCache.getDraft(invoiceId);

        if (draft != null) {

            System.out.println("[DEBUG] Cancel draft → rollback inventory");

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

            return new SuccessResponse<>("Hủy hóa đơn thành công", mapper.toDTO(draft));
        }

        // If exists in DB
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

        return new SuccessResponse<>("Hủy hóa đơn thành công", mapper.toDTO(updated));
    }

    // =====================================================================
    // UPDATE INVOICE DRAFT
    // =====================================================================
    public ResponseDTO<InvoiceInfo> updateInvoice(InvoiceRequestDTO req) {

        System.out.println("\n[DEBUG] UPDATE INVOICE: " + req);

        if (!req.validForUpdate()) {
            return new InvalidResponse<>("Thiếu ID hóa đơn");
        }

        InvoiceModel draft = draftCache.getDraft(req.getInvoiceId());
        if (draft == null) {
            System.out.println("[ERROR] draft null in cache");
            return new NotFoundResponse<>("Hóa đơn draft không tồn tại");
        }

        // rollback old inventory
        if (draft.getDetails() != null) {
            for (InvoiceDetailModel old : draft.getDetails()) {
                InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                        draft.getBranch().getId(),
                        old.getProduct().getId()
                );
                if (inv != null) {
                    inv.setQuantity(inv.getQuantity() + old.getQuantity());
                    inventoryRepo.update(inv);
                }
            }
        }

        // update fields
        if (req.getEmployeeId() != null) {
            draft.setEmployee(employeeRepo.findById(req.getEmployeeId()));
        }
        if (req.getBranchId() != null) {
            draft.setBranch(branchRepo.findById(req.getBranchId()));
        }
        if (req.getCustomerId() != null) {
            draft.setCustomer(customerRepo.findById(req.getCustomerId()));
        }
        if (req.getNote() != null) {
            draft.setNote(req.getNote());
        }
        if (req.getDiscount() != null) {
            BigDecimal discount = BigDecimalUtil.safe(req.getDiscount());
            if (discount.compareTo(BigDecimal.ZERO) < 0) {
                return new InvalidResponse<>("Giảm giá không thể âm");
            }
            draft.setDiscount(discount);
        }

        // update details
        if (req.getDetails() != null && !req.getDetails().isEmpty()) {
            List<InvoiceDetailModel> newDetails = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (InvoiceRequestDTO.InvoiceDetailRequest d : req.getDetails()) {
                ProductModel product = productRepo.findById(d.getProductId());
                if (product == null) {
                    return new NotFoundResponse<>("Sản phẩm không tồn tại");
                }

                InventoryModel inv = inventoryRepo.findByBranchAndProduct(
                        draft.getBranch().getId(), product.getId());

                if (inv == null || inv.getQuantity() < d.getQuantity()) {
                    return new InvalidResponse<>("Sản phẩm không đủ tồn kho: " + product.getName());
                }

                inv.setQuantity(inv.getQuantity() - d.getQuantity());
                inventoryRepo.update(inv);

                BigDecimal unitPrice = BigDecimal.valueOf(product.getSellPrice());
                InvoiceDetailModel detail = new InvoiceDetailModel(product, draft, d.getQuantity(), unitPrice);

                newDetails.add(detail);
                total = total.add(detail.getTotal());
            }

            draft.setDetails(newDetails);
            draft.setTotal(total.subtract(BigDecimalUtil.safe(draft.getDiscount())));
        }

        draftCache.addDraft(draft.getId(), draft);

        return new SuccessResponse<>("Cập nhật hóa đơn draft thành công", mapper.toDTO(draft));
    }

    // =====================================================================
    // OTHER GET METHODS
    // =====================================================================
    public ResponseDTO<InvoiceInfo> getInvoiceById(String invoiceId) {
        InvoiceModel invoice = invoiceRepo.findById(invoiceId);
        if (invoice == null) {
            return new NotFoundResponse<>("Hóa đơn không tồn tại");
        }
        return new SuccessResponse<>("Lấy hóa đơn thành công", mapper.toDTO(invoice));
    }

    public ResponseDTO<List<InvoiceInfo>> getInvoiceByCustomer(String customerId) {
        return new SuccessResponse<>("OK", mapper.toDTOList(invoiceRepo.findByCustomer(customerId)));
    }

    public ResponseDTO<List<InvoiceInfo>> getInvoiceByBranch(Integer branchId) {
        return new SuccessResponse<>("OK", mapper.toDTOList(invoiceRepo.findByBranch(branchId)));
    }

    public ResponseDTO<List<InvoiceInfo>> getInvoiceByEmployee(String employeeId) {
        return new SuccessResponse<>("OK", mapper.toDTOList(invoiceRepo.findByEmployee(employeeId)));
    }

    public ResponseDTO<InvoiceInfo> deleteInvoice(String invoiceId) {
        InvoiceModel deleted = invoiceRepo.delete(invoiceId);
        if (deleted == null) {
            return new NotFoundResponse<>("Hóa đơn không tồn tại");
        }
        return new SuccessResponse<>("OK", mapper.toDTO(deleted));
    }
}
