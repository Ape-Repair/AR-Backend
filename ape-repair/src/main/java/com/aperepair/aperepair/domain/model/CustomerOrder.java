package com.aperepair.aperepair.domain.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity(name = "Customer_Order")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "service_type")
    private String serviceType;

    @Column(length = 350)
    private String description;

    @NotNull
    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customerId;

    @OneToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private Provider providerId;

    private Double amount;

    @NotBlank
    private String status;

    private boolean paid;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public CustomerOrder() {
        super();
    }

    public CustomerOrder(String serviceType, String description, Customer customerId) {
        this.serviceType = serviceType;
        this.description = description;
        this.customerId = customerId;
    }

    public CustomerOrder(Integer id, String serviceType, String description, Customer customerId, Provider providerId, Double amount, String status, boolean paid, LocalDateTime createdAt) {
        this.id = id;
        this.serviceType = serviceType;
        this.description = description;
        this.customerId = customerId;
        this.providerId = providerId;
        this.amount = amount;
        this.status = status;
        this.paid = paid;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Provider getProviderId() {
        return providerId;
    }

    public void setProviderId(Provider providerId) {
        this.providerId = providerId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}