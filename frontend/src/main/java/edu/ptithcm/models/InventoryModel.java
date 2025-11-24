package edu.ptithcm.models;

import java.util.Map;

public class InventoryModel {

    private Integer id;
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private Integer quantity;

    private String createdAt;
    private String updatedAt;

    // ============================================================
    // fromMap() – FIX kiểu dữ liệu không ổn định từ Backend
    // ============================================================
    public static InventoryModel fromMap(Map<String, Object> data) {
        InventoryModel m = new InventoryModel();

        // ===== ID =====
        Object rawId = data.get("id");
        if (rawId instanceof Number n) {
            m.id = n.intValue();
        } else if (rawId != null) {
            try {
                m.id = Integer.valueOf(rawId.toString());
            } catch (Exception e) {
                m.id = null;
            }
        }

        // ===== Branch ID =====
        Object rawBranchId = data.get("branchId");
        if (rawBranchId instanceof Number n) {
            m.branchId = String.valueOf(n.intValue());
        } else if (rawBranchId != null) {
            m.branchId = rawBranchId.toString();
        }

        m.branchName = data.get("branchName") != null
                ? data.get("branchName").toString()
                : null;

        // ===== Product ID =====
        Object rawProductId = data.get("productId");
        if (rawProductId instanceof Number n) {
            m.productId = String.valueOf(n.intValue());
        } else if (rawProductId != null) {
            m.productId = rawProductId.toString();
        }

        m.productName = data.get("productName") != null
                ? data.get("productName").toString()
                : null;

        // ===== Quantity =====
        Object rawQty = data.get("quantity");
        if (rawQty instanceof Number n) {
            m.quantity = n.intValue();
        } else if (rawQty != null) {
            try {
                m.quantity = Integer.valueOf(rawQty.toString());
            } catch (Exception e) {
                m.quantity = 0;
            }
        } else {
            m.quantity = 0;
        }

        // ===== Timestamp =====
        m.createdAt = data.get("createdAt") != null ? data.get("createdAt").toString() : null;
        m.updatedAt = data.get("updatedAt") != null ? data.get("updatedAt").toString() : null;

        return m;
    }

    // ============================================================
    // GETTERS
    // ============================================================
    public Integer getId() {
        return id;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
