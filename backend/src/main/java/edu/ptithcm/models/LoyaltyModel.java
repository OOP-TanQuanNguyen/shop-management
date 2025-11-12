package edu.ptithcm.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "loyalty")
public class LoyaltyModel {

    @Id
    @Column(name = "loyalty_id", length = 36)
    private String id;

    @OneToOne
    @JoinColumn(name = "customer_id", unique = true)
    private CustomerModel customer;

    @Column(name = "total_points")
    private int totalPoints = 0;

    @Column(name = "last_update")
    private Timestamp lastUpdate;

    // --- Constructors ---
    public LoyaltyModel() {}

    public LoyaltyModel(String id, CustomerModel customer, int totalPoints, Timestamp lastUpdate) {
        this.id = id;
        this.customer = customer;
        this.totalPoints = totalPoints;
        this.lastUpdate = lastUpdate;
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public CustomerModel getCustomer() { return customer; }
    public void setCustomer(CustomerModel customer) { this.customer = customer; }
    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    public Timestamp getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }

    @Override
    public String toString() {
        return "LoyaltyModel{" +
                "id='" + id + '\'' +
                ", totalPoints=" + totalPoints +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
