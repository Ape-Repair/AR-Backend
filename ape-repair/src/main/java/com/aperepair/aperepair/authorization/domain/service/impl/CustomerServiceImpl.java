package com.aperepair.aperepair.authorization.domain.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.*;
import com.aperepair.aperepair.authorization.domain.dto.factory.AddressDtoFactory;
import com.aperepair.aperepair.authorization.domain.model.Address;
import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.dto.factory.CustomerDtoFactory;
import com.aperepair.aperepair.authorization.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.enums.Role;
import com.aperepair.aperepair.authorization.domain.repository.AddressRepository;
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

    //TODO(1 ferias): refatorar service para retornar dto e não ResponseEntity
    //TODO( ferias): criar exceptionHandler para personalizar retornos e exceções

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public ResponseEntity<CustomerResponseDto> create(CustomerRequestDto customerRequestDto) {
        String cpf = customerRequestDto.getCpf();
        String email = customerRequestDto.getEmail();
        String phone = customerRequestDto.getPhone();

        if (thisCpfOrEmailOrPhoneIsAlreadyRegistered(cpf, email, phone)) {
            logger.error(String.format("Customer: %s already registered", customerRequestDto.toString()));
            return ResponseEntity.status(400).build();
        }

        customerRequestDto.setPassword(encoder.encode(customerRequestDto.getPassword()));
        logger.info("Customer password encrypted with successfully");

        customerRequestDto.setAuthenticated(false);
        customerRequestDto.setRole(Role.CUSTOMER.name());

        Customer customer = CustomerDtoFactory.toEntity(customerRequestDto);

        customerRepository.save(customer);
        logger.info("Customer saved with successfully");

        Address address = AddressDtoFactory.toEntity(customerRequestDto.getAddress());

        addressRepository.save(address);
        logger.info("Address registered successfully for customer");

        AddressResponseDto addressResponseDto = AddressDtoFactory.toResponseDto(address);

        Integer customerId = customer.getId();
        customerRepository.updateAddressIdById(address, customerId);
        logger.info("Foreign key [addressId] updated successfully");

        CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponseFullDto(customer, addressResponseDto);
        logger.info(String.format("CustomerDto: %s registered successfully", customerResponseDto.toString()));

        return ResponseEntity.status(201).body(customerResponseDto);
    }

    @Override
    public ResponseEntity<List<CustomerResponseDto>> findAll() {
        List<Customer> customers = new ArrayList(customerRepository.findAll());

        if (customers.isEmpty()) {
            logger.info("There are no registered customers");
            return ResponseEntity.status(204).build();
        }

        List<CustomerResponseDto> customersDto = new ArrayList();

        for (Customer customer : customers) {
            CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponsePartialDto(customer);
            customersDto.add(customerResponseDto);
        }

        logger.info("Success in finding registered customers");
        return ResponseEntity.status(200).body(customersDto);
    }

    @Override
    public ResponseEntity<CustomerResponseDto> findById(Integer id) {
        if (customerRepository.existsById(id)) {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            logger.info(String.format("Customer of id %d found", id));

            Customer customer = optionalCustomer.get();

            CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponsePartialDto(customer);

            return ResponseEntity.status(200).body(customerResponseDto);
        }

        logger.error(String.format("Customer of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<CustomerResponseDto> update(Integer id, CustomerRequestDto updatedCustomer) {
        if (customerRepository.existsById(id)) {

            Customer customer = customerRepository.findById(id).get();

            if (!isAuthenticatedCustomer(customer)) {
                logger.error("Customer is not authenticated!");
                return ResponseEntity.status(403).build();
            }

            logger.info(String.format("Customer of id %d found", customer.getId()));

            updatedCustomer.setRole(customer.getRole());
            updatedCustomer.setAuthenticated(true);

            Customer newCustomer = CustomerDtoFactory.toEntity(updatedCustomer);

            Address address = AddressDtoFactory.toEntity(updatedCustomer.getAddress());

            newCustomer.setId(id);
            newCustomer.setAddressId(customer.getAddressId());

            address.setId(customer.getAddressId().getId());

            customerRepository.save(newCustomer);
            addressRepository.save(address);

            logger.info(String.format("Updated customer of id: %d registration data!", newCustomer.getId()));

            CustomerResponseDto updatedCustomerResponseDto = CustomerDtoFactory.toResponsePartialDto(customer);

            return ResponseEntity.status(200).body(updatedCustomerResponseDto);
        }

        logger.error(String.format("Customer of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Boolean> delete(Integer id) {
        boolean success = false;
        if (customerRepository.existsById(id)) {
            Customer customer = customerRepository.findById(id).get();
            logger.info(String.format("Customer id %d found", id));

            customer.setRole(Role.DELETED.name());
            customerRepository.save(customer);

            logger.info(String.format("Customer id: %d successfully deleted", customer.getId()));
            success = true;

            return ResponseEntity.status(200).body(success);
        }

        logger.error(String.format("Customer id %d not found", id));
        return ResponseEntity.status(404).body(success);
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto(false, Role.CUSTOMER);

        String emailAttempt = loginRequestDto.getEmail();
        String passwordAttempt = loginRequestDto.getPassword();

        logger.info(String.format("Searching for customer by email: [%s]", emailAttempt));
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isEmpty()) {
            logger.warn(String.format("Email customer: [%s] - Not Found!", emailAttempt));
            return ResponseEntity.status(404).body(loginResponseDto);
        }

        Customer customer = optionalCustomer.get();

        if (customer.getRole() != Role.CUSTOMER.name()) {
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
    public ResponseEntity<LogoutResponseDto> logout(LoginRequestDto loginRequestDto) {
        LogoutResponseDto logoutResponse = new LogoutResponseDto(false);

        String emailAttempt = loginRequestDto.getEmail();
        String passwordAttempt = loginRequestDto.getPassword();

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isEmpty()) {
            logger.warn(String.format("Email customer: [%s] - Not Found!", emailAttempt));
            return ResponseEntity.status(404).body(logoutResponse);
        }

        Customer customer = optionalCustomer.get();

        if (customer.getRole() != Role.CUSTOMER.name()) {
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

    //TODO: Colocar esse método em uma classe separada
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
        if (customer.isAuthenticated()) return true;

        return false;
    }

    private boolean isValidPassword(String passwordAttempt, Customer customer) {
        if (encoder.matches(passwordAttempt, customer.getPassword())) return true;

        return false;
    }

    private boolean thisCpfOrEmailOrPhoneIsAlreadyRegistered(String cpf, String email, String phone) {
        if (customerRepository.existsByCpf(cpf) ||
                customerRepository.existsByEmail(email) ||
                customerRepository.existsByPhone(phone)) return true;

        return false;
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}
