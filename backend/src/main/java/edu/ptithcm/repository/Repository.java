package edu.ptithcm.repository;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.repository.employee.EmployeeRepositoryMySQL;

public class Repository {

    private static String dbType = "MYSQL"; // mặc định MySQL

    private static EmployeeRepository employeeRepo;

    public static void setDatabaseType(String type) {
        dbType = type;
        initRepositories(); // mỗi lần đổi DB thì init lại
    }

    private static void initRepositories() {
        switch (dbType) {
            case "MYSQL":
                employeeRepo = EmployeeRepositoryMySQL.getInstance();
                break;
            default:
                break;
        }
    }

    public static EmployeeRepository employee() { return employeeRepo; }


    static {
        initRepositories();
    }
}
