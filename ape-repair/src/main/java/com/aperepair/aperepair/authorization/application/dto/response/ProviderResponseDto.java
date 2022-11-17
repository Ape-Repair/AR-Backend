package com.aperepair.aperepair.authorization.application.dto.response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProviderResponseDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Email
    private String email;

    private String genre;

    private String cpf;

    private String phone;

    private String role;

    private Boolean isAuthenticated;

    private AddressResponseDto address;

    public ProviderResponseDto(String name, String email, String genre, String cpf, String phone, String role, Boolean isAuthenticated, AddressResponseDto address) {
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.cpf = cpf;
        this.phone = phone;
        this.role = role;
        this.isAuthenticated = isAuthenticated;
        this.address = address;
    }

    public ProviderResponseDto(String name, String email, String genre, String cpf, String phone, String role, Boolean isAuthenticated) {
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.cpf = cpf;
        this.phone = phone;
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

    public Boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AddressResponseDto getAddress() {
        return address;
    }

    public void setAddress(AddressResponseDto address) {
        this.address = address;
    }
}