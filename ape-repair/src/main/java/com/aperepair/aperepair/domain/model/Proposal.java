package com.aperepair.aperepair.domain.model;

import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_order_id", referencedColumnName = "id")
    private CustomerOrder customerOrderId;

    @OneToOne
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    private Provider providerId;

    private String serviceType;

    @Column(length = 350)
    private String description;

    private Double amount;

    private boolean accepted;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Proposal(String serviceType, String description, Double amount, boolean accepted, LocalDateTime createdAt) {
        this.serviceType = serviceType;
        this.description = description;
        this.amount = amount;
        this.accepted = accepted;
        this.createdAt = createdAt;
    }

    public Proposal() {
        super();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
