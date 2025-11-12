package edu.ptithcm.services;

import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.dto.response.UserLoginInfo;
import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.utils.CryptoUtil;

public class AuthenticationService {
    private static AuthenticationService instance;

    private static final EmployeeRepository employeeRepo = Repository.employee();

    private AuthenticationService(){}

    public static AuthenticationService getInstance(){
        if (AuthenticationService.instance == null){
            AuthenticationService.instance = new AuthenticationService();
        }

        return AuthenticationService.instance;
    }

    public ResponseDTO<UserLoginInfo> login(String username, String password) {
        try {
            EmployeeModel employee = employeeRepo.findByUsername(username);

            if (employee == null) {
                return new ResponseDTO.Builder<UserLoginInfo>()
                        .type("LOGIN")
                        .status("NOT_FOUND_USER")
                        .message("Tài khoản không tồn tại")
                        .data(null)
                        .build();
            }

            boolean valid = CryptoUtil.verifyPassword(password, employee.getPassword());
            if (!valid) {
                return new ResponseDTO.Builder<UserLoginInfo>()
                        .type("LOGIN")
                        .status("WRONG_PASSWORD")
                        .message("Sai mật khẩu!")
                        .data(null)
                        .build();
            }

            UserLoginInfo user = new UserLoginInfo(
                    employee.getId(),
                    employee.getUsername(),
                    employee.getName(),
                    employee.getRole().name(),
                    employee.getBranch() != null ? employee.getBranch().getId() : null,
                    employee.getBranch() != null ? employee.getBranch().getName() : null
            );

            return new ResponseDTO.Builder<UserLoginInfo>()
                    .type("LOGIN")
                    .status("SUCCESS")
                    .message("Đăng nhập thành công!")
                    .data(user)
                    .build();

        } catch (Exception e) {
            return new ResponseDTO.Builder<UserLoginInfo>()
                    .type("LOGIN")
                    .status("ERROR")
                    .message("Lỗi hệ thống: " + e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
