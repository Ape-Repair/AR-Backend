package com.aperepair.aperepair.authorization.domain.dto.factory;

import com.aperepair.aperepair.authorization.domain.dto.response.CustomerResponseDto;
import com.aperepair.aperepair.authorization.domain.model.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerDtoFactory {

    public static CustomerResponseDto toDto(Customer customer) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto(
                customer.getName(),
                customer.getEmail(),
                customer.getGenre(),
                customer.getCpf(),
                customer.getPhone(),
                customer.getRole(),
                customer.isAuthenticated()
        );

        logger.info("Customer transformed to DTO with successfully");
        return customerResponseDto;
    }

    private static final Logger logger = LogManager.getLogger(CustomerDtoFactory.class.getName());
}
