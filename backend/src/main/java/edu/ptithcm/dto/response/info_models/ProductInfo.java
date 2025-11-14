package edu.ptithcm.dto.response.info_models;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductInfo {
    private final String productId;
    private final String name;
    private final String categoryId;
    private String categoryName;
    private final double costPrice;
    private final double sellPrice;
    private final Date expiryDate;
    private final Boolean isActive;

    // ==========================
    // Constructor private
    // ==========================
    private ProductInfo(Builder b) {
        this.productId = b.productId;
        this.name = b.name;
        this.categoryId = b.categoryId;
        this.categoryName = b.categoryName;
        this.costPrice = b.costPrice;
        this.sellPrice = b.sellPrice;
        this.expiryDate = b.expiryDate;
        this.isActive = b.isActive;
    }

    // ==========================
    // Getters
    // ==========================
    public String getProductId() { return this.productId; }
    public String getName() { return this.name; }
    public String getCategoryId() { return this.categoryId; }
    public String getCategoryName() { return this.categoryName; }
    public double getCostPrice() { return this.costPrice; }
    public double getSellPrice() { return this.sellPrice; }
    public Date getExpiryDate() { return this.expiryDate; }
    public Boolean getIsActive() { return this.isActive; }

    // ==========================
    // Map converter
    // ==========================
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", this.productId);
        map.put("name", this.name);
        map.put("categoryId", this.categoryId);
        map.put("costPrice", this.costPrice);
        map.put("sellPrice", this.sellPrice);
        map.put("expiryDate", this.expiryDate);
        map.put("isActive", this.isActive);
        return map;
    }

    public static class Builder {
        private String productId;
        private String name;
        private String categoryId;
        private String categoryName;
        private double costPrice;
        private double sellPrice;
        private Date expiryDate;
        private Boolean isActive;

        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder categoryId(String categoryId) { this.categoryId = categoryId; return this; }
        public Builder categoryName(String categoryName) { this.categoryName = categoryName; return this; } 
        public Builder costPrice(double costPrice) { this.costPrice = costPrice; return this; }
        public Builder sellPrice(double sellPrice) { this.sellPrice = sellPrice; return this; }
        public Builder expiryDate(Date expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }

        public ProductInfo build() {
            return new ProductInfo(this);
        }
    }
}
