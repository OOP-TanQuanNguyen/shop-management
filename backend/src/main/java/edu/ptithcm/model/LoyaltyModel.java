package edu.ptithcm.model;


public class LoyaltyModel {
    private String id;
    private String customerId;
    private int totalPoints;

    public LoyaltyModel(String id, String customerId, int totalPoints) {
        this.id = id;
        this.customerId = customerId;
        this.totalPoints = totalPoints;
    }

    public String getId() { return this.id; }
    public String getCustomerId() { return this.customerId; }
    public int getTotalPoints() { return this.totalPoints; }
}
