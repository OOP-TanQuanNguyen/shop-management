package edu.ptithcm.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class ProductModel {

    @Id
    @Column(name = "product_id", length = 36)
    private String id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryModel category;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "sell_price")
    private Double sellPrice;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToMany(mappedBy = "product")
    private List<InvoiceDetailModel> invoiceDetails;

    @OneToMany(mappedBy = "product")
    private List<InventoryModel> inventories;

    // --- Constructors ---
    public ProductModel() {}

    private ProductModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.category = builder.category;
        this.costPrice = builder.costPrice;
        this.sellPrice = builder.sellPrice;
        this.expiryDate = builder.expiryDate;
        this.isActive = builder.isActive;
        this.createdAt = builder.createdAt;
        this.invoiceDetails = builder.invoiceDetails;
        this.inventories = builder.inventories;
    }

    // --- Builder ---
    public static class Builder {
        private String id;
        private String name;
        private CategoryModel category;
        private Double costPrice;
        private Double sellPrice;
        private Date expiryDate;
        private boolean isActive;
        private Timestamp createdAt;
        private List<InvoiceDetailModel> invoiceDetails;
        private List<InventoryModel> inventories;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder category(CategoryModel category) { this.category = category; return this; }
        public Builder costPrice(Double costPrice) { this.costPrice = costPrice; return this; }
        public Builder sellPrice(Double sellPrice) { this.sellPrice = sellPrice; return this; }
        public Builder expiryDate(Date expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public Builder createdAt(Timestamp createdAt) { this.createdAt = createdAt; return this; }
        public Builder invoiceDetails(List<InvoiceDetailModel> invoiceDetails) { this.invoiceDetails = invoiceDetails; return this; }
        public Builder inventories(List<InventoryModel> inventories) { this.inventories = inventories; return this; }

        public ProductModel build() { return new ProductModel(this); }
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CategoryModel getCategory() { return category; }
    public void setCategory(CategoryModel category) { this.category = category; }

    public Double getCostPrice() { return costPrice; }
    public void setCostPrice(Double costPrice) { this.costPrice = costPrice; }

    public Double getSellPrice() { return sellPrice; }
    public void setSellPrice(Double sellPrice) { this.sellPrice = sellPrice; }

    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<InvoiceDetailModel> getInvoiceDetails() { return invoiceDetails; }
    public void setInvoiceDetails(List<InvoiceDetailModel> invoiceDetails) { this.invoiceDetails = invoiceDetails; }

    public List<InventoryModel> getInventories() { return inventories; }
    public void setInventories(List<InventoryModel> inventories) { this.inventories = inventories; }

    // --- toString ---
    @Override
    public String toString() {
        return "ProductModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", costPrice=" + costPrice +
                ", sellPrice=" + sellPrice +
                ", expiryDate=" + expiryDate +
                ", isActive=" + isActive +
                '}';
    }
}
