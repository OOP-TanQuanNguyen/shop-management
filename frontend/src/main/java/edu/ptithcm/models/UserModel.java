package edu.ptithcm.models;

import java.util.Map;

public class UserModel {
    private String id;
    private Integer branchId;
    private String username;
    private String name;
    private String phone;
    private String role;
    private String hireDate;
    private String endDate;
    private Boolean status;
    private String branch;

    private UserModel(Builder builder) {
        this.id = builder.id;
        this.branchId = builder.branchId;
        this.username = builder.username;
        this.name = builder.name;
        this.phone = builder.phone;
        this.role = builder.role;
        this.hireDate = builder.hireDate;
        this.endDate = builder.endDate;
        this.status = builder.status;
        this.branch = builder.branch;
    }

    public String getUsername(){
        return this.username;
    }
    public String getRole(){
        return this.role;
    }

    public String getBranch(){
        return this.branch;
    }
    public static UserModel fromMap(Map<String, Object> data) {
        return new Builder()
                .id((String) data.get("id"))
                .branchId(data.get("branch_id") instanceof Number
                        ? ((Number) data.get("branch_id")).intValue() : null)
                .username((String) data.get("username"))
                .name((String) data.get("name"))
                .phone((String) data.get("phone"))
                .role((String) data.get("role"))
                .hireDate((String) data.get("hireDate"))
                .endDate((String) data.get("endDate"))
                .status((Boolean) data.get("status"))
                .branch((String) data.get("branch"))
                .build();
    }


    public static class Builder {
        private String id;
        private Integer branchId;
        private String username;
        private String name;
        private String phone;
        private String role;
        private String hireDate;
        private String endDate;
        private Boolean status;
        private String branch;

        public Builder id(String id) { this.id = id; return this; }
        public Builder branchId(Integer branchId) { this.branchId = branchId; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder hireDate(String hireDate) { this.hireDate = hireDate; return this; }
        public Builder endDate(String endDate) { this.endDate = endDate; return this; }
        public Builder status(Boolean status) { this.status = status; return this; }
        public Builder branch(String branch) { this.branch = branch; return this; }

        public UserModel build() {
            return new UserModel(this);
        }
    }
}
