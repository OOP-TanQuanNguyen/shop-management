package edu.ptithcm.services;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ptithcm.dto.request.employee.EmployeeRequestDTO;
import edu.ptithcm.dto.response.EmployeeInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.model.EmployeeModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.employee.EmployeeRepository;

public class EmployeeService {
    private static final EmployeeRepository employeeRepo = Repository.employee();

    public ResponseDTO<List<EmployeeInfo>> getAllEmployees() {
        try {
            List<EmployeeModel> list = employeeRepo.getAllEmployees(1000);
            return successList("EMPLOYEE_GET_ALL", "Lấy toàn bộ nhân viên", mapList(list));
        } catch (SQLException e) {
            return errorList("EMPLOYEE_GET_ALL", e);
        }
    }

    public ResponseDTO<List<EmployeeInfo>> getActiveEmployees() {
        try {
            List<EmployeeModel> list = employeeRepo.getAllEmployeesActive(1000);
            return successList("EMPLOYEE_GET_ACTIVE", "Lấy nhân viên đang hoạt động", mapList(list));
        } catch (SQLException e) {
            return errorList("EMPLOYEE_GET_ACTIVE", e);
        }
    }

    public ResponseDTO<EmployeeInfo> createEmployee(EmployeeRequestDTO req) {
        if (!req.validForCreate())
            return invalid("EMPLOYEE_CREATE", "Thiếu thông tin bắt buộc!");

        try {

            EmployeeModel model = new EmployeeModel.Builder()
                    .username(req.getUsername())
                    .name(req.getName())
                    .phone(req.getPhone())
                    .role(req.getRole())
                    .branchId(req.getBranchId())
                    .status(req.getStatus())
                    .build();

            employeeRepo.createEmployee(List.of(model));

            return success("EMPLOYEE_CREATE", "Thêm nhân viên thành công", toInfo(model));
        } catch (SQLException e) {
            return error("EMPLOYEE_CREATE", e);
        }
    }

    public ResponseDTO<EmployeeInfo> updateEmployee(EmployeeRequestDTO req) {
        if (!req.validForUpdate())
            return invalid("EMPLOYEE_UPDATE", "Thiếu ID hoặc dữ liệu cập nhật!");

        try {
            Map<String, Object> fields = new HashMap<>();
            if (req.getName() != null) fields.put("name", req.getName());
            if (req.getPhone() != null) fields.put("phone", req.getPhone());
            if (req.getRole() != null) fields.put("role", req.getRole());
            if (req.getBranchId() != null) fields.put("branch_id", req.getBranchId());
            if (req.getStatus() != null) fields.put("status", req.getStatus());

            employeeRepo.updateEmployee(req.getId(), fields);
            return success("EMPLOYEE_UPDATE", "Cập nhật nhân viên thành công", null);
        } catch (SQLException e) {
            return error("EMPLOYEE_UPDATE", e);
        }
    }

    public ResponseDTO<EmployeeInfo> deleteEmployee(EmployeeRequestDTO req) {
        if (req.getId() == null)
            return invalid("EMPLOYEE_DELETE", "Thiếu ID nhân viên!");

        try {
            employeeRepo.removeEmployee(req.getId());
            return success("EMPLOYEE_DELETE", "Xóa nhân viên thành công!", null);
        } catch (SQLException e) {
            return error("EMPLOYEE_DELETE", e);
        }
    }

    public ResponseDTO<List<EmployeeInfo>> filterEmployees(Map<String, Object> filters) {
        try {
            List<EmployeeModel> list = employeeRepo.filterEmployees(filters);
            return successList("EMPLOYEE_FILTER", "Lọc dữ liệu thành công", mapList(list));
        } catch (SQLException e) {
            return errorList("EMPLOYEE_FILTER", e);
        }
    }

    public ResponseDTO<List<EmployeeInfo>> searchEmployees(String keyword) {
        try {
            List<EmployeeModel> list = employeeRepo.searchEmployees(keyword);
            return successList("EMPLOYEE_SEARCH", "Tìm kiếm thành công", mapList(list));
        } catch (SQLException e) {
            return errorList("EMPLOYEE_SEARCH", e);
        }
    }
    private List<EmployeeInfo> mapList(List<EmployeeModel> models) {
        if (models == null) return Collections.emptyList();
        return models.stream().map(this::toInfo).collect(Collectors.toList());
    }

    private EmployeeInfo toInfo(EmployeeModel e) {
        return new EmployeeInfo(
            e.getId(),
            e.getUsername(),
            e.getName(),
            e.getPhone(),
            e.getRole(),
            e.getBranchId(),
            e.getBranch(),
            e.isStatus()
        );
    }

    private ResponseDTO<EmployeeInfo> success(String type, String msg, EmployeeInfo data) {
        return new ResponseDTO.Builder<EmployeeInfo>()
                .type(type)
                .status("SUCCESS")
                .message(msg)
                .data(data)
                .build();
    }

    private ResponseDTO<EmployeeInfo> invalid(String type, String msg) {
        return new ResponseDTO.Builder<EmployeeInfo>()
                .type(type)
                .status("INVALID")
                .message(msg)
                .data(null)
                .build();
    }

    private ResponseDTO<EmployeeInfo> error(String type, Exception e) {
        return new ResponseDTO.Builder<EmployeeInfo>()
                .type(type)
                .status("ERROR")
                .message(e.getMessage())
                .data(null)
                .build();
    }

    private ResponseDTO<List<EmployeeInfo>> successList(String type, String msg, List<EmployeeInfo> data) {
        return new ResponseDTO.Builder<List<EmployeeInfo>>()
                .type(type)
                .status("SUCCESS")
                .message(msg)
                .data(data)
                .build();
    }

    private ResponseDTO<List<EmployeeInfo>> errorList(String type, Exception e) {
        return new ResponseDTO.Builder<List<EmployeeInfo>>()
                .type(type)
                .status("ERROR")
                .message(e.getMessage())
                .data(null)
                .build();
    }
}
