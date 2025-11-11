package edu.ptithcm.dto.response;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductInfo {
    private final String productId;
    private final String name;
    private final String categoryId;
    private final double costPrice;
    private final double sellPrice;
    private final Date expiryDate;
    private final Boolean isActive;

    public ProductInfo(String productId, String name, String categoryId, double costPrice, double sellPrice, Date expiryDate, Boolean isActive) {
        this.productId = productId;
        this.name = name;
        this.categoryId = categoryId;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
    }

    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategoryId() { return categoryId; }
    public double getCostPrice() { return costPrice; }
    public double getSellPrice() { return sellPrice; }
    public Date getExpiryDate() { return expiryDate; }
    public Boolean getIsActive() { return isActive; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("name", name);
        map.put("categoryId", categoryId);
        map.put("costPrice", costPrice);
        map.put("sellPrice", sellPrice);
        map.put("expiryDate", expiryDate);
        map.put("isActive", isActive);
        return map;
    }
}
