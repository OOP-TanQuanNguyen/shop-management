package edu.ptithcm.models;

import java.util.Map;

public class BranchInfo {

    private Integer id;
    private String name;
    private String phone;
    private String address;
    private String openDate;
    private Boolean isActive;

    public BranchInfo() {
    }

    public BranchInfo(Map<String, Object> data) {
        this.id = data.get("id") != null ? ((Number) data.get("id")).intValue() : null;
        this.name = (String) data.get("name");
        this.phone = (String) data.get("phone");
        this.address = (String) data.get("address");
        this.openDate = (String) data.get("openDate");

        Object activeObj = data.get("isActive");
        this.isActive = activeObj != null ? (Boolean) activeObj : true;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getStatusText() {
        return Boolean.TRUE.equals(isActive) ? "Hoạt động" : "Ngừng hoạt động";
    }

    @Override
    public String toString() {
        return "BranchInfo{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", phone='" + phone + '\''
                + ", address='" + address + '\''
                + ", isActive=" + isActive
                + '}';
    }
}
