package edu.ptithcm.models;

import java.util.Map;

public class CustomerModel {

    private String id;
    private String name;
    private String phone;
    private Integer point; // ✅ Phải là Integer (nullable) hoặc int với giá trị mặc định

    private CustomerModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.phone = builder.phone;
        this.point = builder.point;
    }

    // ========================
    //        GETTERS
    // ========================
    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Integer getPoint() {
        return this.point != null ? this.point : 0; // ✅ Trả về 0 nếu null
    }

    // ========================
    //        SETTERS (cho Hibernate)
    // ========================
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

    // ========================
    //      fromMap()
    // ========================
    public static CustomerModel fromMap(Map<String, Object> data) {
        Object pointObj = data.get("point");
        Integer pointValue = 0; // ✅ Mặc định = 0

        if (pointObj instanceof Number) {
            pointValue = ((Number) pointObj).intValue();
        }

        return new Builder()
                .id((String) data.get("customerId"))
                .name((String) data.get("name"))
                .phone((String) data.get("phone"))
                .point(pointValue)
                .build();
    }

    // ========================
    //        BUILDER
    // ========================
    public static class Builder {

        private String id;
        private String name;
        private String phone;
        private Integer point = 0; // ✅ Mặc định = 0

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
            this.point = point != null ? point : 0; // ✅ Đảm bảo không null
            return this;
        }

        public CustomerModel build() {
            return new CustomerModel(this);
        }
    }
}
