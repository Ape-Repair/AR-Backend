package com.aperepair.aperepair.application.dto.request;

import com.aperepair.aperepair.application.dto.response.CustomerResponseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateOrderRequestDto {

    @NotBlank
    private String serviceType;

    @NotBlank
    private String description;

    @NotNull
    private Integer customerId;

    public CreateOrderRequestDto(String serviceType, String description, Integer customerId) {
        this.serviceType = serviceType;
        this.description = description;
        this.customerId = customerId;
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