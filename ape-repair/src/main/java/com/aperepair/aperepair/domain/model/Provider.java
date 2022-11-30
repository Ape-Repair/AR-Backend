package com.aperepair.aperepair.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity(name = "Provider")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 60)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    private String genre;

    @NotBlank
    @Column(name = "cpf", unique = true)
    @CPF
    private String cpf;

    @NotBlank
    @Column(name = "phone", unique = true)
    @Size(min = 10, max = 25)
    private String phone;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address addressId;

    @Column(name = "specialty_type")
    @NotBlank
    private String specialtyType;

    @JsonIgnore
    private String role;

    @Column(name = "is_authenticated")
    private Boolean isAuthenticated;

    public Provider() {
        super();
    }

    public Provider(Integer id, String name, String email, String password, String genre, String cpf, String phone, Address addressId, String specialtyType, String role, Boolean isAuthenticated) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.genre = genre;
        this.cpf = cpf;
        this.phone = phone;
        this.addressId = addressId;
        this.specialtyType = specialtyType;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Address getAddressId() {
        return addressId;
    }

    public void setAddressId(Address addressId) {
        this.addressId = addressId;
    }

    public Boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getSpecialtyType() {
        return specialtyType;
    }

    public void setSpecialtyType(String specialtyType) {
        this.specialtyType = specialtyType;
    }
}