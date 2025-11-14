package edu.ptithcm.dto.response.info_models;

import java.util.HashMap;
import java.util.Map;

public class UserLoginInfo {
    private final String id;
    private final String username;
    private final String name;
    private final String role;
    private final Integer branchId;
    private final String branch;

    public UserLoginInfo(String id,String username,String name,String role,Integer branchId,String branch) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.role = role;
        this.branchId = branchId;
        this.branch = branch;
    }

    public String getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getName() { return this.name; }
    public String getRole() { return this.role; }
    public Integer getBranchId() { return this.branchId; }
    public String getBranch() { return this.branch; }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id",this.id);
        map.put("username", this.username);
        map.put("name", this.name);
        map.put("role", this.role);
        map.put("branch_id", this.branchId);
        map.put("branch", this.branch);
        return map;
    }
}
