package com.aperepair.aperepair.application.dto.response;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ProposalResponseDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proposalId;

    @NotNull
    private String orderId;

    @NotNull
    private Integer providerId;

    @NotBlank
    private String serviceType;

    @Column(length = 350)
    private String description;

    @NotNull
    private Double amount;

    private boolean accepted;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    public ProposalResponseDto(Integer proposalId, String orderId, Integer providerId, String serviceType, String description, Double amount, boolean accepted, LocalDateTime createdAt) {
        this.proposalId = proposalId;
        this.orderId = orderId;
        this.providerId = providerId;
        this.serviceType = serviceType;
        this.description = description;
        this.amount = amount;
        this.accepted = accepted;
        this.createdAt = createdAt;
    }

    public Integer getProposalId() {
        return proposalId;
    }

    public void setProposalId(Integer proposalId) {
        this.proposalId = proposalId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}