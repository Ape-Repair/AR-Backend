package com.aperepair.aperepair.reports.domain.entity.dto.response;

import com.aperepair.aperepair.authorization.domain.model.enums.Role;
import com.aperepair.aperepair.reports.domain.entity.Admin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AdminResponseDto {

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    private Role role;

    public AdminResponseDto(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AdminResponseDto{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }

    public static AdminResponseDto toDto(Admin admin) {
        AdminResponseDto adminResponseDto = new AdminResponseDto(
                admin.getUsername(),
                admin.getRole()
        );

        logger.info("Admin transformed to adminResponseDTO with successfully");
        return adminResponseDto;
    }

    private static final Logger logger = LogManager.getLogger(AdminResponseDto.class.getName());
}
