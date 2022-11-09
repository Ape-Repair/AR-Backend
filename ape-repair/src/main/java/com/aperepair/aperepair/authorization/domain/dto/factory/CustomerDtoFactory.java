package com.aperepair.aperepair.authorization.domain.dto.factory;

import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.CustomerResponseDto;
import com.aperepair.aperepair.authorization.domain.model.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerDtoFactory {

    public static Customer toEntity(CustomerRequestDto customerRequestDto) {
        Customer customer = new Customer(
                null,
                customerRequestDto.getName(),
                customerRequestDto.getEmail(),
                customerRequestDto.getPassword(),
                customerRequestDto.getGenre(),
                customerRequestDto.getCpf(),
                customerRequestDto.getPhone(),
                null,
                customerRequestDto.getRole(),
                customerRequestDto.isAuthenticated()
        );
        logger.info("CustomerRequestDto transformed to Customer entity with successfully");
        return customer;
    }

    public static CustomerResponseDto toResponseDto(Customer customer) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto(
                customer.getName(),
                customer.getEmail(),
                customer.getGenre(),
                customer.getCpf(),
                customer.getPhone(),
                customer.getRole(),
                customer.isAuthenticated()
        );

        logger.info("Customer transformed to CustomerResponseDto with successfully");
        return customerResponseDto;
    }

    private static final Logger logger = LogManager.getLogger(CustomerDtoFactory.class.getName());
}
