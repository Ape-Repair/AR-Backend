package com.aperepair.aperepair.matchs.models;

import com.aperepair.aperepair.autorizadores.model.enums.Zone;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

public class Match {
    private LocalDateTime dataHora; //avaliar necessidade deste atributo

    @Column(name="zona")
    @Enumerated(EnumType.STRING)
    private Zone local;

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

    public Zone getLocal() {
        return local;
    }

    public void setLocal(Zone local) {
        this.local = local;
    }
}
