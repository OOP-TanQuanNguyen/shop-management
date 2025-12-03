package edu.ptithcm.dto.request.inventory;

import edu.ptithcm.utils.RequestUtil;
import java.util.Map;

public class InventoryRequestDTO {
    private final Integer id;
    private final String branchId;
    private final String productId;
    private final Integer quantity;

    public InventoryRequestDTO(Map<String, Object> data) {
        this.id = RequestUtil.toInt(data.get("id"));
        this.branchId = RequestUtil.toStr(data.get("branchId"));
        this.productId = RequestUtil.toStr(data.get("productId"));
        this.quantity = RequestUtil.toInt(data.get("quantity"));
    }

    // Getters
    public Integer getId() { return id; }
    public String getBranchId() { return branchId; }
    public String getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }

    // Validate
    public boolean validForCreate() {
        if (branchId == null || branchId.trim().isEmpty()) return false;
        if (productId == null || productId.trim().isEmpty()) return false;

        if (quantity == null) return false;
        return quantity >= 0; 
    }

    public boolean validForUpdate() {
        if (id == null || id <= 0) return false;
        if (branchId == null) return false;
        if (productId == null) return false;
        if (quantity != null && quantity < 0) return false;
        return true;
    }

    public boolean validForDelete() {
        return id != null && id > 0;
    }
}
