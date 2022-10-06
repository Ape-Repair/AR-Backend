package com.aperepair.aperepair.authorization.model.dto;

import com.aperepair.aperepair.authorization.model.enums.Genre;

public class ProviderDto {

    private String name;

    private String email;

    private Genre genre;

    private String cpf;

    private String cnpj;

    public ProviderDto(String name, String email, Genre genre, String cpf, String cnpj) {
        this.name = name;
        this.email = email;
        this.genre = genre;
        this.cpf = cpf;
        this.cnpj = cnpj;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "ProviderDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", genre=" + genre +
                ", cpf='" + cpf + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}
