package edu.ptithcm.model;

import java.sql.Date;
import java.util.UUID;

public class ProductModel {
    private String id;
    private String name;
    private String categoryId;
    private String category;
    private double costPrice;
    private double sellPrice;
    private Date expiryDate;
    private boolean isActive;

    public ProductModel() {}

    private ProductModel(Builder b) {
        this.id = b.id;
        this.name = b.name;
        this.categoryId = b.categoryId;
        this.category = b.category;
        this.costPrice = b.costPrice;
        this.sellPrice = b.sellPrice;
        this.expiryDate = b.expiryDate;
        this.isActive = b.isActive;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategoryId() { return categoryId; }
    public String getCategory() { return category; }
    public double getCostPrice() { return costPrice; }
    public double getSellPrice() { return sellPrice; }
    public Date getExpiryDate() { return expiryDate; }
    public boolean isActive() { return isActive; }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private String name;
        private String categoryId;
        private String category;
        private double costPrice;
        private double sellPrice;
        private Date expiryDate;
        private boolean isActive = true;

        public Builder id(String id){this.id = id;return this;}
        public Builder name(String n) { this.name = n; return this; }
        public Builder categoryId(String c) { this.categoryId = c; return this; }
        public Builder category(String c) { this.category = c; return this; }
        public Builder costPrice(double c) { this.costPrice = c; return this; }
        public Builder sellPrice(double s) { this.sellPrice = s; return this; }
        public Builder expiryDate(Date e) { this.expiryDate = e; return this; }
        public Builder isActive(boolean a) { this.isActive = a; return this; }

        public ProductModel build() { return new ProductModel(this); }
    }

    @Override
    public String toString() { return name + " (" + sellPrice + ")"; }
}
