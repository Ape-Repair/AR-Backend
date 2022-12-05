package com.aperepair.aperepair.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity(name = "Dashboard")
public class Dashboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount_paid")
    @NotNull
    private Double amountPaid;

    @Column(name = "active_providers")
    private long activeProviders;

    @Column(name = "active_customers")
    private long activeCustomers;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Dashboard(Double amountPaid, long activeProviders, long activeCustomers) {
        this.amountPaid = amountPaid;
        this.activeProviders = activeProviders;
        this.activeCustomers = activeCustomers;
        createdAt = LocalDateTime.now();
    }

    public Dashboard(Integer id, Double amountPaid, long activeProviders, long activeCustomers, LocalDateTime createdAt) {
        this.id = id;
        this.amountPaid = amountPaid;
        this.activeProviders = activeProviders;
        this.activeCustomers = activeCustomers;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public long getActiveProviders() {
        return activeProviders;
    }

    public void setActiveProviders(long activeProviders) {
        this.activeProviders = activeProviders;
    }

    public long getActiveCustomers() {
        return activeCustomers;
    }

    public void setActiveCustomers(long activeCustomers) {
        this.activeCustomers = activeCustomers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
