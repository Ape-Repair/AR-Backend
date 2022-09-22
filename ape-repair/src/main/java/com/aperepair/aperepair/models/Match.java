package com.aperepair.aperepair.models;

import com.aperepair.aperepair.enums.Zonas;
import com.aperepair.aperepair.Contrato;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Match {

    private List<Contrato> contratos; //por instancia deve ter um cliente, um prestador e um servico
    //esta lista virará um repository(conexão com banco de dados) acho, ponto a validar

    private LocalDateTime dataHora; //avaliar necessidade deste atributo

    @Column(name="zona")
    @Enumerated(EnumType.STRING)
    private Zonas local;

    public Match() {
        contratos = new ArrayList();
    }

    public void addServico() {
        return;
    }

    public void cancelarVisita() {
        return;
    }

    public void finalizarServico() {
        //Este metodo deve alterar o status(enum) do serviço;
        return;
    }

    //GETTERS AND SETTERS
    public List<Contrato> getContratos() {
        return contratos;
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Zonas getLocal() {
        return local;
    }

    public void setLocal(Zonas local) {
        this.local = local;
    }
}
