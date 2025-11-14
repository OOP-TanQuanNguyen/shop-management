package edu.ptithcm.dto.request.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvoiceRequestDTO {

    private String invoiceId;
    private String employeeId;
    private Integer branchId;
    private String customerId;
    private BigDecimal discount;
    private String note;
    private List<InvoiceDetailRequest> details;

    // --- Inner class chi tiết ---
    public static class InvoiceDetailRequest {
        private String productId;
        private int quantity;

        public InvoiceDetailRequest() {}
        public InvoiceDetailRequest(String productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    // --- Constructors ---
    public InvoiceRequestDTO() {}

    // Constructor Map -> DTO
    public InvoiceRequestDTO(Map<String, Object> data) {
        if (data == null) return;
        this.invoiceId = (String) data.get("invoiceId");
        this.employeeId = (String) data.get("employeeId");
        this.branchId = data.get("branchId") != null ? (Integer) data.get("branchId") : null;
        this.customerId = (String) data.get("customerId");
        this.note = (String) data.get("note");
        this.discount = data.get("discount") != null ? new BigDecimal(data.get("discount").toString()) : null;

        Object detailsObj = data.get("details");
        if (detailsObj instanceof List<?>) {
            this.details = new ArrayList<>();
            for (Object o : (List<?>) detailsObj) {
                if (o instanceof Map<?, ?> m) {
                    String productId = (String) m.get("productId");
                    int quantity = m.get("quantity") != null ? ((Number) m.get("quantity")).intValue() : 0;
                    this.details.add(new InvoiceDetailRequest(productId, quantity));
                }
            }
        }
    }
    
    // --- Getters & Setters ---
    public String getInvoiceId() { return invoiceId; }
    public void setInvoiceId(String invoiceId) { this.invoiceId = invoiceId; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public Integer getBranchId() { return branchId; }
    public void setBranchId(Integer branchId) { this.branchId = branchId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public List<InvoiceDetailRequest> getDetails() { return details; }
    public void setDetails(List<InvoiceDetailRequest> details) { this.details = details; }

    // --- Validation ---
    public boolean validForCreate() {
        return employeeId != null && !employeeId.isBlank()
            && branchId != null
            && customerId != null && !customerId.isBlank()
            && details != null && !details.isEmpty();
    }

    public boolean validForUpdate() {
        return invoiceId != null && !invoiceId.isBlank();
    }
}
