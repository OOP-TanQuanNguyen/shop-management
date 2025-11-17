package edu.ptithcm.dto.response.info_models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class LoyaltyInfo {
    private final String loyaltyId;
    private final String customerId;
    private final int totalPoints;
    private final Timestamp lastUpdate;

    private LoyaltyInfo(Builder b) {
        this.loyaltyId = b.loyaltyId;
        this.customerId = b.customerId;
        this.totalPoints = b.totalPoints;
        this.lastUpdate = b.lastUpdate;
    }

    public String getLoyaltyId() { return loyaltyId; }
    public String getCustomerId() { return customerId; }
    public int getTotalPoints() { return totalPoints; }
    public Timestamp getLastUpdate() { return lastUpdate; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("loyaltyId", loyaltyId);
        map.put("customerId", customerId);
        map.put("totalPoints", totalPoints);
        map.put("lastUpdate", lastUpdate);
        return map;
    }

    public static class Builder {
        private String loyaltyId;
        private String customerId;
        private int totalPoints;
        private Timestamp lastUpdate;

        public Builder loyaltyId(String loyaltyId) { this.loyaltyId = loyaltyId; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder totalPoints(int totalPoints) { this.totalPoints = totalPoints; return this; }
        public Builder lastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; return this; }

        public LoyaltyInfo build() { return new LoyaltyInfo(this); }
    }
}
