package edu.ptithcm.utils.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.ptithcm.dto.response.info_models.EmployeeInfo;
import edu.ptithcm.models.EmployeeModel;

final class EmployeeMapper implements BaseMapper<EmployeeModel, EmployeeInfo> {

    EmployeeMapper() {}
    @Override
    public EmployeeInfo toDTO(EmployeeModel e) {
        if (e == null) return null;

        return new EmployeeInfo.Builder()
                .id(e.getId())
                .username(e.getUsername())
                .name(e.getName())
                .phone(e.getPhone())
                .role(e.getRole() != null ? e.getRole().name() : null)
                .branchId(e.getBranch() != null ? e.getBranch().getId() : null)
                .branch(e.getBranch() != null ? e.getBranch().getName() : null)
                .status(e.isStatus())
                .build();
    }

    @Override
    public List<EmployeeInfo> toDTOList(List<EmployeeModel> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
