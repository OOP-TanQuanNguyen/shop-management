package edu.ptithcm.dto.response.info_models;

import java.sql.Timestamp;
import java.util.Map;

public class CustomerInfo {

    private final String customerId;
    private final String name;
    private final String phone;
    private final Timestamp createdAt;

    private CustomerInfo(Builder builder) {
        this.customerId = builder.customerId;
        this.name = builder.name;
        this.phone = builder.phone;
        this.createdAt = builder.createdAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "customerId", customerId,
                "name", name,
                "phone", phone,
                "createdAt", createdAt
        );
    }

    // ------------------- Builder -------------------
    public static class Builder {

        private String customerId;
        private String name;
        private String phone;
        private Timestamp createdAt;

        public Builder customerId(String customerId) {
            this.customerId = customerId;
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

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CustomerInfo build() {
            return new CustomerInfo(this);
        }
    }
}
