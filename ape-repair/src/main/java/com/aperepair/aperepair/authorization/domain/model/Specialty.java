package com.aperepair.aperepair.authorization.domain.model;

import javax.persistence.*;

@Entity
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type;

    private String category;

    @Column(name = "hour_value")
    private Double hourValue;

    @Column(name = "provider_id")
    private Integer providerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getHourValue() {
        return hourValue;
    }

    public void setHourValue(Double hourValue) {
        this.hourValue = hourValue;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    @Override
    public String toString() {
        return "Especialidade{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", hourValue=" + hourValue +
                ", providerId=" + providerId +
                '}';
    }
}
