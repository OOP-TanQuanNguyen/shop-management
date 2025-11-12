package edu.ptithcm.models;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "branch")
public class BranchModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    private String phone;
    private String address;

    @Column(name = "open_date")
    private Date openDate;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeModel> employees;

    // --- Constructors ---
    public BranchModel() {}
    private BranchModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.phone = builder.phone;
        this.address = builder.address;
        this.openDate = builder.openDate;
        this.isActive = builder.isActive;
        this.employees = builder.employees;
    }

    // --- Builder ---
    public static class Builder {
        private Integer id;
        private String name;
        private String phone;
        private String address;
        private Date openDate;
        private boolean isActive = true;
        private List<EmployeeModel> employees;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder openDate(Date openDate) { this.openDate = openDate; return this; }
        public Builder isActive(boolean isActive) { this.isActive = isActive; return this; }
        public Builder employees(List<EmployeeModel> employees) { this.employees = employees; return this; }
        public BranchModel build() { return new BranchModel(this); }
    }

    // --- Getters & Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Date getOpenDate() { return openDate; }
    public void setOpenDate(Date openDate) { this.openDate = openDate; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public List<EmployeeModel> getEmployees() { return employees; }
    public void setEmployees(List<EmployeeModel> employees) { this.employees = employees; }

    @Override
    public String toString() {
        return "BranchModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", openDate=" + openDate +
                ", isActive=" + isActive +
                '}';
    }
}
