package com.aperepair.aperepair.application.dto.request;

import com.aperepair.aperepair.application.dto.response.CustomerResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateOrderRequestDto {

    @JsonIgnore
    private String orderId;

    @NotBlank
    private String serviceType;

    @NotBlank
    private String description;

    @NotNull
    private Integer customerId;

    public CreateOrderRequestDto(String serviceType, String description, Integer customerId) {
        orderId = UlidCreator.getUlid().toString();
        this.serviceType = serviceType;
        this.description = description;
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}