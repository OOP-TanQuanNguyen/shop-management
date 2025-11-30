package edu.ptithcm.models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceInfo {

    private String invoiceId;
    private String employeeId;
    private String employeeName;     // NEW
    private String branchId;
    private String branchName;       // NEW
    private String customerId;
    private String customerName;
    private Long createdAt;
    private BigDecimal total;
    private BigDecimal discount;
    private String note;
    private String status;
    private List<Map<String, Object>> details;

    public InvoiceInfo() {
    }

    public InvoiceInfo(
            String invoiceId,
            String employeeId,
            String employeeName,
            String branchId,
            String branchName,
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
        this.employeeName = employeeName;
        this.branchId = branchId;
        this.branchName = branchName;
        this.customerId = customerId;
        this.customerName = customerName;
        this.createdAt = createdAt;
        this.total = total;
        this.discount = discount;
        this.note = note;
        this.status = status;
        this.details = details;
    }

    @SuppressWarnings("unchecked")
    public static InvoiceInfo fromMap(Map<String, Object> map) {
        return new InvoiceInfo(
                s(map.get("invoiceId")),
                s(map.get("employeeId")),
                s(map.get("employeeName")), // NEW
                s(map.get("branchId")),
                s(map.get("branchName")), // NEW
                s(map.get("customerId")),
                s(map.get("customerName")),
                l(map.get("createdAt")),
                bd(map.get("total")),
                bd(map.get("discount")),
                s(map.get("note")),
                s(map.get("status")),
                (List<Map<String, Object>>) map.get("details")
        );
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }   // NEW

    public String getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }       // NEW

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public String getNote() {
        return note;
    }

    public String getStatus() {
        return status;
    }

    public List<Map<String, Object>> getDetails() {
        return details;
    }

    private static String s(Object o) {
        return o == null ? null : o.toString();
    }

    private static Long l(Object o) {
        try {
            return o == null ? null : Long.parseLong(o.toString());
        } catch (Exception ex) {
            return null;
        }
    }

    private static BigDecimal bd(Object o) {
        try {
            return o == null ? null : new BigDecimal(o.toString());
        } catch (Exception ex) {
            return null;
        }
    }
}
