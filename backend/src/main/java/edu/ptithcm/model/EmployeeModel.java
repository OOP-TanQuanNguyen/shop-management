package edu.ptithcm.model;

import java.util.UUID;

public class EmployeeModel {

    private enum ROLE {
        ADMIN,
        STAFF
    }

    private String id;
    private Integer branchId;
    private String username;
    private String passwordHash;
    private String name;
    private String phone;
    private String role;
    private String hireDate;
    private String endDate;
    private boolean status;
    private String branch;

    // 🔹 Constructor rỗng cho ORM / mapper
    public EmployeeModel() {}

    // 🔹 Constructor từ Builder
    public EmployeeModel(Builder builder) {
        this.id = builder.id;
        this.branchId = builder.branchId;
        this.username = builder.username;
        this.passwordHash = builder.passwordHash;
        this.name = builder.name;
        this.phone = builder.phone;
        this.role = builder.role;
        this.hireDate = builder.hireDate;
        this.endDate = builder.endDate;
        this.status = builder.status;
        this.branch = builder.branch;
    }

    public String getId() { return this.id; }
    public Integer getBranchId() { return this.branchId; }
    public String getBranch() { return this.branch; }
    public String getUsername() { return this.username; }
    public String getPasswordHash() { return this.passwordHash; }
    public String getName() { return this.name; }
    public String getPhone() { return this.phone; }
    public String getRole() { return this.role; }
    public String getHireDate() { return this.hireDate; }
    public String getEndDate() { return this.endDate; }
    public boolean isStatus() { return this.status; }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private Integer branchId;
        private String username;
        private String passwordHash;
        private String name;
        private String phone;
        private String role = ROLE.STAFF.toString();
        private String hireDate;
        private String endDate;
        private boolean status;
        private String branch;

        public Builder id(String id) { this.id = id; return this; }
        public Builder branchId(Integer branch) { this.branchId = branch; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.passwordHash = password; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder hireDate(String hireDate) { this.hireDate = hireDate; return this; }
        public Builder endDate(String endDate) { this.endDate = endDate; return this; }
        public Builder status(boolean status) { this.status = status; return this; }
        public Builder branch(String branch) { this.branch = branch; return this; }

        public EmployeeModel build() {
            return new EmployeeModel(this);
        }
    }

    public static class AdminBuilder extends Builder {
        public AdminBuilder() {
            this.role(ROLE.ADMIN.toString());
        }
    }

    @Override
    public String toString() {
        return "EmployeeModel {" +
                "\n  id='" + this.id + '\'' +
                ",\n  branchId=" + this.branchId +
                ",\n  username='" + this.username + '\'' +
                ",\n  passwordHash='" + this.passwordHash + '\'' +
                ",\n  name='" + this.name + '\'' +
                ",\n  phone='" + this.phone + '\'' +
                ",\n  role='" + this.role + '\'' +
                ",\n  hireDate='" + this.hireDate + '\'' +
                ",\n  endDate='" + this.endDate + '\'' +
                ",\n  status=" + this.status +
                ",\n  branch='" + this.branch + '\'' +
                "\n}";
    }
}
