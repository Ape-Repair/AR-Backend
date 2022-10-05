package com.aperepair.aperepair.autorizadores.model.dto;

import com.aperepair.aperepair.autorizadores.model.enums.Genre;

public class CustomerDto {

    private String name;

    private String email;

    private String password;

    private Genre genre;

    private String cpf;

    public CustomerDto(String name, String email, String password, Genre genre, String cpf) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.genre = genre;
        this.cpf = cpf;
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

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
