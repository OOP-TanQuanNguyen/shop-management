package edu.ptithcm.repository;

import edu.ptithcm.repository.branch.BranchRepository;
import edu.ptithcm.repository.branch.BranchRepositoryMySQL;
import edu.ptithcm.repository.category.CategoryRepository;
import edu.ptithcm.repository.category.CategoryRepositoryMySQL;
import edu.ptithcm.repository.customer.CustomerRepository;
import edu.ptithcm.repository.customer.CustomerRepositoryMySQL;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.repository.employee.EmployeeRepositoryMySQL;
import edu.ptithcm.repository.inventory.InventoryRepository;
import edu.ptithcm.repository.inventory.InventoryRepositoryMySQL;
import edu.ptithcm.repository.invoice.InvoiceDetailRepository;
import edu.ptithcm.repository.invoice.InvoiceDetailRepositoryMySQL;
import edu.ptithcm.repository.invoice.InvoiceRepository;
import edu.ptithcm.repository.invoice.InvoiceRepositoryMySQL;
import edu.ptithcm.repository.loyalty.LoyaltyRepository;
import edu.ptithcm.repository.loyalty.LoyaltyRepositoryMySQL;
import edu.ptithcm.repository.product.ProductRepository;
import edu.ptithcm.repository.product.ProductRepositoryMySQL;
import edu.ptithcm.repository.shift.ShiftAssignmentRepository;
import edu.ptithcm.repository.shift.ShiftAssignmentRepositoryMySQL;
import edu.ptithcm.repository.shift.ShiftRepository;
import edu.ptithcm.repository.shift.ShiftRepositoryMySQL;



public final class Repository {

    private static DBType dbType = DBType.MYSQL;

    private static CategoryRepository categoryRepo;
    private static ProductRepository productRepo;
    private static EmployeeRepository employeeRepo;
    private static BranchRepository branchRepo;
    private static CustomerRepository customerRepo;
    private static LoyaltyRepository loyaltyRepo;
    private static InvoiceRepository invoiceRepo;
    private static InvoiceDetailRepository invoiceDetailRepo;
    private static InventoryRepository inventoryRepo;
    private static ShiftRepository shiftRepo;
    private static ShiftAssignmentRepository shiftAssignRepo;

    private Repository() {} 


    public static void setDatabaseType(DBType type) {
        dbType = type;
        initRepositories();
    }

    private static void initRepositories() {
        if (dbType == DBType.MYSQL) {
            categoryRepo = CategoryRepositoryMySQL.getInstance();
            productRepo = ProductRepositoryMySQL.getInstance();
            employeeRepo = EmployeeRepositoryMySQL.getInstance();
            branchRepo = BranchRepositoryMySQL.getInstance();
            customerRepo = CustomerRepositoryMySQL.getInstance();
            loyaltyRepo = LoyaltyRepositoryMySQL.getInstance();
            invoiceRepo = InvoiceRepositoryMySQL.getInstance();
            invoiceDetailRepo = InvoiceDetailRepositoryMySQL.getInstance();
            inventoryRepo = InventoryRepositoryMySQL.getInstance();
            shiftRepo = ShiftRepositoryMySQL.getInstance();
            shiftAssignRepo = ShiftAssignmentRepositoryMySQL.getInstance();
        } else {
            throw new IllegalArgumentException("Unsupported DB: " + dbType);
        }
    }

    public static CategoryRepository category() { return categoryRepo; }
    public static ProductRepository product() { return productRepo; }
    public static EmployeeRepository employee() { return employeeRepo; }
    public static BranchRepository branch() { return branchRepo; }
    public static CustomerRepository customer() { return customerRepo; }
    public static LoyaltyRepository loyalty() { return loyaltyRepo; }
    public static InvoiceRepository invoice() { return invoiceRepo; }
    public static InvoiceDetailRepository invoiceDetail() { return invoiceDetailRepo; }
    public static InventoryRepository inventory() { return inventoryRepo; }
    public static ShiftRepository shift() { return shiftRepo; }
    public static ShiftAssignmentRepository shiftAssignment() { return shiftAssignRepo; }


    static { initRepositories(); }
}
