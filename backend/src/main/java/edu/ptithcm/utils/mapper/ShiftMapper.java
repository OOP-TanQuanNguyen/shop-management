package edu.ptithcm.utils.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.ptithcm.dto.response.info_models.ShiftInfo;
import edu.ptithcm.models.ShiftModel;

public final class ShiftMapper implements BaseMapper<ShiftModel, ShiftInfo> {

    public ShiftMapper() {}

    @Override
    public ShiftInfo toDTO(ShiftModel s) {
        if (s == null) return null;
        return new ShiftInfo.Builder()
                .id(s.getId())
                .name(s.getName())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .build();
    }

    @Override
    public List<ShiftInfo> toDTOList(List<ShiftModel> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
