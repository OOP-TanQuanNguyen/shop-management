package edu.ptithcm.dto.request.employee;

import java.util.Map;

public class EmployeeRequestDTO {
    private final String id;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final String role;
    private final Integer branchId;
    private final Boolean status;

    public EmployeeRequestDTO(Map<String, Object> data) {
        this.id = (String)data.get("id");
        this.username = (String) data.get("username");
        this.password = (String) data.get("password");
        this.name = (String) data.get("name");
        this.phone = (String) data.get("phone");
        this.role = (String) data.get("role") == null ? "STAFF":"ADMIN";
        this.branchId = data.get("branchId") != null ? (Integer) data.get("branchId") : null;
        this.status = data.get("status") != null ? (Boolean)data.get("status") : true;
    }

    public boolean validForCreate() {
        return username != null && password != null && name != null && role != null;
    }

    public boolean validForUpdate() {
        return id != null && name != null;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() {return this.password; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public Integer getBranchId() { return branchId; }
    public Boolean getStatus() { return status; }
}
