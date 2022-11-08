package com.aperepair.aperepair.authorization.domain.service.impl;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.domain.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.authorization.domain.dto.factory.ProviderDtoFactory;
import com.aperepair.aperepair.authorization.domain.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.domain.enums.Role;
import com.aperepair.aperepair.authorization.domain.repository.ProviderRepository;
import com.aperepair.aperepair.authorization.domain.service.ProviderService;
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
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public ResponseEntity<ProviderResponseDto> create(@RequestBody Provider provider) {
        String cpf = provider.getCpf();
        String cnpj = provider.getCnpj();
        String phone = provider.getPhone();

        if (thisCpfAndCnpjAreNull(cpf, cnpj)) return ResponseEntity.status(400).build();

        if (thisCpfOrCnpjOrPhoneIsAlreadyRegistered(cpf, cnpj, phone)) return ResponseEntity.status(400).build();

        provider.setPassword(encoder.encode(provider.getPassword()));
        logger.info("Provider password encrypted with successfully");

        provider.setAuthenticated(false);

        providerRepository.save(provider);

        ProviderResponseDto providerResponseDto = ProviderDtoFactory.toDto(provider);
        logger.info(String.format("Provider: %s registered successfully", providerResponseDto.toString()));

        return ResponseEntity.status(201).body(providerResponseDto);
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
            ProviderResponseDto providerResponseDto = ProviderDtoFactory.toDto(provider);
            providerResponseDtos.add(providerResponseDto);
        }

        logger.info("Success in finding registered providers");
        return ResponseEntity.status(200).body(providerResponseDtos);
    }

    @Override
    public ResponseEntity<ProviderResponseDto> findById(Integer id) {
        if (providerRepository.existsById(id)) {
            Optional<Provider> optionalProvider = providerRepository.findById(id);
            logger.info(String.format("Provider of id %d found", id));

            Provider provider = optionalProvider.get();

            ProviderResponseDto providerResponseDto = ProviderDtoFactory.toDto(provider);

            return ResponseEntity.status(200).body(providerResponseDto);
        }

        logger.error(String.format("Provider of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<ProviderResponseDto> update(Integer id, Provider updatedProvider) {
        if (providerRepository.existsById(id)) {

            Provider provider = providerRepository.findById(id).get();

            if (!isAuthenticatedProvider(provider)) {
                logger.error("Provider is not authenticated!");
                return ResponseEntity.status(403).build();
            }

            logger.info(String.format("Provider of id %d found", provider.getId()));

            provider.setName(updatedProvider.getName());
            provider.setEmail(updatedProvider.getEmail());
            provider.setCpf(updatedProvider.getCpf());
            provider.setGenre(updatedProvider.getGenre());
            provider.setPhone(updatedProvider.getPhone());
            provider.setCnpj(updatedProvider.getCnpj());
            provider.setPassword(encoder.encode(updatedProvider.getPassword()));

            providerRepository.save(provider);
            logger.info(String.format("Updated provider of id: %d registration data!", provider.getId()));

            ProviderResponseDto updatedProviderResponseDto = ProviderDtoFactory.toDto(provider);

            return ResponseEntity.status(200).body(updatedProviderResponseDto);
        }

        logger.error(String.format("Provider of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Boolean> delete(Integer id) {
        Boolean success = false;
        if (providerRepository.existsById(id)) {
            Provider provider = providerRepository.findById(id).get();
            logger.info(String.format("Provider of id %d found", id));

            ProviderResponseDto providerResponseDto = ProviderDtoFactory.toDto(provider);

            provider.setRole(Role.DELETED.name());
            providerRepository.save(provider);

            logger.info(String.format("Provider: %s successfully deleted", providerResponseDto.toString()));
            success = true;

            return ResponseEntity.status(200).body(success);
        }

        logger.error(String.format("Provider of id %d not found", id));
        return ResponseEntity.status(404).body(success);
    }

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

    private Boolean isAuthenticatedProvider(Provider provider) {
        if (provider.getAuthenticated()) return true;

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
