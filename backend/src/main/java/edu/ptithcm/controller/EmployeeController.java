package edu.ptithcm.controller;

import edu.ptithcm.dto.EmployeeDTO;
import edu.ptithcm.dto.RequestObject;
import edu.ptithcm.dto.ResponseObject;
import edu.ptithcm.model.EmployeeModel;
import edu.ptithcm.services.EmployeeService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController() {
        this.employeeService = new EmployeeService();
    }

    // Lấy danh sách toàn bộ nhân viên
    public ResponseObject getAllEmployees(RequestObject req) {
        try {
            int limit = req.getInt("limit", 50);
            List<EmployeeDTO> dtos = employeeService.getAllEmployees(limit);
            return new ResponseObject()
                    .setStatus("SUCCESS")
                    .setMessage("Fetched all employees successfully")
                    .setData(dtos);
        } catch (SQLException e) {
            return new ResponseObject()
                    .setStatus("ERROR")
                    .setMessage("Database error: " + e.getMessage());
        }
    }

    // Lấy danh sách nhân viên active
    public ResponseObject getAllEmployeesActive(RequestObject req) {
        try {
            int limit = req.getInt("limit", 50);
            List<EmployeeDTO> dtos = employeeService.getAllEmployeesActive(limit);
            return new ResponseObject()
                    .setStatus("SUCCESS")
                    .setMessage("Fetched active employees successfully")
                    .setData(dtos);
        } catch (SQLException e) {
            return new ResponseObject()
                    .setStatus("ERROR")
                    .setMessage("Database error: " + e.getMessage());
        }
    }

    // Lấy danh sách nhân viên unactive
    public ResponseObject getAllEmployeesUnactive(RequestObject req) {
        try {
            int limit = req.getInt("limit", 50);
            List<EmployeeDTO> dtos = employeeService.getAllEmployeesUnactive(limit);
            return new ResponseObject()
                    .setStatus("SUCCESS")
                    .setMessage("Fetched inactive employees successfully")
                    .setData(dtos);
        } catch (SQLException e) {
            return new ResponseObject()
                    .setStatus("ERROR")
                    .setMessage("Database error: " + e.getMessage());
        }
    }

    // Tạo nhân viên mới
    public ResponseObject createEmployee(RequestObject req) {
        try {
            String username = req.getString("username", null);
            String password = req.getString("password", null);
            String name = req.getString("name", null);
            String phone = req.getString("phone", null);
            String role = req.getString("role", "STAFF");

            if (username == null || password == null || name == null) {
                return new ResponseObject()
                        .setStatus("ERROR")
                        .setMessage("Missing required fields");
            }

            EmployeeDTO empDTO = employeeService.createEmployee(username, password, name, phone, role);
            if (empDTO == null) {
                return new ResponseObject()
                        .setStatus("ERROR")
                        .setMessage("Username already exists");
            }

            return new ResponseObject()
                    .setStatus("SUCCESS")
                    .setMessage("Employee created successfully")
                    .setData(empDTO);

        } catch (SQLException e) {
            return new ResponseObject()
                    .setStatus("ERROR")
                    .setMessage("Database error: " + e.getMessage());
        }
    }

    // Cập nhật thông tin nhân viên
    public ResponseObject updateEmployee(RequestObject req) {
        try {
            String employeeId = req.getString("employeeId", null);
            if (employeeId == null) {
                return new ResponseObject()
                        .setStatus("ERROR")
                        .setMessage("Missing employeeId");
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> fields = (Map<String, Object>) req.get("fields");
            if (fields == null || fields.isEmpty()) {
                return new ResponseObject()
                        .setStatus("ERROR")
                        .setMessage("No fields to update");
            }

            employeeService.updateEmployee(employeeId, fields);
            return new ResponseObject()
                    .setStatus("SUCCESS")
                    .setMessage("Employee updated successfully");

        } catch (SQLException e) {
            return new ResponseObject()
                    .setStatus("ERROR")
                    .setMessage("Database error: " + e.getMessage());
        }
    }

    // Xóa nhân viên
    public ResponseObject deleteEmployee(RequestObject req) {
        try {
            String employeeId = req.getString("employeeId", null);
            if (employeeId == null) {
                return new ResponseObject()
                        .setStatus("ERROR")
                        .setMessage("Missing employeeId");
            }

            employeeService.removeEmployee(employeeId);
            return new ResponseObject()
                    .setStatus("SUCCESS")
                    .setMessage("Employee deleted successfully");

        } catch (SQLException e) {
            return new ResponseObject()
                    .setStatus("ERROR")
                    .setMessage("Database error: " + e.getMessage());
        }
    }

    // Lọc nhân viên theo filter
    public ResponseObject filterEmployees(RequestObject req) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> filters = (Map<String, Object>) req.get("filters");
            int limit = req.getInt("limit", 50);

            List<EmployeeDTO> dtos = employeeService.filterEmployees(filters, limit);

            return new ResponseObject()
                    .setStatus("SUCCESS")
                    .setMessage("Filtered employees successfully")
                    .setData(dtos);

        } catch (SQLException e) {
            return new ResponseObject()
                    .setStatus("ERROR")
                    .setMessage("Database error: " + e.getMessage());
        }
    }
}
