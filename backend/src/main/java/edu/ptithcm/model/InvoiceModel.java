package edu.ptithcm.model;

import java.sql.Timestamp;
import java.util.UUID;

public class InvoiceModel {
    private String id;
    private String employeeId;
    private Integer branchId;
    private String customerId;
    private Timestamp createdAt;
    private double total;
    private double discount;
    private String note;

    private InvoiceModel() {}

    public InvoiceModel(Builder b) {
        this.id = b.id;
        this.employeeId = b.employeeId;
        this.branchId = b.branchId;
        this.customerId = b.customerId;
        this.createdAt = b.createdAt;
        this.total = b.total;
        this.discount = b.discount;
        this.note = b.note;
    }

    public String getId() {
        return this.id;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public Integer getBranchId() {
        return this.branchId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public double getTotal() {
        return this.total;
    }

    public double getDiscount() {
        return this.discount;
    }

    public String getNote() {
        return this.note;
    }

    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private String employeeId;
        private Integer branchId;
        private String customerId;
        private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        private double total;
        private double discount;
        private String note;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder employee(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder branch(Integer branchId) {
            this.branchId = branchId;
            return this;
        }

        public Builder customer(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder total(double total) {
            this.total = total;
            return this;
        }

        public Builder discount(double discount) {
            this.discount = discount;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public InvoiceModel build() {
            return new InvoiceModel(this);
        }
    }


    @Override
    public String toString() {
        return "InvoiceModel{" +
                "id='" + this.id + '\'' +
                ", employeeId='" + this.employeeId + '\'' +
                ", branchId=" + this.branchId +
                ", customerId='" + this.customerId + '\'' +
                ", createdAt=" + this.createdAt +
                ", total=" + this.total +
                ", discount=" + this.discount +
                ", note='" + this.note + '\'' +
                '}';
    }
}
