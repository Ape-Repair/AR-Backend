package com.aperepair.aperepair.application.dto.response;

public class OrderUlidResponseDto {

    private String orderId;

    public OrderUlidResponseDto(String id) {
        this.orderId = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
