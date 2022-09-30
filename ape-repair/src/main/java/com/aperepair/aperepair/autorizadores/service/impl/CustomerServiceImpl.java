package com.aperepair.aperepair.autorizadores.service.impl;

import com.aperepair.aperepair.autorizadores.model.Customer;
import com.aperepair.aperepair.autorizadores.repository.CustomerRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class CustomerServiceImpl {

    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity<Customer> create(@RequestBody Customer newCustomer) {
        if (validateCPFNewClient(newCustomer)) {
            customerRepository.save(newCustomer);
            logger.info(String.format("Client: %s registered successfully", newCustomer.toString()));
            return ResponseEntity.status(201).body(newCustomer);
        }
        logger.error("There was an error registering the client");
        return ResponseEntity.status(400).build();
    }

    private boolean validateCPFNewClient(Customer customer) {
        boolean isValid = true; // alterar aqui apos concluir TODO
        if (isValid) {
            logger.info(String.format("CPF: %s is valid", customer.getCpf()));
            return true;
        }
        logger.info("CPF: %s is invalid", customer.getCpf());
        return false;
        //TODO (API CPF) - implementar API de validar cpf
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}
