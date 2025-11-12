package edu.ptithcm.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "employee")
public class EmployeeModel {

    @Id
    @Column(name = "employee_id", length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchModel branch;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STAFF;

    @Column(name = "start_at")
    private Timestamp startAt;

    @Column(name = "end_at")
    private Timestamp endAt;

    private boolean status = true;

    @OneToMany(mappedBy = "employee")
    private List<InvoiceModel> invoices;

    @OneToMany(mappedBy = "employee")
    private List<ShiftAssignmentModel> shiftAssignments;

    public enum Role { ADMIN, STAFF }

    // --- Constructors ---
    public EmployeeModel() {}
    private EmployeeModel(Builder builder) {
        this.id = builder.id;
        this.branch = builder.branch;
        this.username = builder.username;
        this.password = builder.password;
        this.name = builder.name;
        this.phone = builder.phone;
        this.role = builder.role;
        this.startAt = builder.startAt;
        this.endAt = builder.endAt;
        this.status = builder.status;
        this.invoices = builder.invoices;
        this.shiftAssignments = builder.shiftAssignments;
    }

    // --- Builder ---
    public static class Builder {
        private String id;
        private BranchModel branch;
        private String username;
        private String password;
        private String name;
        private String phone;
        private Role role = Role.STAFF;
        private Timestamp startAt;
        private Timestamp endAt;
        private boolean status = true;
        private List<InvoiceModel> invoices;
        private List<ShiftAssignmentModel> shiftAssignments;

        public Builder id(String id) { this.id = id; return this; }
        public Builder branch(BranchModel branch) { this.branch = branch; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder startAt(Timestamp startAt) { this.startAt = startAt; return this; }
        public Builder endAt(Timestamp endAt) { this.endAt = endAt; return this; }
        public Builder status(boolean status) { this.status = status; return this; }
        public Builder invoices(List<InvoiceModel> invoices) { this.invoices = invoices; return this; }
        public Builder shiftAssignments(List<ShiftAssignmentModel> shiftAssignments) { this.shiftAssignments = shiftAssignments; return this; }

        public EmployeeModel build() { return new EmployeeModel(this); }
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BranchModel getBranch() { return branch; }
    public void setBranch(BranchModel branch) { this.branch = branch; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public Timestamp getStartAt() { return startAt; }
    public void setStartAt(Timestamp startAt) { this.startAt = startAt; }
    public Timestamp getEndAt() { return endAt; }
    public void setEndAt(Timestamp endAt) { this.endAt = endAt; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public List<InvoiceModel> getInvoices() { return invoices; }
    public void setInvoices(List<InvoiceModel> invoices) { this.invoices = invoices; }
    public List<ShiftAssignmentModel> getShiftAssignments() { return shiftAssignments; }
    public void setShiftAssignments(List<ShiftAssignmentModel> shiftAssignments) { this.shiftAssignments = shiftAssignments; }

    @Override
    public String toString() {
        return "EmployeeModel{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
