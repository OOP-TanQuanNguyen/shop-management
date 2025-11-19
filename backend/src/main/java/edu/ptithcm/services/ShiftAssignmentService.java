package edu.ptithcm.services;

import java.util.List;
import edu.ptithcm.dto.request.shiftassignment.ShiftAssignmentRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.ShiftAssignmentInfo;
import edu.ptithcm.models.ShiftAssignmentModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.shiftassignment.ShiftAssignmentRepository;
import edu.ptithcm.utils.mapper.BaseMapper;
import edu.ptithcm.utils.mapper.MapperFactory;

public class ShiftAssignmentService {

    private static final ShiftAssignmentRepository ShiftAssignmentRepo = Repository.shiftAssignment();
    private static final BaseMapper<ShiftAssignmentModel, ShiftAssignmentInfo> mapper = MapperFactory.shiftAssignment();

    public ResponseDTO<List<ShiftAssignmentInfo>> getAllAssignments() throws RuntimeException {
        return new SuccessResponse<>("Lấy toàn bộ shift assignment thành công",
                mapper.toDTOList(ShiftAssignmentRepo.findAll()));
    }

    public ResponseDTO<ShiftAssignmentInfo> createAssignment(ShiftAssignmentRequestDTO req) throws RuntimeException {
        if (!req.validForCreate())
            return new InvalidResponse<>("Dữ liệu không hợp lệ để tạo shift assignment");

        ShiftAssignmentModel sa = new ShiftAssignmentModel();
        sa.setShift(Repository.shift().findById(req.getShiftId()));
        sa.setEmployee(Repository.employee().findById(req.getEmployeeId()));
        sa.setBranch(Repository.branch().findById(req.getBranchId()));

        ShiftAssignmentRepo.save(sa);
        return new SuccessResponse<>("Tạo shift assignment thành công", mapper.toDTO(sa));
    }

    public ResponseDTO<ShiftAssignmentInfo> deleteAssignment(ShiftAssignmentRequestDTO req) throws RuntimeException {
        if (!req.validForUpdate())
            return new InvalidResponse<>("Thiếu dữ liệu để xóa shift assignment");

        ShiftAssignmentModel deleted = ShiftAssignmentRepo.delete(new edu.ptithcm.models.ShiftAssignmentId(
                req.getShiftId(), req.getEmployeeId(), req.getBranchId()
        ));

        if (deleted == null)
            return new NotFoundResponse<>("Không tìm thấy shift assignment để xóa");

        return new SuccessResponse<>("Xóa shift assignment thành công", mapper.toDTO(deleted));
    }

    public ResponseDTO<List<ShiftAssignmentInfo>> getByShiftId(Integer shiftId) throws RuntimeException {
        if (shiftId == null) return new InvalidResponse<>("Thiếu shiftId");
        return new SuccessResponse<>("Lấy danh sách assignments theo shift thành công",
                mapper.toDTOList(ShiftAssignmentRepo.findByShift(shiftId)));
    }

    public ResponseDTO<List<ShiftAssignmentInfo>> getByEmployeeId(String employeeId) throws RuntimeException {
        if (employeeId == null || employeeId.isBlank()) return new InvalidResponse<>("Thiếu employeeId");
        return new SuccessResponse<>("Lấy danh sách assignments theo employee thành công",
                mapper.toDTOList(ShiftAssignmentRepo.findByEmployee(employeeId)));
    }

    public ResponseDTO<List<ShiftAssignmentInfo>> getByBranchId(Integer branchId) throws RuntimeException {
        if (branchId == null) return new InvalidResponse<>("Thiếu branchId");
        return new SuccessResponse<>("Lấy danh sách assignments theo branch thành công",
                mapper.toDTOList(ShiftAssignmentRepo.findByBranch(branchId)));
    }
}
