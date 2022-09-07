package com.aperepair.aperepair.models;
import com.aperepair.aperepair.enums.Genero;
import com.aperepair.aperepair.interfaces.Contrato;

public abstract class Usuario implements Contrato {

    private Integer id;

    private String nome;

    private String email;

    private String senha;

    private Genero genero;

    private String cpf;

    private Telefone telefone;

    private Endereco endereco;

    public Usuario(
            Integer id,
            String nome,
            String email,
            String senha,
            Genero genero,
            String cpf,
            Telefone telefone,
            Endereco endereco
    ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
    }
}
