package edu.ptithcm.utils.mapper;

import edu.ptithcm.dto.response.info_models.InventoryInfo;
import edu.ptithcm.models.InventoryModel;
import java.util.*;
import java.util.stream.Collectors;

public class InventoryMapper
    implements BaseMapper<InventoryModel, InventoryInfo> {

    @Override
    public InventoryInfo toDTO(InventoryModel entity) {
        if (entity == null) return null;

        return new InventoryInfo.Builder()
            .id(entity.getId())
            .branchId(
                entity.getBranch() != null
                    ? String.valueOf(entity.getBranch().getId())
                    : null
            )
            .branchName(
                entity.getBranch() != null ? entity.getBranch().getName() : null
            )
            .productId(
                entity.getProduct() != null ? entity.getProduct().getId() : null
            )
            .productName(
                entity.getProduct() != null
                    ? entity.getProduct().getName()
                    : null
            )
            .quantity(entity.getQuantity())
            .build();
    }

    @Override
    public List<InventoryInfo> toDTOList(List<InventoryModel> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
