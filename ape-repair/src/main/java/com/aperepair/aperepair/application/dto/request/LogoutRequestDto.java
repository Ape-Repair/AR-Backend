package com.aperepair.aperepair.application.dto.request;

import javax.validation.constraints.Email;

public class LogoutRequestDto {

    @Email
    private String email;

    public LogoutRequestDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
