package edu.ptithcm.dto.request.employee;

import java.util.Map;
import edu.ptithcm.models.EmployeeModel;

public class EmployeeRequestDTO {
    private final String employeeId;
    private final String username;
    private final String password;
    private final String name;
    private final String phone;
    private final EmployeeModel.Role role;
    private final Integer branchId;
    private final Boolean status;

    public EmployeeRequestDTO(Map<String, Object> data) {
        this.employeeId = (String) data.get("id");
        this.username = (String) data.get("username");
        this.password = (String) data.get("password");
        this.name = (String) data.get("name");
        this.phone = (String) data.get("phone");

        // --- Role handling ---
        Object roleValue = data.get("role");
        if (roleValue instanceof String strRole) {
            this.role = EmployeeModel.Role.valueOf(strRole.toUpperCase());
        } else if (roleValue instanceof EmployeeModel.Role r) {
            this.role = r;
        } else {
            this.role = EmployeeModel.Role.STAFF; // default
        }

        // --- Branch ID handling ---
        Object branchValue = data.get("branchId");
        if (branchValue instanceof Number num) {
            this.branchId = num.intValue();
        } else if (branchValue instanceof String str && !str.isEmpty()) {
            this.branchId = Integer.parseInt(str);
        } else {
            this.branchId = null;
        }

        // --- Status handling ---
        Object statusValue = data.get("status");
        if (statusValue instanceof Boolean b) {
            this.status = b;
        } else {
            this.status = true; // default
        }
    }

    public boolean validForCreate() {
        return username != null && !username.isEmpty()
            && password != null && !password.isEmpty()
            && name != null && !name.isEmpty()
            && role != null;
    }

    public boolean validForUpdate() {
        return employeeId != null && !employeeId.isEmpty();
    }

    public String getEmployeeId() { return employeeId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public EmployeeModel.Role getRole() { return role; }
    public Integer getBranchId() { return branchId; }
    public Boolean getStatus() { return status; }
}
