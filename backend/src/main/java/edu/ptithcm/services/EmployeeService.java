package edu.ptithcm.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.ptithcm.dto.request.employee.EmployeeRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.info_models.EmployeeInfo;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.models.BranchModel;
import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.branch.BranchRepository;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.utils.CryptoUtil;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class EmployeeService {

    private static final EmployeeRepository employeeRepo = Repository.employee();
    private static final BranchRepository branchRepo = Repository.branch();
    private static final BaseMapper<EmployeeModel, EmployeeInfo> mapper = MapperFactory.employee();

    // ------------------ Lấy tất cả nhân viên ------------------
    public ResponseDTO<List<EmployeeInfo>> getAllEmployees() throws RuntimeException {
        return new SuccessResponse<>(
                "Lấy toàn bộ nhân viên thành công",
                mapper.toDTOList(employeeRepo.findAll())
        );
    }

    // ------------------ Lấy nhân viên đang hoạt động ------------------
    public ResponseDTO<List<EmployeeInfo>> getActiveEmployees() throws RuntimeException {
        return new SuccessResponse<>(
                "Lấy nhân viên đang hoạt động thành công",
                mapper.toDTOList(employeeRepo.findActive())
        );
    }

    // ------------------ Tạo nhân viên ------------------
    public ResponseDTO<EmployeeInfo> createEmployee(EmployeeRequestDTO req) throws RuntimeException {

        if (!req.validForCreate())
            return new InvalidResponse<>("Thiếu thông tin bắt buộc");

        if (employeeRepo.existsByUsername(req.getUsername()))
            return new InvalidResponse<>("Tên đăng nhập đã tồn tại");

        BranchModel branch = (req.getBranchId() != null)
                ? branchRepo.findById(req.getBranchId())
                : null;

        System.out.println("SERVICES : "+ branch);

        EmployeeModel employee = new EmployeeModel.Builder()
                .id(UUID.randomUUID().toString())
                .username(req.getUsername())
                .password(CryptoUtil.hash(req.getPassword()))
                .name(req.getName())
                .phone(req.getPhone())
                .branch(branch)
                .role(req.getRole())
                .status(true)
                .startAt(new java.sql.Timestamp(System.currentTimeMillis()))
                .build();

        employeeRepo.save(employee);

        return new SuccessResponse<>("Tạo nhân viên thành công", mapper.toDTO(employee));
    }

    // ------------------ Cập nhật nhân viên ------------------
    public ResponseDTO<EmployeeInfo> updateEmployee(EmployeeRequestDTO req) throws RuntimeException {

        if (!req.validForUpdate())
            return new InvalidResponse<>("Thiếu ID hoặc dữ liệu cập nhật");

        EmployeeModel current = employeeRepo.findById(req.getEmployeeId());
        if (current == null)
            return new NotFoundResponse<>("Nhân viên không tồn tại!");

        boolean newStatus = req.getStatus() != null ? req.getStatus() : current.isStatus();
        java.sql.Timestamp startAt = current.getStartAt();
        java.sql.Timestamp endAt = current.getEndAt();

        if (current.isStatus() && !newStatus) {
            endAt = new java.sql.Timestamp(System.currentTimeMillis());
        }
        else if (!current.isStatus() && newStatus) {
            startAt = new java.sql.Timestamp(System.currentTimeMillis()); 
            endAt = null; 
        }
        else {
            startAt = current.getStartAt();
            endAt = current.getEndAt();
        }

        EmployeeModel temp = new EmployeeModel.Builder()
                .id(req.getEmployeeId())
                .name(req.getName())
                .phone(req.getPhone())
                .role(req.getRole())
                .password(req.getPassword() != null ? CryptoUtil.hash(req.getPassword()) : null)
                .branch(req.getBranchId() != null ? branchRepo.findById(req.getBranchId()) : null)
                .status(newStatus)
                .startAt(startAt)
                .endAt(endAt)
                .build();
      
        EmployeeModel updated = employeeRepo.update(temp);

        if (updated == null)
            return new NotFoundResponse<>("Nhân viên không tồn tại!");

        return new SuccessResponse<>("Cập nhật nhân viên thành công", mapper.toDTO(updated));
    }

    // ------------------ Xóa nhân viên ------------------
    public ResponseDTO<EmployeeInfo> deleteEmployee(EmployeeRequestDTO req) throws RuntimeException {

        if (req.getEmployeeId() == null || req.getEmployeeId().isEmpty())
            return new InvalidResponse<>("Thiếu ID nhân viên");

        EmployeeModel deleted = employeeRepo.delete(req.getEmployeeId());

        if (deleted == null)
            return new NotFoundResponse<>("Không tồn tại nhân viên");

        return new SuccessResponse<>("Xóa nhân viên thành công", mapper.toDTO(deleted));
    }

    // ------------------ Lọc nhân viên ------------------
    public ResponseDTO<List<EmployeeInfo>> filterEmployees(Map<String, Object> filters) throws RuntimeException{
        return new SuccessResponse<>(
                "Lọc nhân viên thành công",
                mapper.toDTOList(employeeRepo.filter(filters))
        );
    }
}
