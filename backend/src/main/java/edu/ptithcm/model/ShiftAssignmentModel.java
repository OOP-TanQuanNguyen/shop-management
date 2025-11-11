package edu.ptithcm.model;

public class ShiftAssignmentModel {
    private int shiftId;
    private String employeeId;
    private int branchId;

    public ShiftAssignmentModel() {}

    private ShiftAssignmentModel(Builder b) {
        this.shiftId = b.shiftId;
        this.employeeId = b.employeeId;
        this.branchId = b.branchId;
    }

    public int getShiftId() { return this.shiftId; }
    public String getEmployeeId() { return this.employeeId; }
    public int getBranchId() { return this.branchId; }

    public static class Builder {
        private int shiftId;
        private String employeeId;
        private int branchId;

        public Builder shift(int id) { this.shiftId = id; return this; }
        public Builder employee(String id) { this.employeeId = id; return this; }
        public Builder branch(int id) { this.branchId = id; return this; }
        public ShiftAssignmentModel build() { return new ShiftAssignmentModel(this); }
    }
}
