package edu.ptithcm.model;

import java.util.Date;
import java.util.UUID;

public class ProductModel {

    private String productId;
    private String name;
    private String categoryId;
    private String category;
    private double costPrice;
    private double sellPrice;
    private Date expiryDate;
    private Boolean isActive;

    // 🔹 Constructor rỗng cho ORM / mapper
    public ProductModel() {}

    // 🔹 Constructor từ Builder
    public ProductModel(Builder builder) {
        this.productId = builder.productId;
        this.name = builder.name;
        this.categoryId = builder.categoryId;
        this.category = builder.category;
        this.costPrice = builder.costPrice;
        this.sellPrice = builder.sellPrice;
        this.expiryDate = builder.expiryDate;
        this.isActive = builder.isActive;
    }

    // ===========================
    // 🔹 GETTERS
    // ===========================
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategoryId() { return categoryId; }
    public String getCategory() { return category; }
    public double getCostPrice() { return costPrice; }
    public double getSellPrice() { return sellPrice; }
    public Date getExpiryDate() { return expiryDate; }
    public Boolean getIsActive() { return isActive; }

    // ===========================
    // 🔹 SETTERS
    // ===========================
    public void setProductId(String productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public void setCategory(String category) { this.category = category; }
    public void setCostPrice(double costPrice) { this.costPrice = costPrice; }
    public void setSellPrice(double sellPrice) { this.sellPrice = sellPrice; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    // ===========================
    // 🔹 BUILDER
    // ===========================
    public static class Builder {
        private String productId = UUID.randomUUID().toString();
        private String name;
        private String categoryId;
        private String category;
        private double costPrice;
        private double sellPrice;
        private Date expiryDate;
        private Boolean isActive = true;

        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder categoryId(String categoryId) { this.categoryId = categoryId; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder costPrice(double costPrice) { this.costPrice = costPrice; return this; }
        public Builder sellPrice(double sellPrice) { this.sellPrice = sellPrice; return this; }
        public Builder expiryDate(Date expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public ProductModel build() {
            return new ProductModel(this);
        }
    }

    @Override
    public String toString() {
        return "ProductModel {" +
                "\n  productId='" + productId + '\'' +
                ",\n  name='" + name + '\'' +
                ",\n  categoryId='" + categoryId + '\'' +
                ",\n  category='" + category + '\'' +
                ",\n  costPrice=" + costPrice +
                ",\n  sellPrice=" + sellPrice +
                ",\n  expiryDate=" + expiryDate +
                ",\n  isActive=" + isActive +
                "\n}";
    }
}
