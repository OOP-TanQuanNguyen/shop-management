package edu.ptithcm.repository.loyalty;

import java.sql.SQLException;
import edu.ptithcm.model.LoyaltyModel;

public interface LoyaltyRepository {
    LoyaltyModel findByCustomer(String customerId) throws SQLException;
    void updatePoints(String customerId, int delta) throws SQLException;
}
