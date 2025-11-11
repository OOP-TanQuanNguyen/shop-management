package edu.ptithcm.model;

import java.sql.Date;

public class BranchModel {

    private Integer id;
    private String name;
    private String phone;
    private String address;
    private Date openDate;
    private boolean isActive;

    public BranchModel() {}

    private BranchModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.phone = builder.phone;
        this.address = builder.address;
        this.openDate = builder.openDate;
        this.isActive = builder.isActive;
    }

    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public String getPhone() { return this.phone; }
    public String getAddress() { return this.address; }
    public Date getOpenDate() { return this.openDate; }
    public boolean isActive() { return this.isActive; }

    public static class Builder {
        private Integer id;
        private String name;
        private String phone;
        private String address;
        private Date openDate;
        private boolean isActive = true;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder openDate(Date openDate) { this.openDate = openDate; return this; }
        public Builder isActive(boolean active) { this.isActive = active; return this; }

        public BranchModel build() { return new BranchModel(this); }
    }

    @Override
    public String toString() {
        return "BranchModel {" +
                "\n  id=" + this.id +
                ",\n  name='" + this.name + '\'' +
                ",\n  phone='" + this.phone + '\'' +
                ",\n  address='" + this.address + '\'' +
                ",\n  openDate=" + this.openDate +
                ",\n  isActive=" + this.isActive +
                "\n}";
    }
}
