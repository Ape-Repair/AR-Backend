package com.aperepair.aperepair.autorizadores.model;

import com.aperepair.aperepair.autorizadores.model.enums.Genre;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Prestador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String email;

    private String senha;

    private Genre genero;

    private String cpf;

    private Telephone telefone;

    private Address endereco;

    private String cnpj;
}
