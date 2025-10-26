package edu.ptithcm.model;
import java.util.UUID;

public class EmployeeModel {
    private enum ROLE {
        ADMIN,
        STAFF;
    }   
    private String id;
    private String branch;
    private String username;
    private String passwordHash;
    private String name;
    private String phone;
    private String role;
    private String hireDate;
    private String endDate;
    private boolean status;

    public EmployeeModel(Builder builder) {
        this.id = builder.id;
        this.branch = builder.branch;
        this.username = builder.username;
        this.passwordHash = builder.passwordHash;
        this.name = builder.name;
        this.phone = builder.phone;
        this.role = builder.role;
        this.hireDate = builder.hireDate;
        this.endDate = builder.endDate;
        this.status = builder.status;
    }

    public String getId() { return id; }
    public String getBranch() { return branch; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public String getHireDate() { return hireDate; }
    public String getEndDate() { return endDate; }
    public boolean isStatus() { return status; }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private String branch;
        private String username;
        private String passwordHash;
        private String name;
        private String phone;
        private String role = ROLE.STAFF.toString();
        private String hireDate;
        private String endDate;
        private boolean status;
        private boolean autoHashPassword = false;

        public Builder id(String id) { this.id = id; return this; }
        public Builder branch(String branch) { this.branch = branch; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.passwordHash = password; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder hireDate(String hireDate) { this.hireDate = hireDate; return this; }
        public Builder endDate(String endDate) { this.endDate = endDate; return this; }
        public Builder status(boolean status) { this.status = status; return this; }

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
                "\n  id='" + id + '\'' +
                ",\n  username='" + username + '\'' +
                ",\n  passwordHash='" + passwordHash + '\'' +
                ",\n  name='" + name + '\'' +
                ",\n  phone='" + phone + '\'' +
                ",\n  role=" + role +
                ",\n  hireDate='" + hireDate + '\'' +
                ",\n  endDate='" + endDate + '\'' +
                ",\n  status=" + status +
                "\n}";
    }

    public static void main(String[] args) {
        EmployeeModel emp = new EmployeeModel.Builder()
                .username("johndoe")
                .password("hashed_password")
                .name("John Doe")
                .phone("0123456789")
                .hireDate("2024-01-01")
                .status(true)
                .build();

        EmployeeModel admin = new AdminBuilder()
                .username("adminuser")
                .password("hashed_admin_password")
                .name("Admin User")
                .phone("0987654321")
                .hireDate("2024-01-01")
                .status(true)
                .build();

        System.out.println("Staff :  " + emp); // Output: STAFF
        System.out.println("Admin: " + admin); // Output: ADMIN
    }

}