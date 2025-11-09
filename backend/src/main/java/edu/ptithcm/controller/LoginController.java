package edu.ptithcm.controller;

import java.sql.SQLException;

import edu.ptithcm.dto.request.login.LoginRequestDTO;
import edu.ptithcm.dto.response.ResponseDTO;
import edu.ptithcm.dto.response.UserLoginInfo;
import edu.ptithcm.services.AuthenticationService;

public class LoginController {
    public static ResponseDTO<UserLoginInfo> handleLogin(LoginRequestDTO request){
        String username = request.getUsername();
        String password = request.getPassword();
        System.out.println("Username : "+username);
        System.out.println("Password : "+password);
        
        try{
            return AuthenticationService.login(username, password);    
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
}
