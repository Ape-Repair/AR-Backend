package com.aperepair.aperepair.authorization.domain.service.impl;

import com.aperepair.aperepair.authorization.application.dto.request.DeleteRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.*;
import com.aperepair.aperepair.authorization.domain.dto.factory.AddressDtoFactory;
import com.aperepair.aperepair.authorization.domain.dto.factory.ProviderDtoFactory;
import com.aperepair.aperepair.authorization.domain.enums.Role;
import com.aperepair.aperepair.authorization.domain.exception.*;
import com.aperepair.aperepair.authorization.domain.gateway.ProfilePictureGateway;
import com.aperepair.aperepair.authorization.domain.model.Address;
import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.repository.AddressRepository;
import com.aperepair.aperepair.authorization.domain.repository.ProviderRepository;
import com.aperepair.aperepair.authorization.domain.service.ProviderService;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
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
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ProfilePictureGateway profilePictureGateway;

    @Override
    public ProviderResponseDto create(ProviderRequestDto request) throws BadRequestException, AlreadyRegisteredException {
        String cpf = request.getCpf();
        String cnpj = request.getCnpj();
        String phone = request.getPhone();

        if (thisCpfAndCnpjAreNull(cpf, cnpj)) {
            logger.error("CPF and CNPJ cannot be null");

            throw new BadRequestException("CPF and CNPJ cannot be null");
        }

        if (thisCpfOrCnpjOrPhoneIsAlreadyRegistered(cpf, cnpj, phone)) {
            logger.error("CPF or CNPJ or Phone is already registered");

            throw new AlreadyRegisteredException("CPF or CNPJ or Phone is already registered");
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
        logger.info("Address registered successfully for customer");

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
    public ProviderResponseDto update(Integer id, ProviderRequestDto updatedProvider) throws NotAuthenticatedException, NotFoundException {
        if (providerRepository.existsById(id)) {

            Provider provider = providerRepository.findById(id).get();

            if (!isAuthenticatedProvider(provider)) {
                ProviderResponseDto providerResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);
                logger.error(String.format("Provider: [%s] is not authenticated", providerResponseDto));
                throw new NotAuthenticatedException(String.format("Provider: [%s] is not authenticated", providerResponseDto));
            }

            logger.info(String.format("Provider of id %d found", provider.getId()));

            updatedProvider.setRole(provider.getRole());
            updatedProvider.setAuthenticated(true);

            Provider newProvider = ProviderDtoFactory.toEntity(updatedProvider);

            Address address = AddressDtoFactory.toEntity(updatedProvider.getAddress());

            newProvider.setId(id);
            newProvider.setAddressId(provider.getAddressId());

            address.setId(provider.getAddressId().getId());

            providerRepository.save(newProvider);
            addressRepository.save(address);

            logger.info(String.format("Updated provider of id: %d registration data!", newProvider.getId()));

            ProviderResponseDto updatedProviderResponseDto = ProviderDtoFactory.toResponsePartialDto(provider);

            return updatedProviderResponseDto;
        }

        logger.error(String.format("Provider with id [%d] not found!", id));
        throw new NotFoundException(String.format("Provider with id [%d] not found!", id));
    }

    @Override
    public DeleteResponseDto delete(DeleteRequestDto request) throws NotFoundException {
        if (providerRepository.existsById(request.getId())) {
            Provider provider = providerRepository.findById(request.getId()).get();
            logger.info(String.format("Provider id %d found", request.getId()));

            provider.setRole(Role.DELETED.name());
            providerRepository.save(provider);

            logger.info(String.format("Provider id: %d successfully deleted", provider.getId()));

            DeleteResponseDto response = new DeleteResponseDto(true);

            return response;
        }

        logger.error(String.format("Provider with id: [%d] not found", request.getId()));
        throw new NotFoundException(String.format("Provider with id [%d] not found!", request.getId()));
    }

    //TODO: Refatorar login e logout, para deixar igual ao customer;
    @Override
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto(false, Role.PROVIDER);

        String emailAttempt = loginRequestDto.getEmail();
        String passwordAttempt = loginRequestDto.getPassword();

        logger.info(String.format("Searching for provider by email: [%s]", emailAttempt));
        Optional<Provider> optionalProvider = providerRepository.findByEmail(emailAttempt);

        if (optionalProvider.isEmpty()) {
            logger.warn(String.format("Email provider: [%s] - Not Found!", emailAttempt));
            return ResponseEntity.status(400).body(loginResponseDto);
        }

        Provider provider = optionalProvider.get();

        if (provider.getRole() != Role.PROVIDER.name()) {
            logger.fatal(String.format("[%S] - Incorrect role for this flow", provider.getRole()));
            return ResponseEntity.status(403).build();
        }

        logger.info(String.format("Trying to login with email: [%s] - as a provider", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, provider);

        if (valid) {
            loginResponseDto.setSuccess(true);
        } else {
            logger.info("Password invalid!");

            return ResponseEntity.status(401).body(loginResponseDto);
        }

        provider.setAuthenticated(true);
        providerRepository.save(provider);

        logger.info("Login successfully");
        return ResponseEntity.status(200).body(loginResponseDto);
    }

    public ResponseEntity<LogoutResponseDto> logout(LoginRequestDto loginRequestDto) {
        LogoutResponseDto logoutResponse = new LogoutResponseDto(false);

        String emailAttempt = loginRequestDto.getEmail();
        String passwordAttempt = loginRequestDto.getPassword();

        Optional<Provider> optionalProvider = providerRepository.findByEmail(emailAttempt);

        if (optionalProvider.isEmpty()) {
            logger.warn(String.format("Email provider: [%s] - Not Found!", emailAttempt));
            return ResponseEntity.status(404).body(logoutResponse);
        }

        Provider provider = optionalProvider.get();

        if (provider.getRole() != Role.PROVIDER.name()) {
            logger.fatal(String.format("[%S] - Incorrect role for this flow", provider.getRole()));
            return ResponseEntity.status(403).build();
        }

        logger.info(String.format("Initiating logout from email provider [%s]", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, provider);

        if (valid && isAuthenticatedProvider(provider)) {
            logoutResponse.setSuccess(true);
            provider.setAuthenticated(false);

            providerRepository.save(provider);

            logger.info("Logout successfully executed!");
            return ResponseEntity.status(200).body(logoutResponse);
        } else {
            if (!valid) logger.info("Password invalid!");

            if (!isAuthenticatedProvider(provider)) {
                logger.info("The provider is not authenticated");
                return ResponseEntity.status(401).body(logoutResponse);
            }

            return ResponseEntity.status(401).body(logoutResponse);
        }
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
            logger.info(String.format("Searching profile picture for customer: [%s]", email));

            GetProfilePictureResponseDto response = new GetProfilePictureResponseDto(null);
            String imageBase64 = profilePictureGateway.getProfilePicture(request);

            response.setImageBase64(imageBase64);

            return response;
        }

        logger.error(String.format("Provider with email [%s] not found!", email));
        throw new NotFoundException(String.format("Provider with email [%s] not found!", email));
    }

    private Boolean isAuthenticatedProvider(Provider provider) {
        if (provider.isAuthenticated()) return true;

        return false;
    }

    private boolean isValidPassword(String passwordAttempt, Provider provider) {
        if (encoder.matches(passwordAttempt, provider.getPassword())) return true;

        return false;
    }

    private boolean thisCpfOrCnpjOrPhoneIsAlreadyRegistered(String cpf, String cnpj, String phone) {
        if (providerRepository.existsByCpf(cpf) ||
                providerRepository.existsByCnpj(cnpj) ||
                providerRepository.existsByPhone(phone)) return true;

        return false;
    }

    private boolean thisCpfAndCnpjAreNull(String cpf, String cnpj) {
        if (cpf == null && cnpj == null) {
            return true;
        }

        return false;
    }

    private static final Logger logger = LogManager.getLogger(ProviderServiceImpl.class.getName());
}
