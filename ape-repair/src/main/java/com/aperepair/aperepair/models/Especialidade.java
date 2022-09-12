package com.aperepair.aperepair.models;

import javax.persistence.*;

@Entity(name = "especialidade")
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String tipo;

    @Column //ENUM?!
    private String categoria;

    @Column
    private Double valorHora;

    @Column
    private Integer qtdMinutosServico;

    @ManyToOne
    @JoinColumn(name = "id_prestador")
    private Integer idPrestador;

    public Especialidade(
            Integer id,
            String tipo,
            String categoria,
            Double valorHora,
            Integer qtdMinutosServico
    ) {
        this.id = id;
        this.tipo = tipo;
        this.categoria = categoria;
        this.valorHora = valorHora;
        this.qtdMinutosServico = qtdMinutosServico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getValorHora() {
        return valorHora;
    }

    public void setValorHora(Double valorHora) {
        this.valorHora = valorHora;
    }

    public Integer getQtdMinutosServico() {
        return qtdMinutosServico;
    }

    public void setQtdMinutosServico(Integer qtdMinutosServico) {
        this.qtdMinutosServico = qtdMinutosServico;
    }

    @Override
    public String toString() {
        return "Especialidade{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", valorHora=" + valorHora +
                ", qtdMinutosServico=" + qtdMinutosServico +
                '}';
    }
}
