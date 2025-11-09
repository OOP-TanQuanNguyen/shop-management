package edu.ptithcm.services;

import java.sql.SQLException;

import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.dto.response.UserLoginInfo;
import edu.ptithcm.model.EmployeeModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.utils.CryptoUtil;

public class AuthenticationService {
    private static final EmployeeRepository employeeRepository = Repository.employee();
    
    public static ResponseDTO<UserLoginInfo> login(String username, String password) 
            throws SQLException {
        
        EmployeeModel employee = employeeRepository.findByUsername(username);
        
        // User not found
        if (employee == null) {
            return new ResponseDTO.Builder<UserLoginInfo>()
                        .type("LOGIN")
                        .status("NOT_FOUND_USER")
                        .message("User không tồn tại")
                        .data(null)
                        .build();
        }
        
        // Wrong password
        boolean ok = CryptoUtil.verifyPassword(password, employee.getPasswordHash());   
        if (!ok) {
            return new ResponseDTO.Builder<UserLoginInfo>()
                        .type("LOGIN")
                        .status("WRONG_PASSWORD")
                        .message("Mật khẩu không chính xác!")
                        .data(null)
                        .build();
        } 
        
        // Success
        UserLoginInfo userInfo = new UserLoginInfo(
            employee.getId(),
            employee.getUsername(),
            employee.getName(),
            employee.getRole(),
            employee.getBranchId(),
            employee.getBranch()
        );
        
        return new ResponseDTO.Builder<UserLoginInfo>()
                    .type("LOGIN")
                    .status("SUCCESS")
                    .message("Đăng nhập thành công!")
                    .data(userInfo)
                    .build();
    }
}