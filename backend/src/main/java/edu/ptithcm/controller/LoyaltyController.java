package edu.ptithcm.controller;

import java.util.List;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.LoyaltyInfo;
import edu.ptithcm.services.LoyaltyService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class LoyaltyController {

    private static final LoyaltyService service = new LoyaltyService();

    public ResponseDTO<LoyaltyInfo> createLoyalty(String customerId) {
        return SafeExecutor.run(() -> service.createLoyalty(customerId));
    }

    public ResponseDTO<LoyaltyInfo> updateLoyalty(String customerId, int pointsChange) {
        return SafeExecutor.run(() -> service.updateLoyalty(customerId, pointsChange));
    }

    public ResponseDTO<LoyaltyInfo> deleteLoyalty(String customerId) {
        return SafeExecutor.run(() -> service.deleteLoyalty(customerId));
    }

    public ResponseDTO<LoyaltyInfo> getLoyaltyByCustomer(String customerId) {
        return SafeExecutor.run(() -> service.getLoyaltyByCustomer(customerId));
    }

    public ResponseDTO<List<LoyaltyInfo>> getAllLoyalty() {
        return SafeExecutor.run(() -> service.getAllLoyalty());
    }
}
