package edu.ptithcm.dto.response.info_models;

import java.util.HashMap;
import java.util.Map;

public class InventoryInfo {
    private final Integer id;
    private final String branchId;
    private String branchName;
    private final String productId;
    private String productName;
    private final int quantity;

    private InventoryInfo(Builder b) {
        this.id = b.id;
        this.branchId = b.branchId;
        this.branchName = b.branchName;
        this.productId = b.productId;
        this.productName = b.productName;
        this.quantity = b.quantity;
    }

    // Getters
    public Integer getId() { return id; }
    public String getBranchId() { return branchId; }
    public String getBranchName() { return branchName; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }

    // Map converter
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("branchId", branchId);
        map.put("branchName", branchName);
        map.put("productId", productId);
        map.put("productName", productName);
        map.put("quantity", quantity);
        return map;
    }

    public static class Builder {
        private Integer id;
        private String branchId;
        private String branchName;
        private String productId;
        private String productName;
        private int quantity;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder branchId(String branchId) { this.branchId = branchId; return this; }
        public Builder branchName(String branchName) { this.branchName = branchName; return this; }
        public Builder productId(String productId) { this.productId = productId; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder quantity(int quantity) { this.quantity = quantity; return this; }

        public InventoryInfo build() { return new InventoryInfo(this); }
    }
}
