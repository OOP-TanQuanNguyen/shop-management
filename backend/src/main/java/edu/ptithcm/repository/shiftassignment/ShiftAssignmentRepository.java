package edu.ptithcm.repository.shiftassignment;

import java.util.List;

import edu.ptithcm.models.ShiftAssignmentModel;
import edu.ptithcm.models.ShiftAssignmentId;
import edu.ptithcm.repository.GenericRepository;

public interface ShiftAssignmentRepository extends GenericRepository<ShiftAssignmentModel, ShiftAssignmentId> {
    List<ShiftAssignmentModel> findByShift(Integer shiftId);
    List<ShiftAssignmentModel> findByEmployee(String employeeId);
    List<ShiftAssignmentModel> findByBranch(Integer branchId);
}
