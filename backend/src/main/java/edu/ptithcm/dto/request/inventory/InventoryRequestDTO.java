package edu.ptithcm.dto.request.inventory;

import java.util.Map;

public class InventoryRequestDTO {
    private final Integer id;
    private final String branchId;
    private final String productId;
    private final Integer quantity;

    public InventoryRequestDTO(Map<String, Object> data) {
        this.id = data.get("id") != null ? (Integer) data.get("id") : null;
        this.branchId = data.get("branchId") != null ? data.get("branchId").toString() : null;
        this.productId = data.get("productId") != null ? data.get("productId").toString() : null;
        this.quantity = data.get("quantity") != null ? (Integer) data.get("quantity") : 0;
    }

    // Getters
    public Integer getId() { return id; }
    public String getBranchId() { return branchId; }
    public String getProductId() { return productId; }
    public Integer getQuantity() { return quantity; }

    // Validate
    public boolean validForCreate() {
        return branchId != null && !branchId.isEmpty()
                && productId != null && !productId.isEmpty()
                && quantity != null && quantity >= 0;
    }

    public boolean validForUpdate() {
        return id != null && id > 0 && validForCreate();
    }
}
