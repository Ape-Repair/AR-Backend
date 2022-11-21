package com.aperepair.aperepair.domain.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 50)
    private String street;

    @Column(name = "street_number")
    @NotNull
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

    @NotNull
    private String uf;

    public Address(String street, Integer streetNumber, String complement, String cep, String district, String city, String uf) {
        this.street = street;
        this.streetNumber = streetNumber;
        this.complement = complement;
        this.cep = cep;
        this.district = district;
        this.city = city;
        this.uf = uf;
    }

    public Address() {
        super();
    }

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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
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
