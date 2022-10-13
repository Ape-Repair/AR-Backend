package com.aperepair.aperepair.authorization.model.dto;

import com.aperepair.aperepair.authorization.model.enums.Genre;

public class CustomerDto {

    private String name;

    private String email;

    private Genre genre;

    private String cpf;

    public CustomerDto(String name, String email, Genre genre, String cpf) {
        this.name = name;
        this.email = email;
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

    @Override
    public String toString() {
        return "CustomerDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", genre=" + genre +
                ", cpf='" + cpf + '\'' +
                '}';
    }
}
