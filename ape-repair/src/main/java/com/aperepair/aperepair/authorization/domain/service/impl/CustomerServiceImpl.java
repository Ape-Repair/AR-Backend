package com.aperepair.aperepair.authorization.domain.service.impl;

import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.DeleteRequestDto;
import com.aperepair.aperepair.authorization.domain.exception.*;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.*;
import com.aperepair.aperepair.authorization.domain.dto.factory.AddressDtoFactory;
import com.aperepair.aperepair.authorization.domain.dto.factory.CustomerDtoFactory;
import com.aperepair.aperepair.authorization.domain.enums.Role;
import com.aperepair.aperepair.authorization.domain.model.Address;
import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.repository.AddressRepository;
import com.aperepair.aperepair.authorization.domain.repository.CustomerRepository;
import com.aperepair.aperepair.authorization.domain.service.CustomerService;
import com.aperepair.aperepair.authorization.domain.gateway.ProfilePictureGateway;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ProfilePictureGateway profilePictureGateway;

    @Override
    public CustomerResponseDto create(CustomerRequestDto customerRequestDto) throws AlreadyRegisteredException {
        String cpf = customerRequestDto.getCpf();
        String email = customerRequestDto.getEmail();
        String phone = customerRequestDto.getPhone();

        if (thisCpfOrEmailOrPhoneIsAlreadyRegistered(cpf, email, phone)) {
            logger.error(String.format("Customer: %s already registered", customerRequestDto.toString()));
            throw new AlreadyRegisteredException(String.format("Customer: %s already registered", customerRequestDto.toString()));
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

        return customerResponseDto;
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
    public ResponseEntity<CustomerResponseDto> findById(Integer id) throws NotFoundException {
        if (customerRepository.existsById(id)) {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            logger.info(String.format("Customer of id %d found", id));

            Customer customer = optionalCustomer.get();

            CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponsePartialDto(customer);

            return ResponseEntity.status(200).body(customerResponseDto);
        }

        logger.error(String.format("Customer with id: [%d] not found", id));
        throw new NotFoundException(String.format("Customer with id [%d] not found!", id));
    }

    @Override
    public CustomerResponseDto update(Integer id, CustomerRequestDto updatedCustomer) throws NotFoundException, NotAuthenticatedException {
        if (customerRepository.existsById(id)) {

            Customer customer = customerRepository.findById(id).get();

            if (!isAuthenticatedCustomer(customer)) {
                CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponsePartialDto(customer);
                logger.error(String.format("Customer: [%s] is not authenticated", customerResponseDto));
                throw new NotAuthenticatedException(String.format("Customer: [%s] is not authenticated", customerResponseDto));
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

            return updatedCustomerResponseDto;
        }

        logger.error(String.format("Customer with id: [%d] not found", id));
        throw new NotFoundException(String.format("Customer with id [%d] not found!", id));
    }

    @Override
    public DeleteResponseDto delete(DeleteRequestDto request) throws NotFoundException {
        if (customerRepository.existsById(request.getId())) {
            Customer customer = customerRepository.findById(request.getId()).get();
            logger.info(String.format("Customer id %d found", request.getId()));

            customer.setRole(Role.DELETED.name());
            customerRepository.save(customer);

            logger.info(String.format("Customer id: %d successfully deleted", customer.getId()));

            DeleteResponseDto response = new DeleteResponseDto(true);

            return response;
        }

        logger.error(String.format("Customer with id: [%d] not found", request.getId()));
        throw new NotFoundException(String.format("Customer with id [%d] not found!", request.getId()));
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException {
        LoginResponseDto loginResponseDto = new LoginResponseDto(false, Role.CUSTOMER);

        String emailAttempt = loginRequestDto.getEmail();
        String passwordAttempt = loginRequestDto.getPassword();

        logger.info(String.format("Searching for customer by email: [%s]", emailAttempt));
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isEmpty()) {
            logger.error(String.format("Customer with email [%s] not found!", emailAttempt));
            throw new NotFoundException(String.format("Customer with email [%s] not found!", emailAttempt));
        }

        Customer customer = optionalCustomer.get();

        if (customer.getRole() != Role.CUSTOMER.name()) {
            logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
        }

        logger.info(String.format("Trying to login with email: [%s] - as a customer", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, customer);

        if (valid) {
            loginResponseDto.setSuccess(true);
        } else {
            logger.info("Password invalid!");

            throw new BadCredentialsException("Bad Credentials");
        }

        customer.setAuthenticated(true);
        customerRepository.save(customer);
        logger.info("Customer authenticated with success!");

        logger.info("Login executed with success!");
        return loginResponseDto;
    }

    @Override
    public LogoutResponseDto logout(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, NotAuthenticatedException, BadCredentialsException {
        LogoutResponseDto logoutResponse = new LogoutResponseDto(false);

        String emailAttempt = loginRequestDto.getEmail();
        String passwordAttempt = loginRequestDto.getPassword();

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isEmpty()) {
            logger.error(String.format("Customer with email: [%s] - Not Found!", emailAttempt));
            throw new NotFoundException(String.format("Customer with email [%s] not found!", emailAttempt));
        }

        Customer customer = optionalCustomer.get();

        if (customer.getRole() != Role.CUSTOMER.name()) {
            logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
        }

        logger.info(String.format("Initiating logout from email customer [%s]", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, customer);

        if (valid && isAuthenticatedCustomer(customer)) {
            logoutResponse.setSuccess(true);
            customer.setAuthenticated(false);

            customerRepository.save(customer);
        } else {
            if (!valid) {
                logger.info("Password invalid!");
                throw new BadCredentialsException("Password invalid!");
            }

            if (!isAuthenticatedCustomer(customer)) {
                logger.info("The customer is not authenticated");
                throw new NotAuthenticatedException("The customer is not authenticated");
            }
        }

        logger.info("Logout successfully executed!");
        return logoutResponse;
    }

    @Override
    public ProfilePictureCreationResponseDto profilePictureCreation(
            ProfilePictureCreationRequestDto request
    ) throws IOException, AwsUploadException, NotFoundException {
        String email = request.getEmail();

        logger.info(String.format("Starting creation profile picture for user email - [%s]",
                email));

        ProfilePictureCreationResponseDto response = new ProfilePictureCreationResponseDto(false);

        if (customerRepository.existsByEmail(email)) {
            boolean profilePictureCreatedWithSuccess = profilePictureGateway.profilePictureCreation(request);

            response.setSuccess(profilePictureCreatedWithSuccess);

            return response;
        }

        logger.error(String.format("Customer with email [%s] not found!", email));
        throw new NotFoundException(String.format("Customer with email [%s] not found!", email));
    }

    @Override
    public GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws Exception {
        String email = request.getEmail();
        if (customerRepository.existsByEmail(email)) {
            logger.info(String.format("Searching profile picture for customer: [%s]", email));

            GetProfilePictureResponseDto response = new GetProfilePictureResponseDto(null);
            String imageBase64 = profilePictureGateway.getProfilePicture(request);

            response.setImageBase64(imageBase64);

            return response;
        }

        logger.error(String.format("Customer with email [%s] not found!", email));
        throw new NotFoundException(String.format("Customer with email [%s] not found!", email));
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
        if (customerRepository.existsByCpf(cpf) || customerRepository.existsByEmail(email) ||
                customerRepository.existsByPhone(phone)) return true;

        return false;
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}
