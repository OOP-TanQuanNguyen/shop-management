package edu.ptithcm.controller;

import edu.ptithcm.dto.request.login.LoginRequestDTO;
import edu.ptithcm.dto.response.base.ResponseDTO;
import edu.ptithcm.dto.response.info_models.UserLoginInfo;
import edu.ptithcm.services.AuthenticationService;

public class LoginController {

    private static LoginController instance;
    private AuthenticationService service; 

    private LoginController(){
        this.service = AuthenticationService.getInstance();
    }

    public static LoginController getInstance(){
        if (LoginController.instance == null){
            LoginController.instance = new LoginController();
        }
        return LoginController.instance;
    }

    public ResponseDTO<UserLoginInfo> handleLogin(LoginRequestDTO request){
        String username = request.getUsername();
        String password = request.getPassword();        
        return this.service.login(username, password);    

    }
}
