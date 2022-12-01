package com.aperepair.aperepair.domain.dto.factory;

import com.aperepair.aperepair.application.dto.request.CreateOrderRequestDto;
import com.aperepair.aperepair.application.dto.response.CustomerResponseDto;
import com.aperepair.aperepair.application.dto.response.OrderResponseDto;
import com.aperepair.aperepair.application.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.domain.model.Customer;
import com.aperepair.aperepair.domain.model.CustomerOrder;

import java.time.LocalDateTime;

public class OrderDtoFactory {

    public static CustomerOrder toEntity(CreateOrderRequestDto request, Customer customer, String status) {

        return new CustomerOrder(
                request.getOrderId(),
                request.getServiceType(),
                request.getDescription(),
                customer,
                null,
                null,
                status,
                false,
                LocalDateTime.now()
        );
    }

    public static OrderResponseDto toResponseDto(CustomerOrder customerOrder, CustomerResponseDto customerResponseDto, ProviderResponseDto providerResponseDto) {

        return new OrderResponseDto(
                customerOrder.getId(),
                customerOrder.getServiceType(),
                customerOrder.getDescription(),
                customerResponseDto,
                providerResponseDto,
                customerOrder.getAmount(),
                customerOrder.getStatus(),
                customerOrder.isPaid(),
                customerOrder.getCreatedAt()
        );
    }

    public static OrderResponseDto toResponseWithNullProviderDto(
            CustomerOrder customerOrder,
            CustomerResponseDto
                    customerResponseDto
    ) {

        return new OrderResponseDto(
                customerOrder.getId(),
                customerOrder.getServiceType(),
                customerOrder.getDescription(),
                customerResponseDto,
                null,
                customerOrder.getAmount(),
                customerOrder.getStatus(),
                customerOrder.isPaid(),
                customerOrder.getCreatedAt()
        );
    }

    public static OrderResponseDto toResponseWithNotNullProviderDto(CustomerOrder customerOrder, CustomerResponseDto
            customerResponseDto, ProviderResponseDto providerResponseDto) {

        return new OrderResponseDto(
                customerOrder.getId(),
                customerOrder.getServiceType(),
                customerOrder.getDescription(),
                customerResponseDto,
                providerResponseDto,
                customerOrder.getAmount(),
                customerOrder.getStatus(),
                customerOrder.isPaid(),
                customerOrder.getCreatedAt()
        );
    }
}