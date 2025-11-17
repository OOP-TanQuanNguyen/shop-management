package edu.ptithcm.models;

import java.util.Map;

public class CustomerModel {

    private String id;
    private String name;
    private String phone;
    private Integer point;

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
        return this.point;
    }

    // ========================
    //      fromMap()
    // ========================
    public static CustomerModel fromMap(Map<String, Object> data) {
        return new Builder()
                .id((String) data.get("customerId"))
                .name((String) data.get("name"))
                .phone((String) data.get("phone"))
                .point(data.get("point") instanceof Number
                        ? ((Number) data.get("point")).intValue()
                        : 0)
                .build();
    }

    // ========================
    //        BUILDER
    // ========================
    public static class Builder {

        private String id;
        private String name;
        private String phone;
        private Integer point;

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
            this.point = point;
            return this;
        }

        public CustomerModel build() {
            return new CustomerModel(this);
        }
    }
}
