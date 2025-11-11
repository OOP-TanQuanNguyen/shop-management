package edu.ptithcm.repository.inventory;

import java.sql.SQLException;
import java.util.List;
import edu.ptithcm.model.InventoryModel;

public interface InventoryRepository {
    void create(InventoryModel inv) throws SQLException;
    void updateQuantity(int branchId, String productId, int newQty) throws SQLException;
    InventoryModel findByProduct(String productId, int branchId) throws SQLException;
    List<InventoryModel> getAllByBranch(int branchId) throws SQLException;
    void remove(int id) throws SQLException;
}
