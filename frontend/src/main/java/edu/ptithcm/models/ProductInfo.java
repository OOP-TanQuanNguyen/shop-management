package edu.ptithcm.models;

import java.util.Map;

/**
 * Model ProductInfo - nhận dữ liệu từ Backend
 */
public class ProductInfo {

    private String id;
    private String name;
    private String categoryId;
    private String categoryName;
    private Double costPrice;
    private Double sellPrice;
    private String expiryDate; // yyyy-MM-dd format
    private Boolean isActive;
    private String createdAt;

    // ============================================================
    // Constructors
    // ============================================================
    public ProductInfo() {
    }

    /**
     * Constructor từ Map (dữ liệu từ Backend)
     */
    public ProductInfo(Map<String, Object> data) {
        this.id = (String) data.get("productId");
        this.name = (String) data.get("name");
        this.categoryId = (String) data.get("categoryId");
        this.categoryName = (String) data.get("categoryName");

        // Handle prices
        Object costObj = data.get("costPrice");
        this.costPrice = costObj != null ? ((Number) costObj).doubleValue() : 0.0;

        Object sellObj = data.get("sellPrice");
        this.sellPrice = sellObj != null ? ((Number) sellObj).doubleValue() : 0.0;

        this.expiryDate = (String) data.get("expiryDate");

        Object activeObj = data.get("isActive");
        this.isActive = activeObj != null ? (Boolean) activeObj : true;

        this.createdAt = (String) data.get("createdAt");
    }

    // ============================================================
    // Getters & Setters
    // ============================================================
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // ============================================================
    // Helper Methods
    // ============================================================
    public String getStatusText() {
        return Boolean.TRUE.equals(isActive) ? "Đang bán" : "Ngừng bán";
    }

    public Double getProfit() {
        if (sellPrice == null || costPrice == null) {
            return 0.0;
        }
        return sellPrice - costPrice;
    }

    public Double getProfitMargin() {
        if (sellPrice == null || costPrice == null || costPrice == 0) {
            return 0.0;
        }
        return ((sellPrice - costPrice) / costPrice) * 100;
    }

    // ============================================================
    // toString
    // ============================================================
    @Override
    public String toString() {
        return "ProductInfo{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", categoryName='" + categoryName + '\''
                + ", costPrice=" + costPrice
                + ", sellPrice=" + sellPrice
                + ", isActive=" + isActive
                + '}';
    }
}
