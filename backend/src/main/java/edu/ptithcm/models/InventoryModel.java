package edu.ptithcm.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(
    name = "inventory",
    uniqueConstraints = @UniqueConstraint(
        columnNames = { "product_id", "branch_id" }
    )
)
public class InventoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchModel branch;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    private int quantity;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

    // -------------------- Getter & Setter --------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BranchModel getBranch() {
        return branch;
    }

    public void setBranch(BranchModel branch) {
        this.branch = branch;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // -------------------- toString (optional for logging) --------------------
    @Override
    public String toString() {
        return (
            "InventoryModel{" +
            "id=" +
            id +
            ", branch=" +
            (branch != null ? branch.getId() : null) +
            ", product=" +
            (product != null ? product.getId() : null) +
            ", quantity=" +
            quantity +
            ", createdAt=" +
            createdAt +
            ", updatedAt=" +
            updatedAt +
            '}'
        );
    }
}
