package edu.ptithcm.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class InvoiceInfo {

    private String invoiceId;
    private String employeeId;
    private String branchId;
    private String customerId;
    private Long createdAt;
    private BigDecimal total;
    private BigDecimal discount;
    private String note;

    private List<Map<String, Object>> details;

    public InvoiceInfo() {
    }

    public InvoiceInfo(String invoiceId, String employeeId, String branchId,
            String customerId, Long createdAt, BigDecimal total,
            BigDecimal discount, String note,
            List<Map<String, Object>> details) {

        this.invoiceId = invoiceId;
        this.employeeId = employeeId;
        this.branchId = branchId;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.total = total;
        this.discount = discount;
        this.note = note;
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
                l(map.get("createdAt")),
                bd(map.get("total")),
                bd(map.get("discount")),
                s(map.get("note")),
                (List<Map<String, Object>>) map.get("details")
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

    // GETTER + SETTER
    public String getInvoiceId() {
        return invoiceId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
}
