package edu.ptithcm.utils.mapper;

import edu.ptithcm.dto.response.info_models.InventoryInfo;
import edu.ptithcm.models.InventoryModel;

public class InventoryMapper implements BaseMapper<InventoryModel, InventoryInfo> {

    @Override
    public InventoryInfo toDTO(InventoryModel entity) {
        if (entity == null) return null;

        return new InventoryInfo.Builder()
                .id(entity.getId())
                .branchId(entity.getBranch() != null ? String.valueOf(entity.getBranch().getId()) : null)
                .branchName(entity.getBranch() != null ? entity.getBranch().getName() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .productName(entity.getProduct() != null ? entity.getProduct().getName() : null)
                .quantity(entity.getQuantity())
                .build();
    }
}
