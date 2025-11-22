package edu.ptithcm.models;

import java.util.Map;

public class BranchInfo {

    private final String id;
    private final String name;
    private final String phone;
    private final String address;

    public BranchInfo(String id, String name, String phone, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // ======================================================================
    //  FIX QUAN TRỌNG: branchId từ BE có thể là Double → Convert về Integer
    // ======================================================================
    public static BranchInfo fromMap(Map<String, Object> map) {

        Object rawId = map.get("branchId");
        String idValue;

        if (rawId instanceof Number n) {
            // BE trả 11.0 → FE chuyển thành "11"
            idValue = String.valueOf(n.intValue());
        } else {
            idValue = rawId != null ? rawId.toString() : null;
        }

        return new BranchInfo(
                idValue, // id FIXED
                (String) map.getOrDefault("name", null),
                (String) map.getOrDefault("phone", null),
                (String) map.getOrDefault("address", null)
        );
    }

    // ======================================================================
    //  GETTERS
    // ======================================================================
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
