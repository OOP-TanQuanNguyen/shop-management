package edu.ptithcm.model;

import java.sql.Timestamp;

public class InventoryModel {
    private Integer id;
    private Integer branchId;
    private String productId;
    private int quantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public InventoryModel() {}

    private InventoryModel(Builder b) {
        this.id = b.id;
        this.branchId = b.branchId;
        this.productId = b.productId;
        this.quantity = b.quantity;
        this.createdAt = b.createdAt;
        this.updatedAt = b.updatedAt;
    }

    public Integer getId() { return this.id; }
    public Integer getBranchId() { return this.branchId; }
    public String getProductId() { return this.productId; }
    public int getQuantity() { return this.quantity; }
    public Timestamp getCreatedAt() { return this.createdAt; }
    public Timestamp getUpdatedAt() { return this.updatedAt; }

    public static class Builder {
        private Integer id;
        private Integer branchId;
        private String productId;
        private int quantity;
        private Timestamp createdAt;
        private Timestamp updatedAt;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder branch(Integer b) { this.branchId = b; return this; }
        public Builder product(String p) { this.productId = p; return this; }
        public Builder quantity(int q) { this.quantity = q; return this; }
        public Builder created(Timestamp t) { this.createdAt = t; return this; }
        public Builder updated(Timestamp t) { this.updatedAt = t; return this; }
        public InventoryModel build() { return new InventoryModel(this); }
    }
}
