package com.aperepair.aperepair.domain.service.impl;

import com.aperepair.aperepair.application.dto.request.*;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.application.exception.*;
import com.aperepair.aperepair.domain.dto.factory.*;
import com.aperepair.aperepair.domain.model.enums.Genre;
import com.aperepair.aperepair.domain.model.enums.Role;
import com.aperepair.aperepair.domain.model.enums.SpecialtyTypes;
import com.aperepair.aperepair.domain.model.enums.Status;
import com.aperepair.aperepair.resources.aws.gateway.ProfilePictureGateway;
import com.aperepair.aperepair.resources.aws.gateway.ReceiptOrderGateway;
import com.aperepair.aperepair.domain.model.*;
import com.aperepair.aperepair.domain.repository.*;
import com.aperepair.aperepair.domain.service.CustomerService;
import com.aperepair.aperepair.report.resources.PileObj;
import com.aperepair.aperepair.report.resources.QueueObj;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.apache.commons.lang3.EnumUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private ReceiptOrderGateway receiptOrderGateway;

    @Autowired
    private DashboardServiceImpl dashboardServiceImpl;

    @Value("${percentage.fee}")
    private Double fee;

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

        customerRequestDto.setRole(Role.CUSTOMER.name());
        if (!EnumUtils.isValidEnum(Genre.class, genre) && !customerRequestDto.getRole().equals(Role.CUSTOMER.name())) {
            logger.error(String.format("Gender [%s] is not valid", genre));
            throw new BadRequestException(String.format("Gender [%s] is not valid", genre));
        }

        customerRequestDto.setPassword(encoder.encode(customerRequestDto.getPassword()));
        logger.info(String.format("Customer password encrypted with success for email: [%s]", email));

        customerRequestDto.setAuthenticated(false);

        Customer customer = CustomerDtoFactory.toEntity(customerRequestDto);

        customerRepository.save(customer);
        logger.info("Customer saved with success");

        Address address = AddressDtoFactory.toEntity(customerRequestDto.getAddress());

        addressRepository.save(address);
        logger.info("Address registered successfully for customer");

        AddressResponseDto addressResponseDto = AddressDtoFactory.toResponseDto(address);

        Integer customerId = customer.getId();
        customerRepository.updateAddressIdById(address, customerId);
        logger.info(String.format("Foreign key [%d] updated successfully", address.getId()));

        CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponseFullDto(customer, addressResponseDto);
        logger.info(String.format("Customer: [%s] registered successfully", customerResponseDto.toString()));

        return customerResponseDto;
    }

    @Override
    public ResponseEntity<QueueObj<CustomerResponseDto>> findAll() {
        List<Customer> customers = new ArrayList<>(customerRepository.findAll());

        if (customers.isEmpty()) {
            logger.info("There are no registered customers");
            return ResponseEntity.status(204).build();
        }

        QueueObj<CustomerResponseDto> queue = new QueueObj(customers.size());

        for (Customer customer : customers) {
            CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponsePartialDto(customer);
            queue.insert(customerResponseDto);
        }

        logger.info("Success in finding registered customers");
        return ResponseEntity.status(200).body(queue);
    }

    @Override
    public ResponseEntity<CustomerResponseDto> findById(Integer id) throws NotFoundException {
        if (customerRepository.existsById(id)) {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            logger.info(String.format("Customer of id %d found", id));

            Customer customer = optionalCustomer.get();

            Address address = customer.getAddressId();
            AddressResponseDto addressResponseDto = AddressDtoFactory.toResponseDto(address);

            CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponseFullDto(customer, addressResponseDto);

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

            return CustomerDtoFactory.toResponseFullDto(customer, addressResponseDto);
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
    public LoginResponseDto login(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException {
        LoginResponseDto loginResponseDto = new LoginResponseDto(null, false, Role.CUSTOMER.name());

        String emailAttempt = loginRequestDto.getEmail();
        String passwordAttempt = loginRequestDto.getPassword();

        logger.info(String.format("Searching for customer with email: [%s]", emailAttempt));
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isEmpty()) {
            logger.error(String.format("Customer with email [%s] not found!", emailAttempt));
            throw new NotFoundException(String.format("Customer with email [%s] not found!", emailAttempt));
        }

        Customer customer = optionalCustomer.get();

        if (!EnumUtils.isValidEnum(Role.class, customer.getRole()) && !customer.getRole().equals(Role.CUSTOMER.name())) {
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
    public LogoutResponseDto logout() {
        LogoutResponseDto logoutResponse = new LogoutResponseDto(false);

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
    public OrderUlidResponseDto createOrder(CreateOrderRequestDto request) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, InvalidServiceTypeException, StatusInvalidToCreateOrder {
        Integer customerId = request.getCustomerId();

        if (customerRepository.existsById(customerId)) {
            Customer customer = customerRepository.findCustomerById(customerId);

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole()) && customer.getRole().equals(Role.CUSTOMER.name())) {
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

            if (orderRepository.findOrdersByCustomerId(customerId).isPresent()) {
                List<OrderResponseDto> allOrders = orderRepository.findOrdersByCustomerId(customerId).get();

                if (!validatePrerequisitesForCreateAnOrder(allOrders)) {
                    logger.error(
                            String.format(
                                    "It is not allowed to create a new order while one is in " +
                                            "progress for customer id: [%d]", customerId)
                    );
                    throw new StatusInvalidToCreateOrder("It is not allowed to create a new order while one is in progress");
                }
            }

            logger.info(String.format("Creating order for customer: [%s]", customer.getEmail()));
            CustomerOrder customerOrder = OrderDtoFactory.toEntity(request, customer, Status.WAITING_FOR_PROPOSAL.name());

            orderRepository.save(customerOrder);
            logger.info("Order saved with success");

            return new OrderUlidResponseDto(customerOrder.getId());
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

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole()) && customer.getRole().equals(Role.CUSTOMER.name())) {
                logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
                throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            }

            CustomerResponseDto customerResponse = CustomerDtoFactory.toResponsePartialDto(customer);

            List<CustomerOrder> customerOrders = orderRepository.findByAscendingOrderOfCustomer(id);

            PileObj<CustomerOrder> pilha = new PileObj(customerOrders.size());

            for (CustomerOrder customerOrder : customerOrders) {
                pilha.push(customerOrder);
            }

            if (customerOrders.isEmpty()) {
                logger.info(String.format("Customer id [%s] does not have registered orders", id));
                throw new NoContentException(String.format("Customer id [%s] does not have registered orders", id));
            }

            logger.info("Found customer orders!");

            List<OrderResponseDto> orders = new ArrayList<>();

            for (int i = 0; i < customerOrders.size(); i++) {
                CustomerOrder customerOrder = pilha.pop();
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

    @Override
    public List<ProposalResponseDto> getProposalsForOrder(String orderId) throws NoContentException, NotFoundException {
        if (orderRepository.existsAndFindByOrderUlid(orderId).isPresent()) {
            logger.info(String.format("Looking for proposals for the order of id [%s]", orderId));

            List<Proposal> proposals = proposalRepository.getAllByOrderId(orderId);

            if (proposals.isEmpty()) {
                logger.info("There are no proposals for this order");

                throw new NoContentException("There are no proposals for this order");
            }

            List<ProposalResponseDto> proposalResponseDtos = new ArrayList<>();

            for (Proposal proposal : proposals) {
                String providerName = providerRepository.findById(proposal.getProviderId().getId()).get().getName();
                String providerPhone = providerRepository.findById(proposal.getProviderId().getId()).get().getPhone();

                ProposalResponseDto proposalResponseDto = ProposalDtoFactory.toResponseDto(proposal, providerName, providerPhone);
                proposalResponseDtos.add(proposalResponseDto);
            }

            return proposalResponseDtos;
        }

        logger.error(String.format("Order with id: [%s] not found", orderId));
        throw new NotFoundException(String.format("Order with id [%s] not found!", orderId));
    }

    @Override
    public void acceptProposal(String orderId, Integer proposalId) throws NotFoundException, InvalidProposalForThisOrderException {
        if (orderRepository.existsAndFindByOrderUlid(orderId).isPresent() && proposalRepository.existsById(proposalId)) {
            logger.info("Getting order and proposal from passed id's");

            CustomerOrder order = orderRepository.existsAndFindByOrderUlid(orderId).get();
            Proposal proposal = proposalRepository.findById(proposalId).get();

            List<Proposal> proposals = proposalRepository.getAllByOrderId(orderId);

            if (proposals.contains(proposal) && validatePrerequisitesForAcceptingAProposal(order, proposal)) {
                logger.info("This proposal and order are VALID");

                final String WAITING_FOR_PAYMENT = Status.WAITING_FOR_PAYMENT.name();

                proposal.setAccepted(true);

                proposalRepository.save(proposal);

                logger.info("Proposal update with success!");

                order.setProviderId(proposal.getProviderId());
                order.setStatus(WAITING_FOR_PAYMENT);
                order.setAmount(proposal.getAmount());

                orderRepository.save(order);

                logger.info("Order update with success!");

            } else {
                logger.error("This proposal and/or order are INVALID for this request");

                throw new InvalidProposalForThisOrderException("This proposal and/or order are INVALID for this request");
            }
        } else {
            logger.error(String.format("Order with id: [%s] and/or Proposal with id: [%d] not found", orderId, proposalId));
            throw new NotFoundException(String.format("Order with id: [%s] and/or Proposal with id: [%d] not found", orderId, proposalId));
        }
    }

    @Override
    public void makePayment(String orderId) throws NotFoundException, InvalidOrderForPaymentException, InvalidRoleException, NotAuthenticatedException, IOException, AwsServiceInternalException {
        logger.info(String.format("Finding order with id: [%s]", orderId));

        if (orderRepository.existsAndFindByOrderUlid(orderId).isPresent()) {
            CustomerOrder order = orderRepository.existsAndFindByOrderUlid(orderId).get();
            Customer customer = order.getCustomerId();

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole()) && customer.getRole().equals(Role.CUSTOMER.name())) {
                logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
                throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            }

            if (!isAuthenticatedCustomer(customer)) {
                logger.info("The customer is not authenticated");
                throw new NotAuthenticatedException("Customer is not authenticated");
            }

            if (validatePrerequisitesForPaymentAOrder(order)) {
                logger.info("This order is valid for payment");

                order.setPaid(true);
                order.setStatus(Status.IN_PROGRESS.name());

                orderRepository.save(order);
                logger.info("Payment made successfully");

                final Double TOTAL_PRICE = order.getAmount() * fee;

                String message = String.format("APE REPAIR - RECIBO\n\nCliente: %s\nCPF: %s\n\nPagamento realizado com sucesso: R$%.2f\n\nPrestador: %s\n\nID: %s\n\nDúvidas? Entrar em contato no número: (11)9 0000-0000",
                        customer.getName(),
                        customer.getCpf(),
                        TOTAL_PRICE,
                        order.getProviderId().getName(),
                        orderId
                );

                createReceipt(order, message);
                dashboardServiceImpl.addToRecipe(TOTAL_PRICE);
            } else {
                logger.error("This order is invalid for payment");
                throw new InvalidOrderForPaymentException("This order is invalid for payment");
            }
        } else {
            logger.error(String.format("Order with id: [%s] not found", orderId));
            throw new NotFoundException(String.format("Order with id: [%s] not found", orderId));
        }
    }

    @Override
    public InputStream getReceipt(String orderId) throws InvalidRoleException, NotAuthenticatedException, InvalidOrderStatusException, NotFoundException, AwsServiceInternalException, AwsS3ImageException {
        logger.info(String.format("Finding order with id: [%s]", orderId));

        if (orderRepository.existsAndFindByOrderUlid(orderId).isPresent()) {
            CustomerOrder order = orderRepository.existsAndFindByOrderUlid(orderId).get();
            Customer customer = order.getCustomerId();
            String email = customer.getEmail();

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole()) && !customer.getRole().equals(Role.CUSTOMER.name())) {
                logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
                throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            }

            if (!isAuthenticatedCustomer(customer)) {
                logger.info("The customer is not authenticated");
                throw new NotAuthenticatedException("Customer is not authenticated");
            }

            if (!order.isPaid()) {
                logger.error("Order status does not allow receipt download");
                throw new InvalidOrderStatusException("Order status does not allow receipt download");
            }

            String key = String.format("%s/order-%s.txt", email, orderId);

            return receiptOrderGateway.getReceiptOrder(key, email);
        }

        logger.error(String.format("Order with id: [%s] not found", orderId));
        throw new NotFoundException(String.format("Order with id: [%s] not found", orderId));
    }

    @Override
    public void concludeOrder(String orderId) throws NotFoundException, InvalidOrderToConcludeException, InvalidRoleException, NotAuthenticatedException {
        logger.info(String.format("Finding order with id: [%s]", orderId));

        if (orderRepository.existsAndFindByOrderUlid(orderId).isPresent()) {
            CustomerOrder order = orderRepository.existsAndFindByOrderUlid(orderId).get();
            Customer customer = order.getCustomerId();

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole()) && customer.getRole().equals(Role.CUSTOMER.name())) {
                logger.fatal(String.format("[%S] - Invalid role for this flow", customer.getRole()));
                throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", customer.getRole()));
            }

            if (!isAuthenticatedCustomer(customer)) {
                logger.info("The customer is not authenticated");
                throw new NotAuthenticatedException("Customer is not authenticated");
            }

            if (validatePrerequisitesForConcludeAOrder(order)) {
                logger.info("This order is valid to be finalized");

                order.setStatus(Status.DONE.name());
                orderRepository.save(order);

                logger.info("Order conclude successfully");
            } else {
                logger.error("This order is invalid to finalized");
                throw new InvalidOrderToConcludeException("This order is invalid to finalized");
            }
        } else {
            logger.error(String.format("Order with id: [%s] not found", orderId));
            throw new NotFoundException(String.format("Order with id: [%s] not found", orderId));
        }
    }

    @Override
    public void cancelOrder(String orderId) throws NotFoundException, InvalidOrderToCanceledException {
        logger.info(String.format("Finding order with id: [%s]", orderId));
        if (orderRepository.existsAndFindByOrderUlid(orderId).isPresent()) {
            CustomerOrder order = orderRepository.existsAndFindByOrderUlid(orderId).get();

            if (!order.getStatus().equals(Status.DONE.name())) {
                //TODO(desejável): Criar entidade de "lifecycle" de uma order, para registrar o motivo do cancelamento, e quem o fez
                order.setStatus(Status.CANCELED.name());
                orderRepository.save(order);

                logger.info("Order canceled successfully!");
            } else {
                logger.error("This order is invalid to canceled");
                throw new InvalidOrderToCanceledException("This order is invalid to canceled");
            }
        } else {
            logger.error(String.format("Order with id: [%s] not found", orderId));
            throw new NotFoundException(String.format("Order with id: [%s] not found", orderId));
        }
    }

    private void createReceipt(CustomerOrder order, String message) throws AwsServiceInternalException {
        String email = order.getCustomerId().getEmail();
        String orderId = order.getId();

        File receipt = null;

        String key = String.format("%s/order-%s.txt", email, orderId);

        try {
            logger.info("Generating transaction receipt");

            receipt = File.createTempFile(String.format("%s-%s", email, orderId), ".txt");

            BufferedWriter buffer = new BufferedWriter(new FileWriter(receipt));
            buffer.write(message);
            buffer.close();

            receiptOrderGateway.uploadReceiptOrder(receipt, key, email);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        } catch (AwsServiceInternalException ex) {
            throw new AwsServiceInternalException(ex.getMessage());
        } finally {
            assert receipt != null;
            receipt.deleteOnExit();
        }
    }

    private Boolean isAuthenticatedCustomer(Customer customer) {
        return customer.isAuthenticated();
    }

    private boolean validatePrerequisitesForCreateAnOrder(List<OrderResponseDto> allOrders) {
        for (OrderResponseDto order : allOrders) {
            if (
                    Objects.equals(order.getStatus(), Status.WAITING_FOR_PROPOSAL.name())
                            || Objects.equals(order.getStatus(), Status.WAITING_FOR_PROPOSAL_TO_BE_ACCEPTED.name())
                            || Objects.equals(order.getStatus(), Status.WAITING_FOR_PAYMENT.name())
                            || Objects.equals(order.getStatus(), Status.IN_PROGRESS.name())
            ) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidPassword(String passwordAttempt, Customer customer) {
        return encoder.matches(passwordAttempt, customer.getPassword());
    }

    private boolean thisCpfOrEmailOrPhoneIsAlreadyRegistered(String cpf, String email, String phone) {
        return customerRepository.existsByCpf(cpf) || customerRepository.existsByEmail(email) ||
                customerRepository.existsByPhone(phone);
    }

    private ProviderResponseDto getProviderWithIdRegisteredInCustomerOrder(CustomerOrder customerOrder) {
        Integer id = customerOrder.getProviderId().getId();

        Provider provider = providerRepository.findById(id).get();

        return ProviderDtoFactory.toResponsePartialDto(provider);
    }

    private boolean validatePrerequisitesForAcceptingAProposal(CustomerOrder order, Proposal proposal) {
        return (!proposal.isAccepted() && !order.isPaid() && !order.getStatus().equals(Status.CANCELED.name()) && !order.getStatus().equals(Status.DONE.name())
                && !order.getStatus().equals(Status.IN_PROGRESS.name()));
    }

    private boolean validatePrerequisitesForPaymentAOrder(CustomerOrder order) {
        return (!order.isPaid() && !order.getStatus().equals(Status.CANCELED.name()) &&
                !order.getStatus().equals(Status.DONE.name()) && !order.getStatus().equals(Status.IN_PROGRESS.name())
                && !order.getStatus().equals(Status.WAITING_FOR_PROPOSAL.name()) &&
                !order.getStatus().equals(Status.WAITING_FOR_PROPOSAL_TO_BE_ACCEPTED.name()) && order.getProviderId() != null
                && order.getAmount() > 0.0
        );
    }

    private boolean validatePrerequisitesForConcludeAOrder(CustomerOrder order) {
        return (order.isPaid() && !order.getStatus().equals(Status.CANCELED.name()) &&
                !order.getStatus().equals(Status.DONE.name()) && order.getStatus().equals(Status.IN_PROGRESS.name())
                && !order.getStatus().equals(Status.WAITING_FOR_PROPOSAL.name()) &&
                !order.getStatus().equals(Status.WAITING_FOR_PROPOSAL_TO_BE_ACCEPTED.name()) && order.getProviderId() != null
                && order.getAmount() > 0.0
        );
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}