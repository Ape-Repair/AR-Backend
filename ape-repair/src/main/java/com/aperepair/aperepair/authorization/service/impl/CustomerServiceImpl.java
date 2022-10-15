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
    public ResponseEntity<CustomerDto> create(Customer customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        logger.info("Customer password encrypted with successfully");

        customerRepository.save(customer);

        CustomerDto customerDto = CustomerDtoFactory.toDto(customer);
        logger.info(String.format("CustomerDto: %s registered successfully", customerDto.toString()));


        return ResponseEntity.status(201).body(customerDto);
    }

    @Override
    public ResponseEntity<List<CustomerDto>> findAll() {
        List<Customer> customers = new ArrayList(customerRepository.findAll());

        if (customers.isEmpty()) {
            logger.info("There are no registered customers");
            return ResponseEntity.status(204).build();
        }

        List<CustomerDto> customersDto = new ArrayList();

        for (Customer customer : customers) {
            CustomerDto customerDto = CustomerDtoFactory.toDto(customer);
            customersDto.add(customerDto);
        }

        logger.info("Success in finding registered customers");
        return ResponseEntity.status(200).body(customersDto);
    }

    @Override
    public ResponseEntity<CustomerDto> findById(Integer id) {
        if (customerRepository.existsById(id)) {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            logger.info(String.format("Customer of id %d found", id));

            Customer customer = optionalCustomer.get();

            CustomerDto customerDto = CustomerDtoFactory.toDto(customer);

            return ResponseEntity.status(200).body(customerDto);
        }

        logger.error(String.format("Customer of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<CustomerDto> update(Integer id, Customer updatedCustomer) {
        if (
                customerRepository.existsById(id)
                        && customerRepository.findById(id).equals(updatedCustomer.getId())
        ) {

            logger.info(String.format("Customer of id %d found", id));

            updatedCustomer.setPassword(encoder.encode(updatedCustomer.getPassword()));

            customerRepository.save(updatedCustomer);
            logger.info(String.format("Updated customer of id: %d registration data!", updatedCustomer.getId()));

            CustomerDto updatedCustomerDto = CustomerDtoFactory.toDto(updatedCustomer);

            return ResponseEntity.status(200).body(updatedCustomerDto);
        }

        logger.error(String.format("Client of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Boolean> delete(Integer id) {
        Boolean success = false;
        if (customerRepository.existsById(id)) {
            Customer customer = customerRepository.findById(id).get();
            logger.info(String.format("Customer id %d found", id));

            customerRepository.deleteById(id);
            logger.info(String.format("Customer id: %d successfully deleted", customer.getId()));
            success = true;

            return ResponseEntity.status(200).body(success);
        }

        logger.error(String.format("Customer id %d not found", id));
        return ResponseEntity.status(404).body(success);
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}
