package com.aperepair.aperepair.domain.service.impl;

import com.aperepair.aperepair.application.dto.request.CreateProposalRequestDto;
import com.aperepair.aperepair.application.dto.request.CredentialsRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.domain.dto.factory.*;
import com.aperepair.aperepair.domain.enums.Genre;
import com.aperepair.aperepair.domain.enums.Role;
import com.aperepair.aperepair.domain.enums.SpecialtyTypes;
import com.aperepair.aperepair.domain.enums.Status;
import com.aperepair.aperepair.domain.exception.*;
import com.aperepair.aperepair.domain.gateway.ProfilePictureGateway;
import com.aperepair.aperepair.domain.model.*;
import com.aperepair.aperepair.domain.repository.AddressRepository;
import com.aperepair.aperepair.domain.repository.OrderRepository;
import com.aperepair.aperepair.domain.repository.ProposalRepository;
import com.aperepair.aperepair.domain.repository.ProviderRepository;
import com.aperepair.aperepair.domain.service.ProviderService;
import com.aperepair.aperepair.report.resources.PileObj;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ProfilePictureGateway profilePictureGateway;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProposalRepository proposalRepository;

    @Override
    public ProviderResponseDto create(ProviderRequestDto request) throws BadRequestException, AlreadyRegisteredException {
        String cpf = request.getCpf();
        String phone = request.getPhone();
        String genre = request.getGenre();
        String specialtyType = request.getSpecialtyType();
        String email = request.getEmail();

        if (thisCpfOrPhoneOrEmailIsAlreadyRegistered(cpf, phone, email)) {
            logger.error("CPF or Phone or Email is already registered");

            throw new AlreadyRegisteredException("Provider already registered");
        }

        if (!EnumUtils.isValidEnum(Genre.class, genre)) {
            logger.error(String.format("Gender [%s] is not valid", genre));
            throw new BadRequestException(String.format("Gender [%s] is not valid", genre));
        }

        if (!EnumUtils.isValidEnum(SpecialtyTypes.class, specialtyType)) {
            logger.error(String.format("Specialty [%s] is not valid", specialtyType));
            throw new BadRequestException(String.format("Specialty [%s] is not valid", specialtyType));
        }

        request.setPassword(encoder.encode(request.getPassword()));
        logger.info("Provider password encrypted with successfully");

        request.setAuthenticated(false);
        request.setRole(Role.PROVIDER.name());

        Provider provider = ProviderDtoFactory.toEntity(request);

        providerRepository.save(provider);
        logger.info("Provider saved with successfully");

        Address address = AddressDtoFactory.toEntity(request.getAddress());

        addressRepository.save(address);
        logger.info("Address registered successfully for provider");

        AddressResponseDto addressResponseDto = AddressDtoFactory.toResponseDto(address);


        Integer providerId = provider.getId();
        providerRepository.updateAddressIdById(address, providerId);
        logger.info("Foreign key [addressId] updated successfully");

        ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponseFullDto(provider, addressResponseDto);
        logger.info(String.format("Provider: %s registered successfully", providerResponseDto.toString()));

        return providerResponseDto;
    }

    @Override
    public ResponseEntity<List<ProviderResponseDto>> findAll() {
        List<Provider> providers = new ArrayList(providerRepository.findAll());

        if (providers.isEmpty()) {
            logger.info("There are no registered providers");
            return ResponseEntity.status(204).build();
        }

        List<ProviderResponseDto> providerResponseDtos = new ArrayList();

        for (Provider provider : providers) {
            ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);
            providerResponseDtos.add(providerResponseDto);
        }

        logger.info("Success in finding registered providers");
        return ResponseEntity.status(200).body(providerResponseDtos);
    }

    @Override
    public ResponseEntity<ProviderResponseDto> findById(Integer id) {
        if (providerRepository.existsById(id)) {
            Optional<Provider> optionalProvider = providerRepository.findById(id);
            logger.info(String.format("Provider with id [%d] found", id));

            Provider provider = optionalProvider.get();

            ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);

            return ResponseEntity.status(200).body(providerResponseDto);
        }

        logger.error(String.format("Provider with id: [%d] not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ProviderResponseDto update(Integer id, ProviderUpdateRequestDto updatedProvider) throws NotAuthenticatedException, NotFoundException, BadRequestException {
        if (providerRepository.existsById(id)) {

            String genre = updatedProvider.getGenre();
            String specialtyType = updatedProvider.getSpecialtyType();

            Provider provider = providerRepository.findById(id).get();

            if (!isAuthenticatedProvider(provider)) {
                ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);
                logger.error(String.format("Provider: [%s] is not authenticated", providerResponseDto));
                throw new NotAuthenticatedException("Provider is not authenticated");
            }

            if (!EnumUtils.isValidEnum(Genre.class, genre)) {
                logger.error(String.format("Gender [%s] is not valid", genre));
                throw new BadRequestException(String.format("Gender [%s] is not valid", genre));
            }

            if (!EnumUtils.isValidEnum(SpecialtyTypes.class, specialtyType)) {
                logger.error(String.format("Specialty [%s] is not valid", specialtyType));
                throw new BadRequestException(String.format("Specialty [%s] is not valid", specialtyType));
            }

            logger.info(String.format("Provider of id %d found", provider.getId()));

            updatedProvider.setRole(provider.getRole());
            updatedProvider.setAuthenticated(true);
            updatedProvider.setEmail(provider.getEmail());
            updatedProvider.setCpf(provider.getCpf());

            Provider newProvider = ProviderDtoFactory.updateToEntity(updatedProvider);

            Address address = AddressDtoFactory.toEntity(updatedProvider.getAddress());

            newProvider.setId(id);
            newProvider.setAddressId(provider.getAddressId());

            address.setId(provider.getAddressId().getId());

            newProvider.setPassword(encoder.encode(updatedProvider.getPassword()));
            logger.info("Provider password encrypted with successfully");

            providerRepository.save(newProvider);
            addressRepository.save(address);

            AddressResponseDto addressResponseDto = AddressDtoFactory.toResponseDto(address);

            logger.info(String.format("Updated provider of id: %d registration data!", newProvider.getId()));

            ProviderResponseDto updatedProviderResponseDto = ProviderDtoFactory.toResponseFullDto(provider, addressResponseDto);

            return updatedProviderResponseDto;
        }

        logger.error(String.format("Provider with id [%d] not found!", id));
        throw new NotFoundException(String.format("Provider with id [%d] not found!", id));
    }

    @Override
    public DeleteResponseDto delete(Integer id) throws NotFoundException {
        logger.info(String.format("Searching provider with id: [%d]", id));

        if (providerRepository.existsById(id)) {
            Provider provider = providerRepository.findById(id).get();
            logger.info(String.format("Provider with id: [%d] found", provider.getId()));

            provider.setRole(Role.DELETED.name());
            providerRepository.save(provider);

            logger.info(String.format("Provider id: [%d] successfully deleted", provider.getId()));

            DeleteResponseDto response = new DeleteResponseDto(true);

            return response;
        }

        logger.error(String.format("Provider with id: [%d] not found", id));
        throw new NotFoundException(String.format("Provider with id [%d] not found!", id));
    }

    @Override
    public LoginResponseDto login(CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException {
        LoginResponseDto loginResponseDto = new LoginResponseDto(null, false, Role.PROVIDER);

        String emailAttempt = credentialsRequestDto.getEmail();
        String passwordAttempt = credentialsRequestDto.getPassword();

        logger.info(String.format("Searching for provider with email: [%s]", emailAttempt));
        Optional<Provider> optionalProvider = providerRepository.findByEmail(emailAttempt);

        if (optionalProvider.isEmpty()) {
            logger.error(String.format("Provider with email [%s] not found!", emailAttempt));
            throw new NotFoundException(String.format("Provider with email [%s] not found!", emailAttempt));
        }

        Provider provider = optionalProvider.get();

        if (provider.getRole() != Role.PROVIDER.name()) {
            logger.fatal(String.format("[%S] - Invalid role for this flow", provider.getRole()));
            throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", provider.getRole()));
        }

        logger.info(String.format("Trying to login with email: [%s] - as a provider", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, provider);

        if (valid) {
            loginResponseDto.setSuccess(true);
            loginResponseDto.setId(provider.getId());
        } else {
            logger.info("Password invalid!");

            throw new BadCredentialsException("Bad Credentials");
        }

        provider.setAuthenticated(true);
        providerRepository.save(provider);
        logger.info("Provider authenticated with success!");

        logger.info("Login executed with success!");
        return loginResponseDto;
    }

    public LogoutResponseDto logout(CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException, NotAuthenticatedException {
        LogoutResponseDto logoutResponse = new LogoutResponseDto(false);

        String emailAttempt = credentialsRequestDto.getEmail();
        String passwordAttempt = credentialsRequestDto.getPassword();

        Optional<Provider> optionalProvider = providerRepository.findByEmail(emailAttempt);

        if (optionalProvider.isEmpty()) {
            logger.error(String.format("Provider with email: [%s] - Not Found!", emailAttempt));
            throw new NotFoundException(String.format("Provider with email [%s] not found!", emailAttempt));
        }

        Provider provider = optionalProvider.get();

        if (provider.getRole() != Role.PROVIDER.name()) {
            logger.fatal(String.format("[%S] - Invalid role for this flow", provider.getRole()));
            throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", provider.getRole()));
        }

        logger.info(String.format("Initiating logout from email provider [%s]", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, provider);

        if (valid && isAuthenticatedProvider(provider)) {
            logoutResponse.setSuccess(true);
            provider.setAuthenticated(false);

            providerRepository.save(provider);
        } else {
            if (!valid) {
                logger.info("Password invalid!");
                throw new BadCredentialsException("Password invalid!");
            }

            if (!isAuthenticatedProvider(provider)) {
                logger.info("The provider is not authenticated");
                throw new NotAuthenticatedException("Provider is not authenticated");
            }
        }

        logger.info("Logout successfully executed!");
        return logoutResponse;
    }

    @Override
    public ProfilePictureCreationResponseDto profilePictureCreation(
            ProfilePictureCreationRequestDto request
    ) throws AwsUploadException, IOException, NotFoundException {
        String email = request.getEmail();

        logger.info(String.format("Starting creation profile picture for user email - [%s]",
                email));

        ProfilePictureCreationResponseDto response = new ProfilePictureCreationResponseDto(false);

        if (providerRepository.existsByEmail(email)) {
            boolean profilePictureCreatedWithSuccess = profilePictureGateway.profilePictureCreation(request);

            response.setSuccess(profilePictureCreatedWithSuccess);

            return response;
        }

        logger.error(String.format("Provider with email [%s] not found!", email));
        throw new NotFoundException(String.format("Provider with email [%s] not found!", email));
    }

    @Override
    public GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws AwsServiceInternalException, IOException, AwsS3ImageException, NotFoundException {
        String email = request.getEmail();
        if (providerRepository.existsByEmail(email)) {
            logger.info(String.format("Searching profile picture for provider: [%s]", email));

            GetProfilePictureResponseDto response = new GetProfilePictureResponseDto(null);
            String imageBase64 = profilePictureGateway.getProfilePicture(request);

            response.setImageBase64(imageBase64);

            return response;
        }

        logger.error(String.format("Provider with email [%s] not found!", email));
        throw new NotFoundException(String.format("Provider with email [%s] not found!", email));
    }

    @Override
    public ProposalResponseDto createProposal(CreateProposalRequestDto request) throws NotFoundException, NotAuthenticatedException, BadRequestException, SpecialtyNotMatchWithServiceTypeException, InvalidProposalForThisOrderException, ProviderAlreadyMadeProposalForOrderException {
        Integer providerId = request.getProviderId();
        String orderId = request.getOrderId();

        if (providerRepository.existsById(providerId)) {
            Provider provider = providerRepository.findById(providerId).get();
            String providerSpecialtyType = provider.getSpecialtyType();

            if (!isAuthenticatedProvider(provider)) {
                ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);

                logger.error(String.format("Provider: [%s] is not authenticated", providerResponseDto));

                throw new NotAuthenticatedException("Provider is not authenticated");
            }

            if (!EnumUtils.isValidEnum(SpecialtyTypes.class, providerSpecialtyType)) {
                logger.error(String.format("Specialty [%s] is not valid at this flow", providerSpecialtyType));

                throw new BadRequestException(String.format("Specialty [%s] is not valid", providerSpecialtyType));
            }

            if (proposalRepository.providerHasAlreadyMadeProposalForThisOrder(providerId, orderId).isPresent()) {
                logger.error("Provider has already made a proposal for this order");
                throw new ProviderAlreadyMadeProposalForOrderException("Provider has already made a proposal for this order");
            }

            logger.info(String.format("Searching order by id: [%s]", orderId));
            Optional<CustomerOrder> orderOpt = orderRepository.existsAndFindByOrderUlid(request.getOrderId());

            if (orderOpt.isEmpty()) {
                logger.error(String.format("Order with id [%s] not found!", orderId));

                throw new NotFoundException(String.format("Order with id [%s] not found!", orderId));
            }

            CustomerOrder order = orderOpt.get();

            if (!EnumUtils.isValidEnum(SpecialtyTypes.class, order.getServiceType())) {
                logger.error("Provider's specialty does not match the specialty required in the order");

                throw new SpecialtyNotMatchWithServiceTypeException(
                        "Provider's specialty does not match the specialty required in the order"
                );
            }

            if (!validatePrerequisitesForCreatingAProposal(order)) {
                logger.error("Proposal cannot be created for this order");

                throw new InvalidProposalForThisOrderException("Proposal cannot be created for this order");
            }

            Proposal proposal = ProposalDtoFactory.toEntity(request, order);

            proposal.setOrderId(order);
            proposal.setProviderId(provider);

            proposalRepository.save(proposal);

            ProposalResponseDto proposalResponseDto = ProposalDtoFactory.toResponseDto(proposal);
            logger.info(String.format("Proposal: [%s] save with success!", proposalResponseDto));

            String updatedStatus = Status.WAITING_FOR_PROPOSAL_TO_BE_ACCEPTED.name();

            orderRepository.updateOrderIdByStatus(orderId, updatedStatus);
            logger.info(String.format("Order id: [%s] - updated status for: [%s] with success!", orderId, updatedStatus));

            return proposalResponseDto;
        }

        logger.error(String.format("Order with id [%s] not found!", orderId));

        throw new NotFoundException(String.format("Order with id [%s] not found!", orderId));
    }

    @Override
    public List<OrderResponseDto> getAllAvailableOrders(Integer providerId) throws NotAuthenticatedException, BadRequestException, NotFoundException, NoContentException {
        boolean providerExists = providerRepository.existsById(providerId);
        if (providerExists) {
            Provider provider = providerRepository.findById(providerId).get();
            String providerSpecialtyType = provider.getSpecialtyType();

            if (!isAuthenticatedProvider(provider)) {
                ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);

                logger.error(String.format("Provider: [%s] is not authenticated", providerResponseDto));

                throw new NotAuthenticatedException("Provider is not authenticated");
            }

            logger.info(String.format("Finding by available orders of specialty type: [%s]", providerSpecialtyType));

            List<CustomerOrder> orders = orderRepository
                    .findByServiceTypeAvailableOrders(providerSpecialtyType);

            if (orders.isEmpty()) {
                throw new NoContentException(String.format("There are no orders available for this specialty [%s]", providerSpecialtyType));
            }

            List<OrderResponseDto> availableOrders = new ArrayList();

            for (CustomerOrder order : orders) {
                Customer customer = order.getCustomerId();

                CustomerResponseDto customerResponseDto = CustomerDtoFactory.toResponsePartialDto(customer);

                OrderResponseDto orderResponseDto = OrderDtoFactory.toResponseDto(
                        order, customerResponseDto, null
                );

                availableOrders.add(orderResponseDto);
            }

            return availableOrders;
        }

        logger.error(String.format("Provider with id [%d] not found!", providerId));
        throw new NotFoundException(String.format("Provider with id [%d] not found!", providerId));
    }

    @Override
    public List<OrderResponseDto> getAllOrders(Integer id) throws NotAuthenticatedException, InvalidRoleException, NoContentException, NotFoundException {
        if (providerRepository.existsById(id)) {
            Provider provider = providerRepository.findProviderById(id);

            if (!isAuthenticatedProvider(provider)) {
                logger.info("The provider is not authenticated");
                throw new NotAuthenticatedException("Provider is not authenticated");
            }

            if (!EnumUtils.isValidEnum(Role.class, provider.getRole())) {
                logger.fatal(String.format("[%S] - Invalid role for this flow", provider.getRole()));
                throw new InvalidRoleException(String.format("[%S] - Invalid role for this flow", provider.getRole()));
            }

            ProviderResponseDto providerResponse = ProviderDtoFactory.toResponsePartialDto(provider);

            List<CustomerOrder> customerOrders = orderRepository.findByAscendingOrderOfProvider(id);

            PileObj<CustomerOrder> pilha = new PileObj(customerOrders.size());

            for (CustomerOrder customerOrder : customerOrders) {
                pilha.push(customerOrder);
            }

            if (customerOrders.isEmpty()) {
                logger.info(String.format("Provider id [%s] does not have registered orders", id));
                throw new NoContentException(String.format("Provider id [%s] does not have registered orders", id));
            }

            logger.info("Found provider orders!");

            List<OrderResponseDto> orders = new ArrayList<>();

            for (int i = 0; i < customerOrders.size(); i++) {
                CustomerOrder customerOrder = pilha.pop();

                Customer customer = customerOrder.getCustomerId();

                CustomerResponseDto customerResponse = CustomerDtoFactory.toResponsePartialDto(customer);

                OrderResponseDto order = OrderDtoFactory.toResponseWithNotNullProviderDto(
                        customerOrder, customerResponse, providerResponse
                );
                orders.add(order);
            }

            return orders;
        }

        logger.error(String.format("Provider with id: [%d] not found", id));
        throw new NotFoundException(String.format("Provider with id [%d] not found!", id));
    }

    @Override
    public void cancelOrder(String orderId) throws InvalidOrderToCanceledException, NotFoundException {
        logger.info(String.format("Finding order with id: [%s]", orderId));
        if (orderRepository.existsAndFindByOrderUlid(orderId).isPresent()) {
            CustomerOrder order = orderRepository.existsAndFindByOrderUlid(orderId).get();

            if (validatePrerequisitesForCancelOrder(order)) {
                //TODO(desejável): Criar entidade de "lifecycle" de uma order, para registrar o motivo do cancelamento, e quem o fez
                order.setStatus(Status.CANCELED.name());
                orderRepository.save(order);

                logger.info("Order canceled successfully by provider!");
            } else {
                logger.error("This order is invalid to canceled by provider");
                throw new InvalidOrderToCanceledException("This order is invalid to canceled by provider");
            }
        } else {
            logger.error(String.format("Order with id: [%s] not found", orderId));
            throw new NotFoundException(String.format("Order with id: [%s] not found", orderId));
        }
    }

    private boolean isAuthenticatedProvider(Provider provider) {
        return provider.isAuthenticated();
    }

    private boolean isValidPassword(String passwordAttempt, Provider provider) {
        return encoder.matches(passwordAttempt, provider.getPassword());
    }

    private boolean thisCpfOrPhoneOrEmailIsAlreadyRegistered(String cpf, String phone, String email) {
        return providerRepository.existsByCpf(cpf) ||
                providerRepository.existsByPhone(phone) || providerRepository.existsByEmail(email);
    }

    private boolean validatePrerequisitesForCreatingAProposal(CustomerOrder order) {
        return (!order.isPaid() && !order.getStatus().equals(Status.CANCELED.name()) && !order.getStatus().equals(Status.DONE.name())
                && !order.getStatus().equals(Status.IN_PROGRESS.name()));
    }

    private boolean validatePrerequisitesForCancelOrder(CustomerOrder order) {
        return (!order.isPaid() && !order.getStatus().equals(Status.CANCELED.name()) && !order.getStatus().equals(Status.DONE.name())
                && !order.getStatus().equals(Status.IN_PROGRESS.name()) && !order.getStatus().equals(Status.WAITING_FOR_PROPOSAL));
    }

    private static final Logger logger = LogManager.getLogger(ProviderServiceImpl.class.getName());
}