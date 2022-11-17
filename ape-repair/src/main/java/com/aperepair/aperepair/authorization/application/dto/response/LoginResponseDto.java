package com.aperepair.aperepair.authorization.application.dto.response;

import com.aperepair.aperepair.authorization.domain.enums.Role;

public class LoginResponseDto {

    private Integer id;

    private Boolean success;

    private Role role;

    public LoginResponseDto(Integer id, Boolean success, Role role) {
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
