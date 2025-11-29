package edu.ptithcm.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class InvoiceInfo {

    private String invoiceId;
    private String employeeId;
    private String branchId;
    private String customerId;
    private String customerName;
    private Long createdAt;
    private BigDecimal total;
    private BigDecimal discount;
    private String note;
    private String status; // ✅ FIX: THÊM field status
    private List<Map<String, Object>> details;

    public InvoiceInfo() {
    }

    public InvoiceInfo(
            String invoiceId,
            String employeeId,
            String branchId,
            String customerId,
            String customerName,
            Long createdAt,
            BigDecimal total,
            BigDecimal discount,
            String note,
            String status,
            List<Map<String, Object>> details
    ) {
        this.invoiceId = invoiceId;
        this.employeeId = employeeId;
        this.branchId = branchId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.total = total;
        this.discount = discount;
        this.note = note;
        this.status = status; // ✅ FIX
        this.details = details;
    }

    @SuppressWarnings("unchecked")
    public static InvoiceInfo fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        return new InvoiceInfo(
                s(map.get("invoiceId")),
                s(map.get("employeeId")),
                s(map.get("branchId")),
                s(map.get("customerId")),
                s(map.get("customerName")),
                l(map.get("createdAt")),
                bd(map.get("total")),
                bd(map.get("discount")),
                s(map.get("note")),
                s(map.get("status")), // ✅ FIX: Parse status
                (List<Map<String, Object>>) map.get("details")
        );
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "invoiceId",
                invoiceId != null ? invoiceId : "",
                "employeeId",
                employeeId != null ? employeeId : "",
                "branchId",
                branchId != null ? branchId : "",
                "customerId",
                customerId != null ? customerId : "",
                "createdAt",
                createdAt != null ? createdAt : 0L,
                "total",
                total != null ? total : BigDecimal.ZERO,
                "discount",
                discount != null ? discount : BigDecimal.ZERO,
                "note",
                note != null ? note : "",
                "status",
                status != null ? status : "PENDING", // ✅ FIX
                "details",
                details != null ? details : List.of()
        );
    }

    private static String s(Object o) {
        return o != null ? o.toString() : null;
    }

    private static Long l(Object o) {
        try {
            return o == null ? null : Long.parseLong(o.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private static BigDecimal bd(Object o) {
        try {
            return o == null ? null : new BigDecimal(o.toString());
        } catch (Exception e) {
            return null;
        }
    }

    // ===================== GETTERS & SETTERS =====================
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getcustomerName() {
        return customerName;
    }

    public void setcustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // ✅ FIX: THÊM getter/setter cho status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Map<String, Object>> getDetails() {
        return details;
    }

    public void setDetails(List<Map<String, Object>> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return ("InvoiceInfo{"
                + "invoiceId='"
                + invoiceId
                + '\''
                + ", employeeId='"
                + employeeId
                + '\''
                + ", customerId='"
                + customerId
                + '\''
                + ", branchId="
                + branchId
                + ", total="
                + total
                + ", discount="
                + discount
                + ", createdAt="
                + createdAt
                + ", details="
                + details
                + '}');
    }
}
