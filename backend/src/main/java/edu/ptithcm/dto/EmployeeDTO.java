package edu.ptithcm.dto;

import edu.ptithcm.model.EmployeeModel;

public class EmployeeDTO {
    private String id;
    private Integer branchId;
    private String username;
    private String name;
    private String phone;
    private String role;
    private String hireDate;
    private String endDate;
    private boolean status;
    private String branch;

    public EmployeeDTO() {}

    public EmployeeDTO(String id, Integer branchId, String username, String name,
                       String phone, String role, String hireDate, String endDate,
                       boolean status, String branch) {
        this.id = id;
        this.branchId = branchId;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.hireDate = hireDate;
        this.endDate = endDate;
        this.status = status;
        this.branch = branch;
    }

    public static EmployeeDTO fromModel(EmployeeModel model) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.id = model.getId();
        dto.branchId = model.getBranchId();
        dto.username = model.getUsername();
        dto.name = model.getName();
        dto.phone = model.getPhone();
        dto.role = model.getRole();
        dto.hireDate = model.getHireDate();
        dto.endDate = model.getEndDate();
        dto.status = model.isStatus();
        dto.branch = model.getBranch();
        return dto;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getBranchId() { return branchId; }
    public void setBranchId(Integer branchId) { this.branchId = branchId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getHireDate() { return hireDate; }
    public void setHireDate(String hireDate) { this.hireDate = hireDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
}
