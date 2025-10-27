package edu.ptithcm.controller;

import java.util.Map;

import edu.ptithcm.services.AuthenticationService;

public class LoginController {
    public static Map<String, Object> handleLogin(Map<String, Object> data) {
        String username = String.valueOf(data.get("username"));
        String password = String.valueOf(data.get("password"));

        System.out.println("Username : "+username);
        System.out.println("Password : "+password);
        
        Map<String,Object> response = AuthenticationService.login(username, password);
        
        response.put("type", "LOGIN");
        if (response.get("status").equals("NOT_FOUND_USER")) {
            response.put("message", "Người dùng không tồn tại");
        }

        if (response.get("status").equals("WRONG_PASSWORD")) {
            response.put("message", "Mật khẩu không đúng");
        }

        if (response.get("status").equals("SUCCESS")) {
            response.put("message", "Đăng nhập thành công");
        }
        
        return response;
    }
}
