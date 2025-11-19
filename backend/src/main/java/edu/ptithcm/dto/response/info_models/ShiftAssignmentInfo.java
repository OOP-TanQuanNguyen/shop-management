package edu.ptithcm.dto.response.info_models;

import java.util.HashMap;
import java.util.Map;

public class ShiftAssignmentInfo {
    private final Integer shiftId;
    private final String employeeId;
    private final Integer branchId;

    private ShiftAssignmentInfo(Builder b) {
        this.shiftId = b.shiftId;
        this.employeeId = b.employeeId;
        this.branchId = b.branchId;
    }

    public Integer getShiftId() { return shiftId; }
    public String getEmployeeId() { return employeeId; }
    public Integer getBranchId() { return branchId; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("shiftId", shiftId);
        map.put("employeeId", employeeId);
        map.put("branchId", branchId);
        return map;
    }

    public static class Builder {
        private Integer shiftId;
        private String employeeId;
        private Integer branchId;

        public Builder shiftId(Integer shiftId) { this.shiftId = shiftId; return this; }
        public Builder employeeId(String employeeId) { this.employeeId = employeeId; return this; }
        public Builder branchId(Integer branchId) { this.branchId = branchId; return this; }

        public ShiftAssignmentInfo build() { return new ShiftAssignmentInfo(this); }
    }
}
