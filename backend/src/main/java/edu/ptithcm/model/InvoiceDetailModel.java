package edu.ptithcm.model;

public class InvoiceDetailModel {
    private String productId;
    private String invoiceId;
    private int quantity;
    private double unitPrice;
    private double total;

    private InvoiceDetailModel(Builder b) {
        this.productId = b.productId;
        this.invoiceId = b.invoiceId;
        this.quantity = b.quantity;
        this.unitPrice = b.unitPrice;
        this.total = b.quantity * b.unitPrice; // auto compute
    }

    // ✅ Builder pattern
    public static class Builder {
        private String productId;
        private String invoiceId;
        private int quantity;
        private double unitPrice;

        public Builder product(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder invoice(String invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder unitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public InvoiceDetailModel build() {
            return new InvoiceDetailModel(this);
        }
    }

    // ✅ Getter + Setter (nếu cần chỉnh sửa runtime)
    public String getProductId() { return this.productId; }
    public String getInvoiceId() { return this.invoiceId; }
    public int getQuantity() { return this.quantity; }
    public double getUnitPrice() { return this.unitPrice; }
    public double getTotal() { return this.total; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.total = this.quantity * this.unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        this.total = this.quantity * this.unitPrice;
    }

    @Override
    public String toString() {
        return "InvoiceDetailModel{" +
                "productId='" + productId + '\'' +
                ", invoiceId='" + invoiceId + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", total=" + total +
                '}';
    }
}
