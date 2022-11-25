package com.aperepair.aperepair.application.dto.response;

import org.hibernate.validator.constraints.br.CPF;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class CustomerResponseDto {

    private Integer id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String genre;

    @CPF
    private String cpf;

    @NotBlank
    private String phone;

    private String role;

    private boolean isAuthenticated;

    private AddressResponseDto address;

    public CustomerResponseDto(String name, String email, String genre, String cpf, String phone, String role, boolean isAuthenticated, AddressResponseDto address) {
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.cpf = cpf;
        this.phone = phone;
        this.role = role;
        this.isAuthenticated = isAuthenticated;
        this.address = address;
    }

    public CustomerResponseDto(String name, String email, String genre, String cpf, String phone, String role, boolean isAuthenticated) {
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.cpf = cpf;
        this.phone = phone;
        this.role = role;
        this.isAuthenticated = isAuthenticated;
    }

    public CustomerResponseDto(Integer id, String name, String email, String genre, String cpf, String phone, String role, boolean isAuthenticated, AddressResponseDto address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.cpf = cpf;
        this.phone = phone;
        this.role = role;
        this.isAuthenticated = isAuthenticated;
        this.address = address;
    }

    public CustomerResponseDto(Integer id, String name, String email, String genre, String cpf, String phone, String role, boolean isAuthenticated) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.cpf = cpf;
        this.phone = phone;
        this.role = role;
        this.isAuthenticated = isAuthenticated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public AddressResponseDto getAddress() {
        return address;
    }

    public void setAddress(AddressResponseDto address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerResponseDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", genre='" + genre + '\'' +
                ", cpf='" + cpf + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", address=" + address +
                '}';
    }
}
