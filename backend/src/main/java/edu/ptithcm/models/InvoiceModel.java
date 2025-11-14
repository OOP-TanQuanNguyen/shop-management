package edu.ptithcm.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice")
public class InvoiceModel {

    @Id
    @Column(name = "invoice_id", length = 36)
    private String id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private EmployeeModel employee;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchModel branch;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerModel customer;

    @Column(name = "created_at")
    private Timestamp createdAt;

    private BigDecimal total = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private String note;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceDetailModel> details;

    // --- Constructors ---
    public InvoiceModel() {}
    private InvoiceModel(Builder builder) {
        this.id = builder.id;
        this.employee = builder.employee;
        this.branch = builder.branch;
        this.customer = builder.customer;
        this.createdAt = builder.createdAt;
        this.total = builder.total;
        this.discount = builder.discount;
        this.note = builder.note;
        this.details = builder.details;
    }

    // --- Builder ---
    public static class Builder {
        private String id;
        private EmployeeModel employee;
        private BranchModel branch;
        private CustomerModel customer;
        private Timestamp createdAt;
        private BigDecimal total = BigDecimal.ZERO;
        private BigDecimal discount = BigDecimal.ZERO;
        private String note;
        private List<InvoiceDetailModel> details;

        public Builder id(String id) { this.id = id; return this; }
        public Builder employee(EmployeeModel employee) { this.employee = employee; return this; }
        public Builder branch(BranchModel branch) { this.branch = branch; return this; }
        public Builder customer(CustomerModel customer) { this.customer = customer; return this; }
        public Builder createdAt(Timestamp createdAt) { this.createdAt = createdAt; return this; }
        public Builder total(BigDecimal total) { this.total = total; return this; }
        public Builder discount(BigDecimal discount) { this.discount = discount; return this; }
        public Builder note(String note) { this.note = note; return this; }
        public Builder details(List<InvoiceDetailModel> details) { this.details = details; return this; }

        public InvoiceModel build() { return new InvoiceModel(this); }
    }

    // --- Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public EmployeeModel getEmployee() { return employee; }
    public void setEmployee(EmployeeModel employee) { this.employee = employee; }
    public BranchModel getBranch() { return branch; }
    public void setBranch(BranchModel branch) { this.branch = branch; }
    public CustomerModel getCustomer() { return customer; }
    public void setCustomer(CustomerModel customer) { this.customer = customer; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public List<InvoiceDetailModel> getDetails() { return details; }
    public void setDetails(List<InvoiceDetailModel> details) { this.details = details; }

    @Override
    public String toString() {
        return "InvoiceModel{" +
                "id='" + id + '\'' +
                ", total=" + total +
                ", discount=" + discount +
                '}';
    }
}
