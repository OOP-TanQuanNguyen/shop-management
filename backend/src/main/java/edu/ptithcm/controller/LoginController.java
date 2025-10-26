package edu.ptithcm.controller;

import edu.ptithcm.services.LoginService;
import edu.ptithcm.utils.ValidationUtil;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    public static Map<String, Object> handleLogin(Map<String, Object> data) {
        Map<String, Object> result = new HashMap<>();

        String username = String.valueOf(data.get("username"));
        String password = String.valueOf(data.get("password"));

        if (!ValidationUtil.isValidUsername(username) || !ValidationUtil.isValidPassword(password)) {
            result.put("status", "ERROR");
            result.put("message", "Tên đăng nhập hoặc mật khẩu không hợp lệ!");
            return result;
        }

        boolean ok = LoginService.authenticate(username, password);
        if (ok) {
            result.put("status", "OK");
            result.put("message", "Đăng nhập thành công!");
            result.put("username", username);
        } else {
            result.put("status", "FAIL");
            result.put("message", "Sai tên đăng nhập hoặc mật khẩu!");
        }

        return result;
    }
}
