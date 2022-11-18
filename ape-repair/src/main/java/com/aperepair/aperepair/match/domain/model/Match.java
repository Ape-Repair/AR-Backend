package com.aperepair.aperepair.match.domain.model;

import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.model.Provider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotBlank
    private String status;

    private Customer customer;

    private Provider provider;

    @NotNull
    @DecimalMin(value = "20.0")
    private Double price;

    @NotNull
    private boolean paid;
}