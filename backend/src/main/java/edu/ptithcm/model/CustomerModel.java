package edu.ptithcm.model;

import java.sql.Timestamp;
import java.util.UUID;

public class CustomerModel {
    private String id;
    private String name;
    private String phone;
    private Timestamp createdAt;

    public CustomerModel() {}

    private CustomerModel(Builder b) {
        this.id = b.id;
        this.name = b.name;
        this.phone = b.phone;
        this.createdAt = b.createdAt;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public Timestamp getCreatedAt() { return createdAt; }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private String name;
        private String phone;
        private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

        public Builder id(String id) { this.id = id; return this;}
        public Builder name(String n) { this.name = n; return this; }
        public Builder phone(String p) { this.phone = p; return this; }
        public CustomerModel build() { return new CustomerModel(this); }
    }
}
