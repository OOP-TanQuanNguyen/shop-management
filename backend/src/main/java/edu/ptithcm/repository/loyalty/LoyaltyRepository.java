package edu.ptithcm.repository.loyalty;

import edu.ptithcm.models.LoyaltyModel;
import edu.ptithcm.repository.GenericRepository;

public interface LoyaltyRepository extends GenericRepository<LoyaltyModel, String> {
    LoyaltyModel findByCustomerId(String customerId);
}
