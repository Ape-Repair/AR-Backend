package com.aperepair.aperepair.authorization.domain.model.dto;

import com.aperepair.aperepair.authorization.domain.model.enums.Genre;
import com.aperepair.aperepair.authorization.domain.model.enums.Role;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProviderDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Email
    private String email;

    private String genre;

    private String role;

    private Boolean isAuthenticated;

    public ProviderDto(String name, String email, String genre, String role, Boolean isAuthenticated) {
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.role = role;
        this.isAuthenticated = isAuthenticated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }
}
