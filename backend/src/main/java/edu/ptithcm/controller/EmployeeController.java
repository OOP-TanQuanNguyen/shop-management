package edu.ptithcm.controller;

import java.util.List;
import java.util.Map;

import edu.ptithcm.dto.request.employee.EmployeeRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.EmployeeInfo;
import edu.ptithcm.services.EmployeeService;
import edu.ptithcm.utils.handle_exception.SafeExecutor;

public class EmployeeController {
    private static final EmployeeService service = new EmployeeService();

    public ResponseDTO<List<EmployeeInfo>> getAllEmployees() {
        return SafeExecutor.run(() -> service.getAllEmployees());
    }

    public ResponseDTO<List<EmployeeInfo>> getAllEmployeesActive() {
        return SafeExecutor.run(() -> service.getActiveEmployees());
    }

    public ResponseDTO<EmployeeInfo> createEmployee(EmployeeRequestDTO req) {
        return SafeExecutor.run(() -> service.createEmployee(req));
    }

    public ResponseDTO<EmployeeInfo> updateEmployee(EmployeeRequestDTO req) {
        return SafeExecutor.run(() -> service.updateEmployee(req));
    }

    public ResponseDTO<EmployeeInfo> deleteEmployee(EmployeeRequestDTO req) {
        return SafeExecutor.run(() -> service.deleteEmployee(req));
    }

    @SuppressWarnings("unchecked")
    public ResponseDTO<List<EmployeeInfo>> filterEmployees(Object data) {
        return SafeExecutor.run(() -> service.filterEmployees((Map<String, Object>) data));
    }
}
