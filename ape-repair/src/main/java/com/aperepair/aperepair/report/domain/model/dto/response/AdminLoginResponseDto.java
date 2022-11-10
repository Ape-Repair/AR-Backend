package com.aperepair.aperepair.report.domain.model.dto.response;

public class AdminLoginResponseDto {

    private String username;

    private Boolean success;

    public AdminLoginResponseDto(String username, Boolean success) {
        this.username = username;
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
