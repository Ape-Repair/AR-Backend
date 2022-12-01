package com.aperepair.aperepair.application.dto.response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class OrderResponseDto {

    @NotNull
    private String orderId;

    @NotBlank
    private String serviceType;

    @NotBlank
    private String description;

    @NotNull
    private CustomerResponseDto customerId;

    private ProviderResponseDto providerId;

    private Double amount;

    @NotBlank
    private String status;

    private boolean paid;

    private LocalDateTime createdAt;

    public OrderResponseDto(String orderId, String serviceType, String description, CustomerResponseDto customerId, ProviderResponseDto providerId, Double amount, String status, boolean paid, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.serviceType = serviceType;
        this.description = description;
        this.customerId = customerId;
        this.providerId = providerId;
        this.amount = amount;
        this.status = status;
        this.paid = paid;
        this.createdAt = createdAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public CustomerResponseDto getCustomerId() {
        return customerId;
    }

    public void setCustomerId(CustomerResponseDto customerId) {
        this.customerId = customerId;
    }

    public ProviderResponseDto getProviderId() {
        return providerId;
    }

    public void setProviderId(ProviderResponseDto providerId) {
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