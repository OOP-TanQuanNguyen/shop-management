package edu.ptithcm.dto.request.login;

public class LoginRequestDTO{
    private final String username;
    private final String password;

    public LoginRequestDTO(String username,String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
}
