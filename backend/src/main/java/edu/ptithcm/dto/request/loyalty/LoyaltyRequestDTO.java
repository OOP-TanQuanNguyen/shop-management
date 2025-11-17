package edu.ptithcm.dto.request.loyalty;

import edu.ptithcm.utils.RequestUtil;
import java.util.Map;

public class LoyaltyRequestDTO {
    private final String loyaltyId;
    private final String customerId;
    private final Integer totalPoints;

    public LoyaltyRequestDTO(Map<String, Object> data) {
        this.loyaltyId = RequestUtil.toStr(data.get("loyaltyId"));
        this.customerId = RequestUtil.toStr(data.get("customerId"));
        this.totalPoints = RequestUtil.toInt(data.get("totalPoints"));
    }

    public String getLoyaltyId() { return loyaltyId; }
    public String getCustomerId() { return customerId; }
    public Integer getTotalPoints() { return totalPoints; }

    public boolean validForCreate() {
        return customerId != null && !customerId.isEmpty();
    }

    public boolean validForUpdate() {
        return loyaltyId != null && !loyaltyId.isEmpty() && validForCreate();
    }

    public Map<String, Object> toMap() {
        return Map.of(
            "loyaltyId", loyaltyId,
            "customerId", customerId,
            "totalPoints", totalPoints
        );
    }
}
