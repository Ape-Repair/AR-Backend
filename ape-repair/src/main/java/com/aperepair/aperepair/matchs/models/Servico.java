package com.aperepair.aperepair.matchs.models;

import com.aperepair.aperepair.autorizadores.model.Cliente;
import com.aperepair.aperepair.autorizadores.model.Prestador;
import com.aperepair.aperepair.autorizadores.model.enums.Categoria;
import com.aperepair.aperepair.autorizadores.model.enums.Status;
import com.aperepair.aperepair.matchs.service.Contrato;
import javax.persistence.*;

public class Servico implements Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String comodo;

    private Status status;

    private String descricao;

    private Double medidaAltura;

    private Double medidaLargura;

    private Categoria categoria;

    private String itemCategoria;

    private Double custoServico;

    private Integer duracaoServicoDias;

    private Integer avaliacao;

    private Cliente cliente;

    private Prestador prestador;

    public Servico(
            Integer id,
            String comodo,
            Status status,
            String descricao,
            Double medidaAltura,
            Double medidaLargura,
            Categoria categoria,
            String itemCategoria,
            Double custoServico,
            Integer duracaoServicoDias,
            Integer avaliacao,
            Cliente cliente,
            Prestador prestador
    ) {
        this.id = id;
        this.comodo = comodo;
        this.status = status;
        this.descricao = descricao;
        this.medidaAltura = medidaAltura;
        this.medidaLargura = medidaLargura;
        this.categoria = categoria;
        this.itemCategoria = itemCategoria;
        this.custoServico = custoServico;
        this.duracaoServicoDias = duracaoServicoDias;
        this.avaliacao = avaliacao;
        this.cliente = cliente;
        this.prestador = prestador;
    }

    @Override
    public Double getValorServico(Servico servico) {
        Double total = 0.0;
        total = (servico.getCustoServico() * servico.getDuracaoServicoDias());
        return total;
    }

    //GETTERS AND SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComodo() {
        return comodo;
    }

    public void setComodo(String comodo) {
        this.comodo = comodo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getMedidaAltura() {
        return medidaAltura;
    }

    public void setMedidaAltura(Double medidaAltura) {
        this.medidaAltura = medidaAltura;
    }

    public Double getMedidaLargura() {
        return medidaLargura;
    }

    public void setMedidaLargura(Double medidaLargura) {
        this.medidaLargura = medidaLargura;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getItemCategoria() {
        return itemCategoria;
    }

    public void setItemCategoria(String itemCategoria) {
        this.itemCategoria = itemCategoria;
    }

    public Double getCustoServico() {
        return custoServico;
    }

    public void setCustoServico(Double custoServico) {
        this.custoServico = custoServico;
    }

    public Integer getDuracaoServicoDias() {
        return duracaoServicoDias;
    }

    public void setDuracaoServicoDias(Integer duracaoServicoDias) {
        this.duracaoServicoDias = duracaoServicoDias;
    }

    public Integer getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Integer avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Prestador getPrestador() {
        return prestador;
    }

    public void setPrestador(Prestador prestador) {
        this.prestador = prestador;
    }

    @Override
    public String toString() {
        return "Servico{" +
                "id=" + id +
                ", comodo='" + comodo + '\'' +
                ", status=" + status +
                ", descricao='" + descricao + '\'' +
                ", medidaAltura=" + medidaAltura +
                ", medidaLargura=" + medidaLargura +
                ", categoria=" + categoria +
                ", itemCategoria='" + itemCategoria + '\'' +
                ", custoServico=" + custoServico +
                ", duracaoServicoDias=" + duracaoServicoDias +
                ", avaliacao=" + avaliacao +
                ", cliente=" + cliente +
                ", prestador=" + prestador +
                '}';
    }
}