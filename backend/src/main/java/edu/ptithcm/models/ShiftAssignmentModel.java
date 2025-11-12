package edu.ptithcm.models;

import jakarta.persistence.*;

@Entity
@Table(name = "shift_assignment")
@IdClass(ShiftAssignmentId.class)
public class ShiftAssignmentModel {

    @Id
    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private ShiftModel shift;

    @Id
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeModel employee;

    @Id
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private BranchModel branch;

    // --- Constructors ---
    public ShiftAssignmentModel() {}

    public ShiftAssignmentModel(ShiftModel shift, EmployeeModel employee, BranchModel branch) {
        this.shift = shift;
        this.employee = employee;
        this.branch = branch;
    }

    // --- Getters & Setters ---
    public ShiftModel getShift() { return shift; }
    public void setShift(ShiftModel shift) { this.shift = shift; }

    public EmployeeModel getEmployee() { return employee; }
    public void setEmployee(EmployeeModel employee) { this.employee = employee; }

    public BranchModel getBranch() { return branch; }
    public void setBranch(BranchModel branch) { this.branch = branch; }

    @Override
    public String toString() {
        return "ShiftAssignmentModel{" +
                "shift=" + (shift != null ? shift.getId() : null) +
                ", employee=" + (employee != null ? employee.getId() : null) +
                ", branch=" + (branch != null ? branch.getId() : null) +
                '}';
    }
}
