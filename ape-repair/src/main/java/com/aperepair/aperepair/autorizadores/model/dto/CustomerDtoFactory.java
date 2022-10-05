package com.aperepair.aperepair.autorizadores.model.dto;

import com.aperepair.aperepair.autorizadores.model.Customer;
import com.aperepair.aperepair.autorizadores.service.impl.CustomerServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerDtoFactory {

    public static CustomerDto toDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto(
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getGenre(),
                customer.getCpf()
        );

        logger.info("Customer transformed to DTO with successfully");
        return customerDto;
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}