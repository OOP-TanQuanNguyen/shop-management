package edu.ptithcm.services;
import java.util.HashMap;
import java.util.Map;

import edu.ptithcm.model.EmployeeModel;
import edu.ptithcm.model.repository.EmployeeRepo;
import edu.ptithcm.utils.CryptoUtil;

public class AuthenticationService {
    public static Map<String, Object> login(String username, String password) {
        Map<String, Object> response = new HashMap<>();

        EmployeeModel employee = EmployeeRepo.findByUsername(username);

        if (employee == null) {
            response.put("status", "NOT_FOUND_USER");
            return response;
        }
        boolean ok = CryptoUtil.verifyPassword(password, employee.getPasswordHash());   
        if (!ok) {
            response.put("status", "WRONG_PASSWORD");
            return response;
        } 
        
        response.put("status", "SUCCESS");
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", employee.getUsername());
        userData.put("name", employee.getName());
        userData.put("role", employee.getRole());
        userData.put("branch", employee.getBranch());
        userData.put("phone", employee.getPhone());
        userData.put("hireDate", employee.getHireDate());
        userData.put("status", employee.isStatus());
        response.put("data", userData);

        return response;
    }
}
