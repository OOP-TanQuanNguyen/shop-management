package edu.ptithcm.services;

import java.util.*;

import edu.ptithcm.model.EmployeeModel;
import edu.ptithcm.model.repository.EmployeeRepo;
import edu.ptithcm.utils.CryptoUtil;

public class EmployeeService {
    public static EmployeeModel createEmployee(String username, String rawPassword, String name, 
                                               String phone, String role) {
        if(EmployeeRepo.checkEmployeeExists(username)) {
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
        EmployeeRepo.createEmployee(list);

        return employee;
    }
}
