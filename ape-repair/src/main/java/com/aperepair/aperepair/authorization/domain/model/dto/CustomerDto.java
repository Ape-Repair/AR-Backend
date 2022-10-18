package com.aperepair.aperepair.authorization.domain.model.dto;

import com.aperepair.aperepair.authorization.domain.model.enums.Genre;
import com.aperepair.aperepair.authorization.domain.model.enums.Role;
import org.hibernate.validator.constraints.br.CPF;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CustomerDto {

    @NotBlank
    private String name;

    @Email
    private String email;

    private Genre genre;

    @CPF
    private String cpf;

    private Role role;

    private Boolean isAuthenticated;

    public CustomerDto(String name, String email, Genre genre, String cpf, Role role, Boolean isAuthenticated) {
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.cpf = cpf;
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

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", genre=" + genre +
                ", role=" + role +
                ", isAuthenticated=" + isAuthenticated +
                '}';
    }
}
