package edu.ptithcm.models;

import java.io.Serializable;
import java.util.Objects;

public class ShiftAssignmentId implements Serializable {
    private Integer shift;
    private String employee;
    private Integer branch;

    public ShiftAssignmentId() {}

    public ShiftAssignmentId(Integer shift, String employee, Integer branch) {
        this.shift = shift;
        this.employee = employee;
        this.branch = branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftAssignmentId)) return false;
        ShiftAssignmentId that = (ShiftAssignmentId) o;
        return Objects.equals(shift, that.shift)
            && Objects.equals(employee, that.employee)
            && Objects.equals(branch, that.branch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shift, employee, branch);
    }
}
