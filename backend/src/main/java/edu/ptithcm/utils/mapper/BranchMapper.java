package edu.ptithcm.utils.mapper;

import edu.ptithcm.dto.response.info_models.BranchInfo;
import edu.ptithcm.models.BranchModel;

public class BranchMapper implements BaseMapper<BranchModel, BranchInfo> {

    @Override
    public BranchInfo toDTO(BranchModel entity) {
        if (entity == null) return null;
        return new BranchInfo.Builder()
                .branchId(entity.getId())
                .name(entity.getName())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
    }
}
