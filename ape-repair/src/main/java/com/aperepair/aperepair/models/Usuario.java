package com.aperepair.aperepair.models;
import com.aperepair.aperepair.enums.Genero;
import com.aperepair.aperepair.interfaces.Contrato;

import javax.persistence.*;

public abstract class Usuario implements Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String nome;

    @Column
    private String email;

    @Column
    private String senha;

    @Column
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column
    private String cpf;

    @JoinTable(name="telefone")
    private Telefone telefone;

    @JoinTable(name="endereco")
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

    public abstract Double getValorServico(Servico servico);
}
