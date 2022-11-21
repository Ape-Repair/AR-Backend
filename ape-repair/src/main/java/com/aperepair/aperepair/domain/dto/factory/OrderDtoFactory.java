package com.aperepair.aperepair.domain.dto.factory;

import com.aperepair.aperepair.application.dto.request.CreateOrderRequestDto;
import com.aperepair.aperepair.application.dto.response.CustomerResponseDto;
import com.aperepair.aperepair.application.dto.response.OrderResponseDto;
import com.aperepair.aperepair.application.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.domain.enums.Status;
import com.aperepair.aperepair.domain.model.Customer;
import com.aperepair.aperepair.domain.model.CustomerOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class OrderDtoFactory {

    public static CustomerOrder toEntity(CreateOrderRequestDto request, Customer customer, String status) {
        CustomerOrder customerOrder = new CustomerOrder(
                null,
                request.getServiceType(),
                request.getDescription(),
                customer,
                null,
                null,
                status,
                false,
                LocalDateTime.now()
        );
        logger.info("CreateOrderRequestDto transformed to Order entity with success!");
        return customerOrder;
    }

    public static OrderResponseDto toResponseDto(CustomerOrder customerOrder, CustomerResponseDto customerResponseDto, ProviderResponseDto providerResponseDto) {
        OrderResponseDto orderResponseDto = new OrderResponseDto(
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

        logger.info("Order transformed to OrderResponseDto successfully");
        return orderResponseDto;
    }

    private static final Logger logger = LogManager.getLogger(OrderDtoFactory.class.getName());
}
