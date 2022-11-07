package com.aperepair.aperepair.authorization.domain.model;

import com.aperepair.aperepair.authorization.domain.model.enums.Genre;
import com.aperepair.aperepair.authorization.domain.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank
    @Column(name = "phone", unique = true)
    @Size(min = 10, max = 25)
    private String phone;

    //TODO (PRIORITARIO) - Ajustar o código para criar uma relação entre Customer e Address (2)
    //TODO (Ajustar collection do postman) e testar(3)... Conectar com Azure (1)
//    @OneToOne
//    private Address address;

    @JsonIgnore
    private Role role = Role.CUSTOMER;

    @Column(name = "is_authenticated")
    private boolean isAuthenticated;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    @Override
    public String toString() {
        return "Customer{" +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", phone'" + phone + '\'' +
                '}';
    }
}