package edu.ptithcm.repository.employee;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.ptithcm.model.EmployeeModel;

public interface EmployeeRepository {
    public boolean checkEmployeeExists(String username) throws SQLException;
    public EmployeeModel findByUsername(String username) throws SQLException;
    public void createEmployee(List<EmployeeModel> employees) throws SQLException;
    public void updateEmployee(String employeeId, Map<String, Object> fields) throws SQLException;
    public void removeEmployee(String employeeId) throws SQLException;
    public List<EmployeeModel> getAllEmployees(int limit) throws SQLException;
    public List<EmployeeModel> getAllEmployeesActive(int limit) throws SQLException;
    public List<EmployeeModel> getAllEmployeesUnactive(int limit) throws SQLException;
    public List<EmployeeModel> searchEmployees(String keyword) throws SQLException;
    public List<EmployeeModel> filterEmployees(Map<String, Object> filters) throws SQLException;
}
