package com.aperepair.aperepair.authorization.domain.model;

import com.aperepair.aperepair.authorization.domain.model.enums.SpecialtyTypes;

import javax.persistence.*;

@Entity
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private SpecialtyTypes specialtyType;

    @ManyToOne
    private Provider provider;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SpecialtyTypes getSpecialtyType() {
        return specialtyType;
    }

    public void setSpecialtyType(SpecialtyTypes specialtyType) {
        this.specialtyType = specialtyType;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
}