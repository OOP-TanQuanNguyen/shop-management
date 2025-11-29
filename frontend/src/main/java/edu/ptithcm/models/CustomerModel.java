package edu.ptithcm.models;

import java.util.Map;

public class CustomerModel {

    private String id;
    private String name;
    private String phone;
    private Integer point;

    private CustomerModel(Builder b) {
        this.id = b.id;
        this.name = b.name;
        this.phone = b.phone;
        this.point = b.point;
    }

    // ==========================
    // GETTERS
    // ==========================
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getPoint() {
        return point != null ? point : 0;
    }

    // ==========================
    // SETTERS (Hibernate)
    // ==========================
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    // ==========================
    // fromMap → Nhận từ CustomerInfo.toMap()
    // ==========================
    public static CustomerModel fromMap(Map<String, Object> data) {

        Integer point = 0;
        Object p = data.get("point");
        if (p instanceof Number n) {
            point = n.intValue();
        }

        return new Builder()
                .id((String) data.get("customerId"))
                .name((String) data.get("name"))
                .phone((String) data.get("phone"))
                .point(point)
                .build();
    }

    // ==========================
    // BUILDER
    // ==========================
    public static class Builder {

        private String id;
        private String name;
        private String phone;
        private Integer point = 0;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder point(Integer point) {
            this.point = point != null ? point : 0;
            return this;
        }

        public CustomerModel build() {
            return new CustomerModel(this);
        }
    }
}
