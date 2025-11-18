package edu.ptithcm.repository;

import edu.ptithcm.repository.product.*;
import edu.ptithcm.repository.employee.*;
import edu.ptithcm.repository.branch.*;
import edu.ptithcm.repository.category.CategoryRepository;
import edu.ptithcm.repository.category.CategoryRepositoryImpl;
import edu.ptithcm.repository.customer.*;
import edu.ptithcm.repository.invoice.*;
import edu.ptithcm.repository.inventory.*;
import edu.ptithcm.repository.loyalty.*;
import edu.ptithcm.repository.shift.*;

public class Repository {
    private static final ProductRepository PRODUCT_REPO = new ProductRepositoryImpl();
    private static final EmployeeRepository EMPLOYEE_REPO = new EmployeeRepositoryImpl();
    private static final BranchRepository BRANCH_REPO = new BranchRepositoryImpl();
    private static final CustomerRepository CUSTOMER_REPO = new CustomerRepositoryImpl();
    private static final InvoiceRepository INVOICE_REPO = new InvoiceRepositoryImpl();
    private static final InventoryRepository INVENTORY_REPO = new InventoryRepositoryImpl();
    private static final CategoryRepository CATEGORY_REPO = new CategoryRepositoryImpl();
    private static final LoyaltyRepository LOYALTY_REPO = new LoyaltyRepositoryImpl();
    private static final ShiftRepository SHIFT_REPO = new ShiftRepositoryImpl();

    public static ProductRepository product() { return PRODUCT_REPO; }
    public static EmployeeRepository employee() { return EMPLOYEE_REPO; }
    public static BranchRepository branch() { return BRANCH_REPO; }
    public static CustomerRepository customer() { return CUSTOMER_REPO; }
    public static InvoiceRepository invoice() { return INVOICE_REPO; }
    public static InventoryRepository inventory() { return INVENTORY_REPO; }
    public static CategoryRepository category() { return CATEGORY_REPO; }
    public static LoyaltyRepository loyalty() { return LOYALTY_REPO; }
    public static ShiftRepository shift() { return SHIFT_REPO; }
}
