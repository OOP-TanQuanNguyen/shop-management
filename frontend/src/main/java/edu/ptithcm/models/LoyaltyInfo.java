package edu.ptithcm.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class LoyaltyInfo {

    private String loyaltyId;
    private String customerId;
    private int totalPoints;
    private Timestamp lastUpdate;

    public LoyaltyInfo() {
    }

    private LoyaltyInfo(Builder b) {
        this.loyaltyId = b.loyaltyId;
        this.customerId = b.customerId;
        this.totalPoints = b.totalPoints;
        this.lastUpdate = b.lastUpdate;
    }

    public static class Builder {

        private String loyaltyId;
        private String customerId;
        private int totalPoints;
        private Timestamp lastUpdate;

        public Builder loyaltyId(String loyaltyId) {
            this.loyaltyId = loyaltyId;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder totalPoints(int totalPoints) {
            this.totalPoints = totalPoints;
            return this;
        }

        public Builder lastUpdate(Timestamp lastUpdate) {
            this.lastUpdate = lastUpdate;
            return this;
        }

        public LoyaltyInfo build() {
            return new LoyaltyInfo(this);
        }
    }

    // ==========================
    // PARSE FROM MAP
    // ==========================
    public static LoyaltyInfo fromMap(Map<String, Object> map) {
        if (map == null) {
            return null;
        }

        return new Builder()
                .loyaltyId(s(map.get("loyaltyId")))
                .customerId(s(map.get("customerId")))
                .totalPoints(i(map.get("totalPoints")))
                .lastUpdate(t(map.get("lastUpdate")))
                .build();
    }

    // ==========================
    // toMap
    // ==========================
    public Map<String, Object> toMap() {
        Map<String, Object> out = new HashMap<>();
        out.put("loyaltyId", loyaltyId != null ? loyaltyId : "");
        out.put("customerId", customerId != null ? customerId : "");
        out.put("totalPoints", totalPoints);
        out.put("lastUpdate", lastUpdate != null ? lastUpdate.getTime() : 0L);
        return out;
    }

    // ==========================
    // Helpers
    // ==========================
    private static String s(Object o) {
        return o != null ? o.toString() : null;
    }

    private static int i(Object o) {
        try {
            return o == null ? 0 : Integer.parseInt(o.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    private static Timestamp t(Object o) {
        try {
            if (o == null) {
                return null;
            }
            if (o instanceof Timestamp ts) {
                return ts;
            }
            return new Timestamp(Long.parseLong(o.toString()));
        } catch (Exception e) {
            return null;
        }
    }

    // ==========================
    // Getters
    // ==========================
    public String getLoyaltyId() {
        return loyaltyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
}
