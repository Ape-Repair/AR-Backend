package com.aperepair.aperepair.authorization.domain.model;

import com.aperepair.aperepair.authorization.domain.model.enums.Genre;
import com.aperepair.aperepair.authorization.domain.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @Column(name = "email", unique = true)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    private Genre genre;

    @Column(name = "cpf", unique = true)
    @NotBlank
    @CPF
    private String cpf;

    @Column(name = "telephone_id")
    private Integer telephoneId;

    @Column(name = "address_id")
    private Integer addressId;

    @JsonIgnore
    private Role role = Role.CUSTOMER;

    @Column(name = "is_authenticated")
    private Boolean isAuthenticated = false;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Integer getTelephoneId() {
        return telephoneId;
    }

    public void setTelephoneId(Integer telephoneId) {
        this.telephoneId = telephoneId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }
}