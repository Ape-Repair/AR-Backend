package com.aperepair.aperepair.authorization.domain.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.model.dto.CustomerDto;
import com.aperepair.aperepair.authorization.domain.model.dto.factory.CustomerDtoFactory;
import com.aperepair.aperepair.authorization.domain.model.dto.request.LoginDto;
import com.aperepair.aperepair.authorization.domain.model.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.ProfilePictureCreationResponseDto;
import com.aperepair.aperepair.authorization.domain.model.enums.Role;
import com.aperepair.aperepair.authorization.domain.repository.CustomerRepository;
import com.aperepair.aperepair.authorization.domain.service.CustomerService;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    //TODO(1): refatorar service para retornar dto e não ResponseEntity
    //TODO(2): criar exceptionHandler para personalizar retornos e exceções

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public ResponseEntity<CustomerDto> create(Customer customer) {
        if (customerRepository.existsByCpf(customer.getCpf()) &&
                customerRepository.existsByEmail(customer.getEmail())
        ) {
            return ResponseEntity.status(400).build();
        }

        customer.setPassword(encoder.encode(customer.getPassword()));
        logger.info("Customer password encrypted with successfully");

        customer.setAuthenticated(false);

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
        if (customerRepository.existsById(id)) {

            Customer customer = customerRepository.findById(id).get();

            if (!isAuthenticatedCustomer(customer)) {
                logger.error("Customer is not authenticated!");
                return ResponseEntity.status(403).build();
            }

            logger.info(String.format("Customer of id %d found", customer.getId()));

            customer.setName(updatedCustomer.getName());
            customer.setEmail(updatedCustomer.getEmail());
            customer.setGenre(updatedCustomer.getGenre());
            customer.setPassword(encoder.encode(updatedCustomer.getPassword()));

            customerRepository.save(customer);
            logger.info(String.format("Updated customer of id: %d registration data!", customer.getId()));

            CustomerDto updatedCustomerDto = CustomerDtoFactory.toDto(customer);

            return ResponseEntity.status(200).body(updatedCustomerDto);
        }

        logger.error(String.format("Customer of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Boolean> delete(Integer id) {
        Boolean success = false;
        if (customerRepository.existsById(id)) {
            Customer customer = customerRepository.findById(id).get();
            logger.info(String.format("Customer id %d found", id));

            customer.setRole(Role.DELETED);
            customerRepository.save(customer);

            logger.info(String.format("Customer id: %d successfully deleted", customer.getId()));
            success = true;

            return ResponseEntity.status(200).body(success);
        }

        logger.error(String.format("Customer id %d not found", id));
        return ResponseEntity.status(404).body(success);
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginDto loginDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto(false, Role.CUSTOMER);

        String emailAttempt = loginDto.getEmail();
        String passwordAttempt = loginDto.getPassword();

        logger.info(String.format("Searching for customer by email: [%s]", emailAttempt));
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isEmpty()) {
            logger.warn(String.format("Email customer: [%s] - Not Found!", emailAttempt));
            return ResponseEntity.status(404).body(loginResponseDto);
        }

        Customer customer = optionalCustomer.get();

        if (customer.getRole() != Role.CUSTOMER) {
            logger.fatal(String.format("[%S] - Incorrect role for this flow", customer.getRole()));
            return ResponseEntity.status(403).build();
        }

        logger.info(String.format("Trying to login with email: [%s] - as a customer", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, customer);

        if (valid) {
            loginResponseDto.setSuccess(true);
        } else {
            logger.info("Password invalid!");

            return ResponseEntity.status(401).body(loginResponseDto);
        }

        customer.setAuthenticated(true);
        customerRepository.save(customer);
        logger.info("Customer authenticated successfully!");

        logger.info("Login successfully executed!");
        return ResponseEntity.status(200).body(loginResponseDto);
    }

    @Override
    public ResponseEntity<LogoutResponseDto> logout(LoginDto loginDto) {
        LogoutResponseDto logoutResponse = new LogoutResponseDto(false);

        String emailAttempt = loginDto.getEmail();
        String passwordAttempt = loginDto.getPassword();

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isEmpty()) {
            logger.warn(String.format("Email customer: [%s] - Not Found!", emailAttempt));
            return ResponseEntity.status(404).body(logoutResponse);
        }

        Customer customer = optionalCustomer.get();

        if (customer.getRole() != Role.CUSTOMER) {
            logger.fatal(String.format("[%S] - Incorrect role for this flow", customer.getRole()));
            return ResponseEntity.status(403).build();
        }

        logger.info(String.format("Initiating logout from email customer [%s]", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, customer);

        if (valid && isAuthenticatedCustomer(customer)) {
            logoutResponse.setSuccess(true);
            customer.setAuthenticated(false);

            customerRepository.save(customer);

            logger.info("Logout successfully executed!");
            return ResponseEntity.status(200).body(logoutResponse);
        } else {
            if (!valid) logger.info("Password invalid!");

            if (!isAuthenticatedCustomer(customer)) {
                logger.info("The customer is not authenticated");
                return ResponseEntity.status(401).body(logoutResponse);
            }

            return ResponseEntity.status(401).body(logoutResponse);
        }
    }

    @Override
    public ProfilePictureCreationResponseDto profilePictureCreation(
            ProfilePictureCreationRequestDto request
    ) throws IOException {
        logger.info(String.format("Starting creation profile picture for user email - [%s]",
                request.getEmail()));

        ProfilePictureCreationResponseDto response = new ProfilePictureCreationResponseDto(false);

        InputStream imageInputStream = null;

        try {
            byte[] imageByteArray = Base64.decodeBase64(request.getImage().getBytes());

            imageInputStream = new ByteArrayInputStream(imageByteArray);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");

            amazonS3.putObject(new PutObjectRequest("ar-profile-pictures",
                            (request.getEmail() + "/image.png"),
                            imageInputStream, metadata
                    )
            );

            response.setSuccess(true);
            logger.info(String.format("Profile picture created successfully for user email - [%s]",
                    request.getEmail()));

        } catch (Exception e) {
            logger.error(String.format("Failed to update image of email: [%s] - in external bucket",
                    request.getEmail()));
            e.printStackTrace();
            throw e;
        } finally {
            imageInputStream.close();
        }

        return response;
    }

    private Boolean isAuthenticatedCustomer(Customer customer) {
        if (customer.getAuthenticated()) return true;

        return false;
    }

    private boolean isValidPassword(String passwordAttempt, Customer customer) {
        if (encoder.matches(passwordAttempt, customer.getPassword())) return true;

        return false;
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}
