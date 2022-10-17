package com.aperepair.aperepair.authorization.model.dto.factory;

import com.aperepair.aperepair.authorization.model.dto.CustomerDto;
import com.aperepair.aperepair.authorization.model.Customer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerDtoFactory {

    public static CustomerDto toDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto(
                customer.getName(),
                customer.getEmail(),
                customer.getGenre(),
                customer.getCpf(),
                customer.getRole(),
                customer.getAuthenticated()
        );

        logger.info("Customer transformed to DTO with successfully");
        return customerDto;
    }

    private static final Logger logger = LogManager.getLogger(CustomerDtoFactory.class.getName());
}
