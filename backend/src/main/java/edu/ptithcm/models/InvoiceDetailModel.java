package edu.ptithcm.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_detail")
@IdClass(InvoiceDetailId.class)
public class InvoiceDetailModel {

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Id
    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceModel invoice;

    private int quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "total")
    private BigDecimal total;

    // --- Constructors ---
    public InvoiceDetailModel() {}

    public InvoiceDetailModel(ProductModel product, InvoiceModel invoice, int quantity, BigDecimal unitPrice) {
        this.product = product;
        this.invoice = invoice;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.total = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    // --- Getters & Setters ---
    public ProductModel getProduct() { return product; }
    public void setProduct(ProductModel product) { this.product = product; }
    public InvoiceModel getInvoice() { return invoice; }
    public void setInvoice(InvoiceModel invoice) { this.invoice = invoice; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    @Override
    public String toString() {
        return "InvoiceDetailModel{" +
                "product=" + (product != null ? product.getId() : null) +
                ", invoice=" + (invoice != null ? invoice.getId() : null) +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
