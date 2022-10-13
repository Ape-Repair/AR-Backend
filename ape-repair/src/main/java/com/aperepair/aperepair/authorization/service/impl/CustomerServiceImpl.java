package com.aperepair.aperepair.authorization.service.impl;

import com.aperepair.aperepair.authorization.model.Customer;
import com.aperepair.aperepair.authorization.model.dto.CustomerDto;
import com.aperepair.aperepair.authorization.model.dto.factory.CustomerDtoFactory;
import com.aperepair.aperepair.authorization.repository.CustomerRepository;
import com.aperepair.aperepair.authorization.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public ResponseEntity<CustomerDto> create(@RequestBody Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        logger.info("Customer password encrypted with successfully");

        customerRepository.save(customer);
        logger.info(String.format("Customer: %s registered successfully", customer.toString()));

        CustomerDto customerDto = CustomerDtoFactory.toDto(customer);

        return ResponseEntity.status(201).body(customerDto);
    }

    @Override
    public ResponseEntity<List<Customer>> findAll() {
        List<Customer> customers = new ArrayList(customerRepository.findAll());

        if (customers.isEmpty()) {
            logger.info("There are no registered customers");
            return ResponseEntity.status(204).build();
        }

        logger.info("Success in finding registered customers");
        return ResponseEntity.status(200).body(customers);
    }

    @Override
    public ResponseEntity<Customer> findById(Integer id) {
        if (customerRepository.existsById(id)) {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            logger.info(String.format("Customer of id %d found", id));

            Customer customer = optionalCustomer.get();

            return ResponseEntity.status(200).body(customer);
        }

        logger.error(String.format("Customer of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Customer> update(Integer id, Customer updatedCustomer) {
        if (
                customerRepository.existsById(id)
                        && customerRepository.findById(id).equals(updatedCustomer.getId())
        ) {

            logger.info(String.format("Customer of id %d found", id));

            customerRepository.save(updatedCustomer);
            logger.info(String.format("Updated customer: %s registration data!", updatedCustomer.toString()));

            return ResponseEntity.status(200).body(updatedCustomer);
        }

        logger.error(String.format("Client of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Boolean> delete(Integer id) {
        Boolean success = false;
        if (customerRepository.existsById(id)) {
            Customer customer = customerRepository.findById(id).get();
            logger.info(String.format("Customer of id %d found", id));

            customerRepository.deleteById(id);
            logger.info(String.format("Customer %s successfully deleted", customer.toString()));
            success = true;

            return ResponseEntity.status(200).body(success);
        }

        logger.error(String.format("Customer of id %d not found", id));
        return ResponseEntity.status(404).body(success);
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}
