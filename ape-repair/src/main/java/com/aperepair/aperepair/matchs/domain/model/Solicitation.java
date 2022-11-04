package com.aperepair.aperepair.matchs.domain.model;

import com.aperepair.aperepair.authorization.domain.model.Customer;

import javax.persistence.Entity;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

public class Solicitation {

    @NotNull
    private Customer customer;

    private Enum serviceType;

    @NotBlank
    private String description;

    private Integer quantity;

    @FutureOrPresent
    private Calendar chosenDateHour;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Enum getServiceType() {
        return serviceType;
    }

    public void setServiceType(Enum serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Calendar getChosenDateHour() {
        return chosenDateHour;
    }

    public void setChosenDateHour(Calendar chosenDateHour) {
        this.chosenDateHour = chosenDateHour;
    }
}
