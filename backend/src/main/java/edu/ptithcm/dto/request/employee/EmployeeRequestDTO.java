package edu.ptithcm.dto.request.employee;

import java.util.Map;

import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.utils.RequestUtil;

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
        this.employeeId = RequestUtil.toStr(data.get("id")) ;
        this.username = RequestUtil.toStr(data.get("username"));
        this.password =  RequestUtil.toStr(data.get("password"));
        this.name =  RequestUtil.toStr(data.get("name"));
        this.phone =  RequestUtil.toStr(data.get("phone"));

        // --- Role handling ---
        Object roleValue = data.get("role");
        if (roleValue instanceof String strRole) {
            this.role = EmployeeModel.Role.valueOf(strRole.toUpperCase());
        } else if (roleValue instanceof EmployeeModel.Role r) {
            this.role = r;
        } else {
            this.role = EmployeeModel.Role.STAFF; // default
        }

        System.out.println("Im before line convert to Integer");

        // --- Branch ID handling ---
        this.branchId = RequestUtil.toInt(data.get("branchId"));
    
        System.out.println("DTO : "+ this.branchId);

        
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
