package com.aperepair.aperepair.authorization.domain.model;

import com.aperepair.aperepair.authorization.domain.model.enums.Uf;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 30)
    private String street;

    @Column(name = "street_number")
    @NotBlank
    @Min(value = 1)
    private Integer streetNumber;

    @Size(min = 3, max = 50)
    private String complement;

    @NotBlank
    @Size(min = 8, max = 8)
    private String cep;

    @NotBlank
    @Size(min = 2, max = 20)
    private String district;

    @NotBlank
    @Size(min = 3, max = 30)
    private String city;

    @NotBlank
    private Uf uf;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Uf getUf() {
        return uf;
    }

    public void setUf(Uf uf) {
        this.uf = uf;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", number=" + streetNumber +
                ", complement='" + complement + '\'' +
                ", cep='" + cep + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", uf=" + uf +
                '}';
    }
}
