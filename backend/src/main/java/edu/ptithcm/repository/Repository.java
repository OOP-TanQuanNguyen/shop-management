package edu.ptithcm.repository;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.repository.employee.EmployeeRepositoryMySQL;
import edu.ptithcm.repository.product.ProductRepository;
import edu.ptithcm.repository.product.ProductRepositoryMySQL;


public class Repository {
    private static String dbType = "MYSQL"; // mặc định MySQL
    private static EmployeeRepository employeeRepo;
    private static ProductRepository productRepo;

    public static void setDatabaseType(String type) {
        dbType = type;
        initRepositories(); // mỗi lần đổi DB thì init lại
    }

    private static void initRepositories() {
        switch (dbType) {
            case "MYSQL":
                employeeRepo = EmployeeRepositoryMySQL.getInstance();
                productRepo = ProductRepositoryMySQL.getInstance();
                break;
            default:
                break;
        }
    }

    public static EmployeeRepository employee() { return employeeRepo; }
    public static ProductRepository p() { return productRepo; }


    static {
        initRepositories();
    }
}
