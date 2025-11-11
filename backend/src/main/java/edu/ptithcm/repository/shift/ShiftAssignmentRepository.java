package edu.ptithcm.repository.shift;

import java.sql.SQLException;
import java.util.List;

import edu.ptithcm.model.ShiftAssignmentModel;

public interface ShiftAssignmentRepository {
    void assign(ShiftAssignmentModel sa) throws SQLException;
    List<ShiftAssignmentModel> getAll() throws SQLException;
    List<ShiftAssignmentModel> findByEmployee(String employeeId) throws SQLException;
    void remove(int shiftId, String employeeId, int branchId) throws SQLException;
}
