package edu.ptithcm.controller;

import edu.ptithcm.dto.request.shiftassignment.ShiftAssignmentRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.ShiftAssignmentInfo;
import edu.ptithcm.services.ShiftAssignmentService;

import java.util.List;

public class ShiftAssignmentController {
    private final ShiftAssignmentService service = new ShiftAssignmentService();

    public ResponseDTO<List<ShiftAssignmentInfo>> getAllAssignments() {
        return service.getAllAssignments();
    }

    public ResponseDTO<ShiftAssignmentInfo> createAssignment(ShiftAssignmentRequestDTO req) {
        return service.createAssignment(req);
    }

    public ResponseDTO<ShiftAssignmentInfo> deleteAssignment(ShiftAssignmentRequestDTO req) {
        return service.deleteAssignment(req);
    }

    public ResponseDTO<List<ShiftAssignmentInfo>> getByShiftId(Integer shiftId) {
        return service.getByShiftId(shiftId);
    }

    public ResponseDTO<List<ShiftAssignmentInfo>> getByEmployeeId(String employeeId) {
        return service.getByEmployeeId(employeeId);
    }

    public ResponseDTO<List<ShiftAssignmentInfo>> getByBranchId(Integer branchId) {
        return service.getByBranchId(branchId);
    }
}
