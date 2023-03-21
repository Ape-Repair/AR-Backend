package com.aperepair.aperepair.application.dto.response;

public class LoginResponseDto {

    private Integer id;

    private Boolean success;

    private String role;

    public LoginResponseDto(Integer id, Boolean success, String role) {
        this.id = id;
        this.success = success;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
