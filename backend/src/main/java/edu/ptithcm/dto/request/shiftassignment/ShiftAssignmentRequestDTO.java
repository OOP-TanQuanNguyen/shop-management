package edu.ptithcm.dto.request.shiftassignment;

import edu.ptithcm.utils.RequestUtil;
import java.util.Map;

public class ShiftAssignmentRequestDTO {
    private final Integer shiftId;
    private final String employeeId;
    private final Integer branchId;

    public ShiftAssignmentRequestDTO(Map<String, Object> data) {
        this.shiftId = RequestUtil.toInt(data.get("shiftId"));
        this.employeeId = RequestUtil.toStr(data.get("employeeId"));
        this.branchId = RequestUtil.toInt(data.get("branchId"));
    }

    public Integer getShiftId() { return shiftId; }
    public String getEmployeeId() { return employeeId; }
    public Integer getBranchId() { return branchId; }

    // Validate
    public boolean validForCreate() {
        return shiftId != null && employeeId != null && !employeeId.isBlank() && branchId != null;
    }

    public boolean validForUpdate() {
        return validForCreate(); // ShiftAssignment chỉ cần tạo hoặc update cùng dữ liệu
    }
}
