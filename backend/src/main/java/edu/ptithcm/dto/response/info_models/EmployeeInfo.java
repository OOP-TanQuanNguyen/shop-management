package edu.ptithcm.dto.response.info_models;

import java.util.HashMap;
import java.util.Map;

public class EmployeeInfo {
    private final String id;
    private final String username;
    private final String name;
    private final String phone;
    private final String role;
    private final Integer branchId;
    private final String branch;
    private final boolean status;

    private EmployeeInfo(Builder b) {
        this.id = b.id;
        this.username = b.username;
        this.name = b.name;
        this.phone = b.phone;
        this.role = b.role;
        this.branchId = b.branchId;
        this.branch = b.branch;
        this.status = b.status;
    }

    // ==========================
    // Getters
    // ==========================
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
    public Integer getBranchId() { return branchId; }
    public String getBranch() { return branch; }
    public boolean isStatus() { return status; }

    // ==========================
    // Map converter
    // ==========================
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("name", name);
        map.put("phone", phone);
        map.put("role", role);
        map.put("branchId", branchId);
        map.put("branch", branch);
        map.put("status", status);
        return map;
    }

    // ==========================
    // Builder pattern
    // ==========================
    public static class Builder {
        private String id;
        private String username;
        private String name;
        private String phone;
        private String role;
        private Integer branchId;
        private String branch;
        private boolean status;

        public Builder id(String id) { this.id = id; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder branchId(Integer branchId) { this.branchId = branchId; return this; }
        public Builder branch(String branch) { this.branch = branch; return this; }
        public Builder status(boolean status) { this.status = status; return this; }

        public EmployeeInfo build() {
            return new EmployeeInfo(this);
        }
    }
}
