package com.aperepair.aperepair.application.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateProposalRequestDto {

    @NotNull
    private Integer customerOrderId;

    @NotNull
    private Integer providerId;

    @NotNull
    private Double amount;

    public CreateProposalRequestDto(Integer customerOrderId, Integer providerId, Double amount) {
        this.customerOrderId = customerOrderId;
        this.providerId = providerId;
        this.amount = amount;
    }

    public Integer getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(Integer customerOrderId) {
        this.customerOrderId = customerOrderId;
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