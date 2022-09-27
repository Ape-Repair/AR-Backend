package com.aperepair.aperepair.autorizadores.model;

import com.aperepair.aperepair.autorizadores.model.enums.Genero;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String email;

    private String senha;

    private Genero genero;

    private String cpf;

    private Telefone telefone;

    private Endereco endereco;
}
