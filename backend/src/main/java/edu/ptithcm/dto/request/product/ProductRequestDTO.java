package edu.ptithcm.dto.request.product;

import edu.ptithcm.utils.RequestUtil;
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
        this.productId = RequestUtil.toStr(data.get("productId"));
        this.name = RequestUtil.toStr(data.get("name"));
        this.categoryId = RequestUtil.toStr(data.get("categoryId"));
        this.costPrice = RequestUtil.toDouble(data.get("costPrice"));
        this.sellPrice = RequestUtil.toDouble(data.get("sellPrice"));
        this.expiryDate = RequestUtil.toDate(data.get("expiryDate"));
        this.isActive = RequestUtil.toBool(data.get("isActive"), true);
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
