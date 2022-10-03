package com.aperepair.aperepair.autorizadores.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Telephone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 8, max = 11)
    private String fixo;

    @Size(min = 11, max = 12)
    private String movel;

    //GETTERS AND SETTERS
    public String getFixo() {
        return fixo;
    }

    public void setFixo(String fixo) {
        this.fixo = fixo;
    }

    public String getMovel() {
        return movel;
    }

    public void setMovel(String movel) {
        this.movel = movel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Telefone{" +
                "id=" + id +
                ", fixo='" + fixo + '\'' +
                ", movel='" + movel + '\'' +
                '}';
    }
}
