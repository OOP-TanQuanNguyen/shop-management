package edu.ptithcm.controller;

import java.util.List;
import java.util.Map;

import edu.ptithcm.dto.request.employee.EmployeeRequestDTO;
import edu.ptithcm.dto.response.EmployeeInfo;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.services.EmployeeService;

public class EmployeeController {
    private static final EmployeeService service = new EmployeeService();

    public ResponseDTO<List<EmployeeInfo>> getAllEmployees() {
        return service.getAllEmployees();
    }

    public ResponseDTO<List<EmployeeInfo>> getAllEmployeesActive() {
        return service.getActiveEmployees();
    }

    public ResponseDTO<EmployeeInfo> createEmployee(EmployeeRequestDTO req) {
        return service.createEmployee(req);
    }

    public ResponseDTO<EmployeeInfo> updateEmployee(EmployeeRequestDTO req) {
        return service.updateEmployee(req);
    }

    public ResponseDTO<EmployeeInfo> deleteEmployee(EmployeeRequestDTO req) {
        return service.deleteEmployee(req);
    }

    @SuppressWarnings("unchecked")
    public ResponseDTO<List<EmployeeInfo>> filterEmployees(Object data) {
        return service.filterEmployees((Map<String, Object>) data);
    }
}
