package edu.ptithcm.services;

import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.error.ErrorResponse;
import edu.ptithcm.dto.response.error.InvalidResponse;
import edu.ptithcm.dto.response.error.NotFoundResponse;
import edu.ptithcm.dto.response.success.SuccessResponse;
import edu.ptithcm.dto.response.info_models.UserLoginInfo;
import edu.ptithcm.models.EmployeeModel;
import edu.ptithcm.repository.Repository;
import edu.ptithcm.repository.employee.EmployeeRepository;
import edu.ptithcm.utils.CryptoUtil;

public class AuthenticationService {
    private static AuthenticationService instance;
    private static final EmployeeRepository employeeRepo = Repository.employee();

    private AuthenticationService() {}

    public static synchronized AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public ResponseDTO<UserLoginInfo> login(String username, String password) {
        try {
            EmployeeModel employee = employeeRepo.findByUsername(username);
            if (employee == null) {
                return new NotFoundResponse<>("Tài khoản không tồn tại");
            }

            boolean valid = CryptoUtil.verifyPassword(password, employee.getPassword());
            if (!valid) {
                return new InvalidResponse<>("Sai mật khẩu!");
            }

            UserLoginInfo user = new UserLoginInfo(
                    employee.getId(),
                    employee.getUsername(),
                    employee.getName(),
                    employee.getRole().name(),
                    employee.getBranch() != null ? employee.getBranch().getId() : null,
                    employee.getBranch() != null ? employee.getBranch().getName() : null
            );

            return new SuccessResponse<>("Đăng nhập thành công!", user);

        } catch (Exception e) {
            return new ErrorResponse<>("Lỗi hệ thống: " + e.getMessage(), null);
        }
    }
}
