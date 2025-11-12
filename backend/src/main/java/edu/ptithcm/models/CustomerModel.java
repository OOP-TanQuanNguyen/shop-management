package edu.ptithcm.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "customer")
public class CustomerModel {

    @Id
    @Column(name = "customer_id", length = 36)
    private String id;

    @Column(nullable = false)
    private String name;

    private String phone;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private LoyaltyModel loyalty;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<InvoiceModel> invoices;

    // --- Constructors ---
    public CustomerModel() {}

    public CustomerModel(String id, String name, String phone, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public LoyaltyModel getLoyalty() { return loyalty; }
    public void setLoyalty(LoyaltyModel loyalty) { this.loyalty = loyalty; }
    public List<InvoiceModel> getInvoices() { return invoices; }
    public void setInvoices(List<InvoiceModel> invoices) { this.invoices = invoices; }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
