package com.aperepair.aperepair.matchs.models;

import com.aperepair.aperepair.autorizadores.model.enums.Zonas;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public class Match {
    private LocalDateTime dataHora; //avaliar necessidade deste atributo

    @Column(name="zona")
    @Enumerated(EnumType.STRING)
    private Zonas local;

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
