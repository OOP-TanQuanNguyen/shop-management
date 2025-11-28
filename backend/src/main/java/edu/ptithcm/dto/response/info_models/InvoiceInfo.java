package edu.ptithcm.dto.response.info_models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceInfo {

    private final String invoiceId;
    private final String employeeId;
    private final String branchId;
    private final String customerId;
    private final String customerName;
    private final Timestamp createdAt;
    private final BigDecimal total;
    private final BigDecimal discount;
    private final String note;
    private final List<Map<String, Object>> details;

    private InvoiceInfo(Builder builder) {
        this.invoiceId = builder.invoiceId;
        this.employeeId = builder.employeeId;
        this.branchId = builder.branchId;
        this.customerId = builder.customerId;
        this.customerName = builder.customerName;
        this.createdAt = builder.createdAt;
        this.total = builder.total;
        this.discount = builder.discount;
        this.note = builder.note;
        this.details = builder.details;
    }

    // --- Convert to Map ---
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("invoiceId", invoiceId);
        map.put("employeeId", employeeId);
        map.put("branchId", branchId);
        map.put("customerId", customerId);
        map.put("createdAt", createdAt != null ? createdAt.getTime() : null);
        map.put("total", total);
        map.put("discount", discount);
        map.put("note", note);
        map.put("details", details);
        return map;
    }

    // --- Getters ---
    public String getInvoiceId() {
        return invoiceId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Timestamp getCreatedAt() {
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

    public List<Map<String, Object>> getDetails() {
        return details;
    }

    // --- Builder ---
    public static class Builder {

        private String invoiceId;
        private String employeeId;
        private String branchId;
        private String customerId;
        private String customerName;
        private Timestamp createdAt;
        private BigDecimal total;
        private BigDecimal discount;
        private String note;
        private List<Map<String, Object>> details;

        public Builder invoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public Builder employeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder branchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder total(BigDecimal total) {
            this.total = total;
            return this;
        }

        public Builder discount(BigDecimal discount) {
            this.discount = discount;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder details(List<Map<String, Object>> details) {
            this.details = details;
            return this;
        }

        public InvoiceInfo build() {
            return new InvoiceInfo(this);
        }
    }
}
