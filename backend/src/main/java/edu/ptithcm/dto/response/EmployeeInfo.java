package edu.ptithcm.dto.response;

import java.util.HashMap;
import java.util.Map;

public class EmployeeInfo {
    private final String id;
    private final String username;
    private final String name;
    private final String phone;
    private final String role;
    private final int branchId;
    private final String branch;
    private final boolean status;

    public EmployeeInfo(String id, String username, String name, String phone,
                        String role, int branchId, String branch, boolean status) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.branchId = branchId;
        this.branch = branch;
        this.status = status;
    }

    public String getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getName() { return this.name; }
    public String getPhone() { return this.phone; }
    public String getRole() { return this.role; }
    public int getBranchId() { return this.branchId; }
    public String getBranch() { return this.branch; }
    public boolean isStatus() { return this.status; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("username", this.username);
        map.put("name", this.name);
        map.put("phone", this.phone);
        map.put("role", this.role);
        map.put("branchId", this.branchId);
        map.put("branch", this.branch);
        map.put("status", this.status);
        return map;
    }
}
