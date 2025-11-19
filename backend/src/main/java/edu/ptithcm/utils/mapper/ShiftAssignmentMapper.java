package edu.ptithcm.utils.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import edu.ptithcm.dto.response.info_models.ShiftAssignmentInfo;
import edu.ptithcm.models.ShiftAssignmentModel;

public final class ShiftAssignmentMapper implements BaseMapper<ShiftAssignmentModel, ShiftAssignmentInfo> {

    @Override
    public ShiftAssignmentInfo toDTO(ShiftAssignmentModel s) {
        if (s == null) return null;

        return new ShiftAssignmentInfo.Builder()
                .shiftId(s.getShift() != null ? s.getShift().getId() : null)
                .employeeId(s.getEmployee() != null ? s.getEmployee().getId() : null)
                .branchId(s.getBranch() != null ? s.getBranch().getId() : null)
                .build();
    }

    @Override
    public List<ShiftAssignmentInfo> toDTOList(List<ShiftAssignmentModel> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }
}


