package edu.ptithcm.utils.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.ptithcm.dto.response.info_models.LoyaltyInfo;
import edu.ptithcm.models.LoyaltyModel;

final class LoyaltyMapper implements BaseMapper<LoyaltyModel, LoyaltyInfo> {

    LoyaltyMapper() {}

    @Override
    public LoyaltyInfo toDTO(LoyaltyModel l) {
        if (l == null) return null;

        return new LoyaltyInfo.Builder()
                .loyaltyId(l.getId())
                .customerId(l.getCustomer() != null ? l.getCustomer().getId() : null)
                .totalPoints(l.getTotalPoints())
                .lastUpdate(l.getLastUpdate())
                .build();
    }

    @Override
    public List<LoyaltyInfo> toDTOList(List<LoyaltyModel> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
