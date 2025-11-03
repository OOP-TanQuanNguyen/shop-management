package edu.ptithcm.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ptithcm.model.EmployeeModel;
import edu.ptithcm.model.repository.employee.EmployeeRepositoryMySQL;
import edu.ptithcm.utils.CryptoUtil;

public class EmployeeService {
    public static final EmployeeRepositoryMySQL employeeRepositoryMySQL = new EmployeeRepositoryMySQL();

    public static EmployeeModel createEmployee(String username, String rawPassword, String name, 
                                               String phone, String role) throws SQLException {
        if(employeeRepositoryMySQL.checkEmployeeExists(username)) {
            return null; 
        }
        String hashedPassword = CryptoUtil.md5(rawPassword);
        EmployeeModel employee;
        if ("ADMIN".equalsIgnoreCase(role)) {
            employee = new EmployeeModel.AdminBuilder()
                            .username(username)
                            .password(hashedPassword)
                            .name(name)
                            .phone(phone)
                            .status(true)
                            .build();
        } else {
            employee = new EmployeeModel.Builder()
                            .username(username)
                            .password(hashedPassword)
                            .name(name)
                            .phone(phone)
                            .role("STAFF")
                            .status(true)
                            .build();
        }

        List<EmployeeModel> list = new ArrayList<>();
        list.add(employee);
        employeeRepositoryMySQL.createEmployee(list);
        return employee;
    }
}
