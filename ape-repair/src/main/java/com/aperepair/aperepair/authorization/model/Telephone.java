package com.aperepair.aperepair.authorization.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Telephone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "landline")
    @Size(min = 8, max = 20)
    private String landline;

    @Size(min = 10, max = 20)
    private String mobile;

    @NotBlank
    @Size(min = 2, max = 3)
    private String ddd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "id=" + id +
                ", landline='" + landline + '\'' +
                ", mobile='" + mobile + '\'' +
                ", ddd='" + ddd + '\'' +
                '}';
    }
}
