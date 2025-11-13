package edu.ptithcm.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import edu.ptithcm.dto.request.employee.EmployeeRequestDTO;
import edu.ptithcm.dto.response.EmployeeInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.models.BranchModel;
import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.branch.BranchRepository;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.utils.CryptoUtil;

public class EmployeeService {

    private static final EmployeeRepository employeeRepo = Repository.employee();
    private static final BranchRepository branchRepo = Repository.branch();

    public ResponseDTO<List<EmployeeInfo>> getAllEmployees() {
        try {
            List<EmployeeModel> list = employeeRepo.findAll();
            return successList("EMPLOYEE_GET_ALL", "Lấy toàn bộ nhân viên thành công", mapList(list));
        } catch (Exception e) {
            return errorList("EMPLOYEE_GET_ALL", e);
        }
    }


    public ResponseDTO<List<EmployeeInfo>> getActiveEmployees() {
        try {
            List<EmployeeModel> list = employeeRepo.findActive();
            return successList("EMPLOYEE_GET_ACTIVE", "Lấy nhân viên đang hoạt động", mapList(list));
        } catch (Exception e) {
            return errorList("EMPLOYEE_GET_ACTIVE", e);
        }
    }

    // ------------------ Tạo nhân viên mới ------------------
    public ResponseDTO<EmployeeInfo> createEmployee(EmployeeRequestDTO req) {
        if (!req.validForCreate()) {
            return invalid("EMPLOYEE_CREATE", "Thiếu thông tin bắt buộc!");
        }

        try {
            // Kiểm tra trùng username
            if (employeeRepo.existsByUsername(req.getUsername())) {
                return invalid("EMPLOYEE_CREATE", "Tên đăng nhập đã tồn tại!");
            }

            BranchModel branch = null;
            if (req.getBranchId() != null)
                branch = branchRepo.findById(req.getBranchId());

            String hashPass = CryptoUtil.md5(req.getPassword());

            EmployeeModel employee = new EmployeeModel();
            employee.setId(UUID.randomUUID().toString());
            employee.setUsername(req.getUsername());
            employee.setPassword(hashPass);
            employee.setName(req.getName());
            employee.setPhone(req.getPhone());
            employee.setBranch(branch);
            employee.setRole(req.getRole());
            employee.setStatus(true);

            employeeRepo.save(employee);

            return success("EMPLOYEE_CREATE", "Tạo nhân viên thành công", toInfo(employee));

        } catch (Exception e) {
            return error("EMPLOYEE_CREATE", e);
        }
    }

    // ------------------ Cập nhật thông tin nhân viên ------------------
    public ResponseDTO<EmployeeInfo> updateEmployee(EmployeeRequestDTO req) {
        if (!req.validForUpdate())
            return invalid("EMPLOYEE_UPDATE", "Thiếu ID hoặc dữ liệu cập nhật!");

        try {
            EmployeeModel existing = employeeRepo.findById(req.getEmployeeId());
            if (existing == null)
                return error("EMPLOYEE_UPDATE", new Exception("Không tìm thấy nhân viên"));

            if (req.getName() != null) existing.setName(req.getName());
            if (req.getPhone() != null) existing.setPhone(req.getPhone());
            if (req.getRole() != null) existing.setRole(req.getRole());
            if (req.getPassword() != null && !req.getPassword().isEmpty())
                existing.setPassword(CryptoUtil.md5(req.getPassword()));

            if (req.getBranchId() != null) {
                BranchModel b = branchRepo.findById(req.getBranchId());
                existing.setBranch(b);
            }

            if (req.getStatus() != null)
                existing.setStatus(req.getStatus());

            employeeRepo.update(existing);

            return success("EMPLOYEE_UPDATE", "Cập nhật nhân viên thành công", toInfo(existing));

        } catch (Exception e) {
            return error("EMPLOYEE_UPDATE", e);
        }
    }

    // ------------------ Xóa nhân viên ------------------
    public ResponseDTO<EmployeeInfo> deleteEmployee(EmployeeRequestDTO req) {
        if (req.getEmployeeId() == null || req.getEmployeeId().isEmpty())
            return invalid("EMPLOYEE_DELETE", "Thiếu ID nhân viên!");

        try {
            EmployeeModel model = employeeRepo.findById(req.getEmployeeId());
            if (model == null)
                return error("EMPLOYEE_DELETE", new Exception("Không tìm thấy nhân viên"));

            employeeRepo.delete(model);
            return success("EMPLOYEE_DELETE", "Xóa nhân viên thành công", null);

        } catch (Exception e) {
            return error("EMPLOYEE_DELETE",e);
        }
    }

    // ------------------ Lọc nhân viên ------------------
    public ResponseDTO<List<EmployeeInfo>> filterEmployees(Map<String, Object> filters) {
        try {
            List<EmployeeModel> list = employeeRepo.filter(filters);
            return successList("EMPLOYEE_FILTER", "Lọc nhân viên thành công", mapList(list));
        } catch (Exception e) {
            return errorList("EMPLOYEE_FILTER", e);
        }
    }

    // =================== Helper methods ===================

    private List<EmployeeInfo> mapList(List<EmployeeModel> list) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(this::toInfo).collect(Collectors.toList());
    }

    private EmployeeInfo toInfo(EmployeeModel e) {
        return new EmployeeInfo(
            e.getId(),
            e.getUsername(),
            e.getName(),
            e.getPhone(),
            e.getRole().name(),
            e.getBranch() != null ? e.getBranch().getId() : null,
            e.getBranch() != null ? e.getBranch().getName() : null,
            e.isStatus()
        );
    }

    private ResponseDTO<EmployeeInfo> success(String type, String msg, EmployeeInfo data) {
        return new ResponseDTO.Builder<EmployeeInfo>()
                .type(type).status(ResponseDTO.STATUS.SUCCESS.getValue()).message(msg).data(data).build();
    }

    private ResponseDTO<EmployeeInfo> invalid(String type, String msg) {
        return new ResponseDTO.Builder<EmployeeInfo>()
                .type(type).status(ResponseDTO.STATUS.INVALID.getValue()).message(msg).data(null).build();
    }

    private ResponseDTO<EmployeeInfo> error(String type, Exception e) {
        return new ResponseDTO.Builder<EmployeeInfo>()
                .type(type).status(ResponseDTO.STATUS.ERROR.getValue()).message("Lỗi server").data(null).build();
    }

    private ResponseDTO<List<EmployeeInfo>> successList(String type, String msg, List<EmployeeInfo> data) {
        return new ResponseDTO.Builder<List<EmployeeInfo>>()
                .type(type).status(ResponseDTO.STATUS.SUCCESS.getValue()).message(msg).data(data).build();
    }

    private ResponseDTO<List<EmployeeInfo>> errorList(String type, Exception e) {
        return new ResponseDTO.Builder<List<EmployeeInfo>>()
                .type(type).status(ResponseDTO.STATUS.ERROR.getValue()).message(e.getMessage()).data(null).build();
    }
}
