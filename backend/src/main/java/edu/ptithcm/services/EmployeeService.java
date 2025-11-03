package edu.ptithcm.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ptithcm.model.EmployeeModel;
import edu.ptithcm.model.repository.employee.EmployeeRepositoryMySQL;
import edu.ptithcm.utils.CryptoUtil;
import edu.ptithcm.dto.EmployeeDTO;

public class EmployeeService {
    public static final EmployeeRepositoryMySQL employeeRepositoryMySQL = new EmployeeRepositoryMySQL();

    public EmployeeDTO createEmployee(String username, String rawPassword, String name, 
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

        // Lưu vào database
        employeeRepositoryMySQL.createEmployee(List.of(employee));

        // Trả về DTO
        return EmployeeDTO.fromModel(employee);
    }
    
    // Cập nhật thông tin nhân viên
    public void updateEmployee(String employeeId, Map<String,Object> fields) throws SQLException {
        if(employeeId == null || fields == null || fields.isEmpty()) return;
        employeeRepositoryMySQL.updateEmployee(employeeId, fields);
    }

    // Xóa nhân viên
    public void removeEmployee(String employeeId) throws SQLException {
        if(employeeId == null) return;
        employeeRepositoryMySQL.removeEmployee(employeeId);
    }

    // Lấy danh sách tất cả nhân viên (Model)
    public List<EmployeeDTO> getAllEmployees(int limit) throws SQLException {
        List<EmployeeModel> models = employeeRepositoryMySQL.getAllEmployees(limit);
        return models.stream().map(EmployeeDTO::fromModel).collect(Collectors.toList());
    }

    public List<EmployeeDTO> getAllEmployeesActive(int limit) throws SQLException {
        List<EmployeeModel> models = employeeRepositoryMySQL.getAllEmployeesActive(limit);
        return models.stream().map(EmployeeDTO::fromModel).collect(Collectors.toList());
    }

    public List<EmployeeDTO> getAllEmployeesUnactive(int limit) throws SQLException {
        List<EmployeeModel> models = employeeRepositoryMySQL.getAllEmployeesUnactive(limit);
        return models.stream().map(EmployeeDTO::fromModel).collect(Collectors.toList());
    }


    // Tìm kiếm/filter nhân viên theo từ khóa
    public List<EmployeeDTO> searchEmployees(String keyword) throws SQLException {
        List<EmployeeModel> models = employeeRepositoryMySQL.searchEmployees(keyword);
        return models.stream().map(EmployeeDTO::fromModel).collect(Collectors.toList());
    }

    // Filter theo map filters + limit
    public List<EmployeeDTO> filterEmployees(Map<String,Object> filters, int limit) throws SQLException {
        List<EmployeeModel> models = employeeRepositoryMySQL.filterEmployees(filters);
        return models.stream()
                     .limit(limit)
                     .map(EmployeeDTO::fromModel)
                     .collect(Collectors.toList());
    }
}
