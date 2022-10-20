package com.aperepair.aperepair.reports.domain.entity.dto.request;

import com.aperepair.aperepair.authorization.domain.model.enums.Role;

public class AdminLoginRequestDto {

    private String username;

    private String password;

    private Role role;

    public AdminLoginRequestDto(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AdminLoginRequestDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
