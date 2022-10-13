package com.aperepair.aperepair.authorization.model.dto;

public class LoginDto {

    private String email;

    private String password;

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
