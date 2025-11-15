package edu.ptithcm.dto.response.info_models;

import java.util.HashMap;
import java.util.Map;

public class BranchInfo {

    private final Integer branchId;
    private final String name;
    private final String phone;
    private final String address;

    public BranchInfo(Integer branchId, String name, String phone, String address) {
        this.branchId = branchId;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public Integer getBranchId() { return branchId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("branchId", branchId);
        map.put("name", name);
        map.put("phone", phone);
        map.put("address", address);
        return map;
    }

    // Builder tiện lợi
    public static class Builder {
        private Integer branchId;
        private String name;
        private String phone;
        private String address;

        public Builder branchId(Integer branchId) { this.branchId = branchId; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public BranchInfo build() { return new BranchInfo(branchId, name, phone, address); }
    }
}
