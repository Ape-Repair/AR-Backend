package com.aperepair.aperepair.models;

public class Telefone {

    private Integer id;
    private String fixo;
    private String movel;

    public Telefone(String fixo, String movel) {
        this.fixo = fixo;
        this.movel = movel;
    }

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