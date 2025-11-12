package edu.ptithcm.repository.inventory;

import edu.ptithcm.models.InventoryModel;
import edu.ptithcm.repository.GenericRepository;
import java.util.List;

public interface InventoryRepository extends GenericRepository<InventoryModel, Integer> {
    List<InventoryModel> findByBranch(Integer branchId);
    InventoryModel findByBranchAndProduct(Integer branchId, String productId);
}
