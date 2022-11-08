package com.aperepair.aperepair.authorization.domain.dto.response;

import com.aperepair.aperepair.authorization.domain.enums.Role;

public class LoginResponseDto {

    private Boolean success;

    private Role role;

    public LoginResponseDto(Boolean success, Role role) {
        this.success = success;
        this.role = role;
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
