package com.aperepair.aperepair.application.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CreateProposalRequestDto {

    @NotNull
    private String orderId;

    @NotNull
    private Integer providerId;

    @Positive
    private Double amount;

    public CreateProposalRequestDto(String orderId, Integer providerId, Double amount) {
        this.orderId = orderId;
        this.providerId = providerId;
        this.amount = amount;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}