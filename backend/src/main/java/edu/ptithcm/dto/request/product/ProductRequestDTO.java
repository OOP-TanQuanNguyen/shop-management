package edu.ptithcm.dto.request.product;

import java.sql.Date;
import java.util.Map;


public class ProductRequestDTO {
    private final String productId;
    private final String name;
    private final String categoryId;
    private final Double costPrice;
    private final Double sellPrice;
    private final Date expiryDate; // yyyy-MM-dd
    private final Boolean isActive;

    public ProductRequestDTO(Map<String, Object> data) {
        this.productId = (String) data.get("productId");
        this.name = (String) data.get("name");
        this.categoryId = (String) data.get("categoryId");
        this.costPrice = data.get("costPrice") != null ? (Double)data.get("costPrice") : 0;
        this.sellPrice = data.get("sellPrice") != null ? (Double)data.get("sellPrice") : 0;
        this.expiryDate = (Date)data.get("expiryDate");
        this.isActive = data.get("isActive") != null ? (Boolean) data.get("isActive") : true;
    }

    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategoryId() { return categoryId; }
    public Double getCostPrice() { return costPrice; }
    public Double getSellPrice() { return sellPrice; }
    public Date getExpiryDate() { return expiryDate; }
    public Boolean getIsActive() { return isActive; }

    // Validate trước khi tạo
    public boolean validForCreate() {
        return name != null && !name.isEmpty()
                && costPrice > 0
                && sellPrice >= costPrice;
    }

    public boolean validForUpdate() {
        return productId != null && !productId.isEmpty() && validForCreate();
    }
}
