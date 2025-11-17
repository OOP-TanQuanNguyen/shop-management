package edu.ptithcm.controller;

import java.util.List;
import edu.ptithcm.dto.request.loyalty.LoyaltyRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.LoyaltyInfo;
import edu.ptithcm.services.LoyaltyService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class LoyaltyController {

    private static final LoyaltyService service = new LoyaltyService();

    public ResponseDTO<LoyaltyInfo> createLoyalty(LoyaltyRequestDTO req) {
        return SafeExecutor.run(() -> service.createLoyalty(req));
    }

    public ResponseDTO<LoyaltyInfo> updateLoyalty(LoyaltyRequestDTO req) {
        return SafeExecutor.run(() -> service.updateLoyalty(req));
    }

    public ResponseDTO<LoyaltyInfo> deleteLoyalty(LoyaltyRequestDTO req) {
        return SafeExecutor.run(() -> service.deleteLoyalty(req));
    }

    public ResponseDTO<LoyaltyInfo> getLoyaltyByCustomer(LoyaltyRequestDTO req) {
        return SafeExecutor.run(() -> service.getLoyaltyByCustomer(req));
    }

    public ResponseDTO<List<LoyaltyInfo>> getAllLoyalty() {
        return SafeExecutor.run(() -> service.getAllLoyalty());
    }
}
