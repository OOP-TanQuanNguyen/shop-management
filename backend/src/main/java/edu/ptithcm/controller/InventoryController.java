package edu.ptithcm.controller;

import edu.ptithcm.dto.request.inventory.InventoryRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.InventoryInfo;
import edu.ptithcm.services.InventoryService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;
import java.util.List;

public class InventoryController {

    private static final InventoryService service = new InventoryService();

    public ResponseDTO<InventoryInfo> createInventory(InventoryRequestDTO req) {
        return SafeExecutor.run(() -> service.createInventory(req));
    }

    public ResponseDTO<InventoryInfo> updateInventory(InventoryRequestDTO req) {
        return SafeExecutor.run(() -> service.updateInventory(req));
    }

    public ResponseDTO<InventoryInfo> deleteInventory(InventoryRequestDTO req) {
        return SafeExecutor.run(() -> service.deleteInventory(req));
    }

    public ResponseDTO<List<InventoryInfo>> getAllInventories() {
        System.out.println("GetAllInventory controller");
        return SafeExecutor.run(() -> service.getAllInventories());
    }

    public ResponseDTO<List<InventoryInfo>> getInventoriesByBranch(
        Integer branchId
    ) {
        return SafeExecutor.run(() -> service.getInventoriesByBranch(branchId));
    }
}
