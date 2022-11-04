package com.aperepair.aperepair.matchs.domain.model;

import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.matchs.domain.model.enums.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Customer customer;

    @NotNull
    private Provider provider;

    @FutureOrPresent
    private Calendar dateHour;

    @NotNull
    private Double price;

    private Enum serviceType;

    @NotNull
    private Calendar durationInHours;

    private Status status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Calendar getDateHour() {
        return dateHour;
    }

    public void setDateHour(Calendar dateHour) {
        this.dateHour = dateHour;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Enum getServiceType() {
        return serviceType;
    }

    public void setServiceType(Enum serviceType) {
        this.serviceType = serviceType;
    }

    public Calendar getDurationInHours() {
        return durationInHours;
    }

    public void setDurationInHours(Calendar durationInHours) {
        this.durationInHours = durationInHours;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus() {
        this.status = Status.WAITING_FOR_ACCEPTANCE;
    }
}
