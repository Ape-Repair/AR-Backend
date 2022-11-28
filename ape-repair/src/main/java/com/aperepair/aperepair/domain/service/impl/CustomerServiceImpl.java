package com.aperepair.aperepair.domain.service.impl;

import com.aperepair.aperepair.application.dto.request.CreateOrderRequestDto;
import com.aperepair.aperepair.application.dto.request.CredentialsRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.domain.dto.factory.*;
import com.aperepair.aperepair.domain.enums.Genre;
import com.aperepair.aperepair.domain.enums.Role;
import com.aperepair.aperepair.domain.enums.SpecialtyTypes;
import com.aperepair.aperepair.domain.enums.Status;
import com.aperepair.aperepair.domain.exception.*;
import com.aperepair.aperepair.domain.gateway.ProfilePictureGateway;
import com.aperepair.aperepair.domain.gateway.ReceiptOrderGateway;
import com.aperepair.aperepair.domain.model.*;
import com.aperepair.aperepair.domain.repository.*;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

    @Autowired
    private ProposalRepository proposalRepository;

    @Autowired
    private ReceiptOrderGateway receiptOrderGateway;

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
        //TODO(essencial): implementar uma pilha obj ordenada por createdAt para retornar histórico

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

    @Override
    public List<ProposalResponseDto> getProposalsForOrder(Integer orderId) throws NoContentException, NotFoundException {
        if (orderRepository.existsById(orderId)) {
            logger.info(String.format("Looking for proposals for the order of id [%d]", orderId));

            List<Proposal> proposals = proposalRepository.getAllByCustomerOrderId(orderId);

            if (proposals.isEmpty()) {
                logger.info("There are no proposals for this order");

                throw new NoContentException("There are no proposals for this order");
            }

            List<ProposalResponseDto> proposalResponseDtos = new ArrayList();

            for (Proposal proposal : proposals) {
                ProposalResponseDto proposalResponseDto = ProposalDtoFactory.toResponseDto(proposal);
                proposalResponseDtos.add(proposalResponseDto);
            }

            return proposalResponseDtos;
        }

        logger.error(String.format("Order with id: [%d] not found", orderId));
        throw new NotFoundException(String.format("Order with id [%d] not found!", orderId));
    }

    @Override
    public void acceptProposal(Integer orderId, Integer proposalId) throws NotFoundException, InvalidProposalForThisOrderException {
        if (orderRepository.existsById(orderId) && proposalRepository.existsById(proposalId)) {
            logger.info("Getting order and proposal from passed id's");

            CustomerOrder order = orderRepository.findById(orderId).get();
            Proposal proposal = proposalRepository.findById(proposalId).get();

            List<Proposal> proposals = proposalRepository.getAllByCustomerOrderId(orderId);

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
            logger.error(String.format("Order with id: [%d] and/or Proposal with id: [%d] not found", orderId, proposalId));
            throw new NotFoundException(String.format("Order with id: [%d] and/or Proposal with id: [%d] not found", orderId, proposalId));
        }
    }

    @Override
    public void makePayment(Integer orderId) throws NotFoundException, InvalidOrderForPaymentException, InvalidRoleException, NotAuthenticatedException, IOException, AwsServiceInternalException {
        logger.info(String.format("Finding order with id: [%d]", orderId));

        if (orderRepository.existsById(orderId)) {
            CustomerOrder order = orderRepository.findById(orderId).get();
            Customer customer = order.getCustomerId();

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole())) {
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

                //TODO(essencial): Montar recibo + completo
                String message = String.format("APE REPAIR - RECIBO\n\nPagamento realizado com sucesso: R$%.2f\n\nDúvidas: Entrar em contato no número: (11)9 0000-0000",
                        order.getAmount()
                );

                createReceipt(order, message);
            } else {
                logger.error("This order is invalid for payment");
                throw new InvalidOrderForPaymentException("This order is invalid for payment");
            }
        } else {
            logger.error(String.format("Order with id: [%d] not found", orderId));
            throw new NotFoundException(String.format("Order with id: [%d] not found", orderId));
        }
    }

    @Override
    public void concludeOrder(Integer orderId) throws NotFoundException, InvalidOrderToConcludeException, InvalidRoleException, NotAuthenticatedException {
        logger.info(String.format("Finding order with id: [%d]", orderId));

        if (orderRepository.existsById(orderId)) {
            CustomerOrder order = orderRepository.findById(orderId).get();
            Customer customer = order.getCustomerId();

            if (!EnumUtils.isValidEnum(Role.class, customer.getRole())) {
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
            logger.error(String.format("Order with id: [%d] not found", orderId));
            throw new NotFoundException(String.format("Order with id: [%d] not found", orderId));
        }
    }

    @Override
    public void cancelOrder(Integer orderId) throws NotFoundException, InvalidOrderToCanceledException {
        logger.info(String.format("Finding order with id: [%d]", orderId));
        if (orderRepository.existsById(orderId)) {
            CustomerOrder order = orderRepository.findById(orderId).get();

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
            logger.error(String.format("Order with id: [%d] not found", orderId));
            throw new NotFoundException(String.format("Order with id: [%d] not found", orderId));
        }
    }

    private void createReceipt(CustomerOrder order, String message) throws IOException, AwsServiceInternalException {
        String email = order.getCustomerId().getEmail();
        Integer orderId = order.getId();

        File receipt = null;

        String key = String.format("%s/order-%d.txt", email, orderId);

        try {
            receipt = File.createTempFile(String.format("%s-%d", email, orderId), ".txt");

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

            //assert buffer != null;
            //buffer.close();
        }
    }

    private Boolean isAuthenticatedCustomer(Customer customer) {
        return customer.isAuthenticated();
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

        ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);

        return providerResponseDto;
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