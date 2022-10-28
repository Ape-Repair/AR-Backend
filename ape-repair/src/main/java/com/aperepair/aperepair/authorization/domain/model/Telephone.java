package com.aperepair.aperepair.authorization.domain.model;

import javax.persistence.*;
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
                '}';
    }
}
