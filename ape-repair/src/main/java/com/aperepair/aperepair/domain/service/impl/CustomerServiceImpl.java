package com.aperepair.aperepair.domain.service.impl;

import com.aperepair.aperepair.application.dto.request.CreateOrderRequestDto;
import com.aperepair.aperepair.application.dto.request.CredentialsRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.domain.dto.factory.AddressDtoFactory;
import com.aperepair.aperepair.domain.dto.factory.CustomerDtoFactory;
import com.aperepair.aperepair.domain.dto.factory.OrderDtoFactory;
import com.aperepair.aperepair.domain.dto.factory.ProviderDtoFactory;
import com.aperepair.aperepair.domain.enums.Genre;
import com.aperepair.aperepair.domain.enums.Role;
import com.aperepair.aperepair.domain.enums.SpecialtyTypes;
import com.aperepair.aperepair.domain.enums.Status;
import com.aperepair.aperepair.domain.exception.*;
import com.aperepair.aperepair.domain.gateway.ProfilePictureGateway;
import com.aperepair.aperepair.domain.model.Address;
import com.aperepair.aperepair.domain.model.Customer;
import com.aperepair.aperepair.domain.model.CustomerOrder;
import com.aperepair.aperepair.domain.model.Provider;
import com.aperepair.aperepair.domain.repository.AddressRepository;
import com.aperepair.aperepair.domain.repository.CustomerRepository;
import com.aperepair.aperepair.domain.repository.OrderRepository;
import com.aperepair.aperepair.domain.repository.ProviderRepository;
import com.aperepair.aperepair.domain.service.CustomerService;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;
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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Override
    public CustomerResponseDto create(CustomerRequestDto customerRequestDto) throws AlreadyRegisteredException, BadRequestException {
        String cpf = customerRequestDto.getCpf();
        String email = customerRequestDto.getEmail();
        String phone = customerRequestDto.getPhone();
        String genre = customerRequestDto.getGenre();

        if (thisCpfOrEmailOrPhoneIsAlreadyRegistered(cpf, email, phone)) {
            logger.error("Cpf or Phone or Email already registered");
            throw new AlreadyRegisteredException("Customer already registered");
        }

        if (!EnumUtils.isValidEnum(Genre.class, genre)) {
            logger.error(String.format("Gender [%s] is not valid", genre));
            throw new BadRequestException(String.format("Gender [%s] is not valid", genre));
        }

        customerRequestDto.setPassword(encoder.encode(customerRequestDto.getPassword()));
        logger.info("Customer password encrypted with success");

        customerRequestDto.setAuthenticated(false);
        customerRequestDto.setRole(Role.CUSTOMER.name());

        Customer customer = CustomerDtoFactory.toEntity(customerRequestDto);

        customerRepository.save(customer);
        logger.info("Customer saved with success");

        Address address = AddressDtoFactory.toEntity(customerRequestDto.getAddress());

        addressRepository.save(address);
        logger.info("Address registered successfully for customer");

        AddressResponseDto addressResponseDto = AddressDtoFactory.toResponseDto(address);

        Integer customerId = customer.getId();
        customerRepository.updateAddressIdById(address, customerId);
        logger.info("Foreign key [addressId] updated successfully");

        CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponseFullDto(customer, addressResponseDto);
        logger.info(String.format("Customer: [%s] registered successfully", customerResponseDto.toString()));

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
    public CustomerResponseDto update(Integer id, CustomerUpdateRequestDto updatedCustomer) throws NotFoundException, NotAuthenticatedException {
        if (customerRepository.existsById(id)) {

            Customer customer = customerRepository.findById(id).get();

            if (!isAuthenticatedCustomer(customer)) {
                CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponsePartialDto(customer);
                logger.error(String.format("Customer: [%s] is not authenticated", customerResponseDto));
                throw new NotAuthenticatedException("Customer is not authenticated");
            }

            logger.info(String.format("Customer of id %d found", customer.getId()));

            updatedCustomer.setRole(customer.getRole());
            updatedCustomer.setAuthenticated(true);
            updatedCustomer.setEmail(customer.getEmail());
            updatedCustomer.setCpf(customer.getCpf());

            Customer newCustomer = CustomerDtoFactory.updatedToEntity(updatedCustomer);

            Address address = AddressDtoFactory.toEntity(updatedCustomer.getAddress());

            newCustomer.setId(id);
            newCustomer.setAddressId(customer.getAddressId());

            address.setId(customer.getAddressId().getId());

            newCustomer.setPassword(encoder.encode(updatedCustomer.getPassword()));
            logger.info("Customer password encrypted with success");

            customerRepository.save(newCustomer);
            addressRepository.save(address);

            AddressResponseDto addressResponseDto = AddressDtoFactory.toResponseDto(address);

            logger.info(String.format("Updated customer of id: %d registration data!", newCustomer.getId()));

            CustomerResponseDto updatedCustomerResponseDto = CustomerDtoFactory.toResponseFullDto(customer, addressResponseDto);

            return updatedCustomerResponseDto;
        }

        logger.error(String.format("Customer with id: [%d] not found", id));
        throw new NotFoundException(String.format("Customer with id [%d] not found!", id));
    }

    @Override
    public DeleteResponseDto delete(Integer id) throws NotFoundException {
        logger.info(String.format("Searching customer with id: [%d]", id));

        if (customerRepository.existsById(id)) {
            Customer customer = customerRepository.findById(id).get();
            logger.info(String.format("Customer id %d found", customer.getId()));

            customer.setRole(Role.DELETED.name());
            customerRepository.save(customer);

            logger.info(String.format("Customer id: %d delete with success", customer.getId()));

            DeleteResponseDto response = new DeleteResponseDto(true);

            return response;
        }

        logger.error(String.format("Customer with id: [%d] not found", id));
        throw new NotFoundException(String.format("Customer with id [%d] not found!", id));
    }

    @Override
    public LoginResponseDto login(CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException {
        LoginResponseDto loginResponseDto = new LoginResponseDto(null, false, Role.CUSTOMER);

        String emailAttempt = credentialsRequestDto.getEmail();
        String passwordAttempt = credentialsRequestDto.getPassword();

        logger.info(String.format("Searching for customer with email: [%s]", emailAttempt));
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isEmpty()) {
            logger.error(String.format("Customer with email [%s] not found!", emailAttempt));
            throw new NotFoundException(String.format("Customer with email [%s] not found!", emailAttempt));
        }

        Customer customer = optionalCustomer.get();

        if (!EnumUtils.isValidEnum(Role.class, customer.getRole())) {
            logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
        }

        logger.info(String.format("Trying to login with email: [%s] - as a customer", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, customer);

        if (valid) {
            loginResponseDto.setSuccess(true);
            loginResponseDto.setId(customer.getId());
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
    public LogoutResponseDto logout(CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, NotAuthenticatedException, BadCredentialsException {
        LogoutResponseDto logoutResponse = new LogoutResponseDto(false);

        String emailAttempt = credentialsRequestDto.getEmail();
        String passwordAttempt = credentialsRequestDto.getPassword();

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
                throw new NotAuthenticatedException("Customer is not authenticated");
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

        logger.info(String.format("Starting creation/upload profile picture for user email - [%s]",
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

    @Override
    public void createOrder(CreateOrderRequestDto request) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, InvalidServiceTypeException {
        Integer customerId = request.getCustomerId();

        if (customerRepository.existsById(customerId)) {
            Customer customer = customerRepository.findCustomerById(customerId);

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole())) {
                logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
                throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            }

            if (!isAuthenticatedCustomer(customer)) {
                logger.info("The customer is not authenticated");
                throw new NotAuthenticatedException("Customer is not authenticated");
            }

            if (!EnumUtils.isValidEnum(SpecialtyTypes.class, request.getServiceType())) {
                logger.info("Service type of order is not valid!");
                throw new InvalidServiceTypeException("Service type of order is not valid");
            }

            logger.info(String.format("Creating order for customer: [%s]", customer.getEmail()));
            CustomerOrder customerOrder = OrderDtoFactory.toEntity(request, customer, Status.WAITING_FOR_PROPOSAL.name());

            orderRepository.save(customerOrder);
            logger.info("Order saved with success");
        } else {
            logger.error(String.format("Customer with id: [%d] not found", customerId));
            throw new NotFoundException(String.format("Customer with id [%d] not found!", customerId));
        }
    }

    public List<OrderResponseDto> getAllOrders(Integer id) throws NotFoundException, InvalidRoleException, NotAuthenticatedException, NoContentException {
        if (customerRepository.existsById(id)) {
            Customer customer = customerRepository.findCustomerById(id);

            if (!isAuthenticatedCustomer(customer)) {
                logger.info("The customer is not authenticated");
                throw new NotAuthenticatedException("Customer is not authenticated");
            }

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole())) {
                logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
                throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            }

            CustomerResponseDto customerResponse = CustomerDtoFactory.toResponsePartialDto(customer);


            List<CustomerOrder> customerOrders = orderRepository.getAllOrdersFromCustomerId(id);

            if (customerOrders.isEmpty()) {
                logger.info(String.format("Customer id [%s] does not have registered orders", id));
                throw new NoContentException(String.format("Customer id [%s] does not have registered orders", id));
            }

            logger.info("Found customer orders!");

            List<OrderResponseDto> orders = new ArrayList();

            for (CustomerOrder customerOrder : customerOrders) {
                if (customerOrder.getProviderId() == null) {
                    OrderResponseDto order = OrderDtoFactory.toResponseWithNullProviderDto(
                            customerOrder, customerResponse
                    );
                    orders.add(order);
                } else {
                    ProviderResponseDto providerResponseDto = getProviderWithIdRegisteredInCustomerOrder(customerOrder);

                    OrderResponseDto orderWithProviderId = OrderDtoFactory.toResponseWithNotNullProviderDto(
                        customerOrder, customerResponse, providerResponseDto
                    );
                    orders.add(orderWithProviderId);
                }
            }

            return orders;
        }

        logger.error(String.format("Customer with id: [%d] not found", id));
        throw new NotFoundException(String.format("Customer with id [%d] not found!", id));
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

    private ProviderResponseDto getProviderWithIdRegisteredInCustomerOrder(CustomerOrder customerOrder) {
        Integer id = customerOrder.getProviderId().getId();

        Provider provider = providerRepository.findById(id).get();

        ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);

        return providerResponseDto;
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}