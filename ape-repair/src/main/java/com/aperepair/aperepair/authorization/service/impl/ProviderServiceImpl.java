package com.aperepair.aperepair.authorization.service.impl;

import com.aperepair.aperepair.authorization.model.Customer;
import com.aperepair.aperepair.authorization.model.Provider;
import com.aperepair.aperepair.authorization.model.dto.LoginDto;
import com.aperepair.aperepair.authorization.model.dto.ProviderDto;
import com.aperepair.aperepair.authorization.model.dto.factory.ProviderDtoFactory;
import com.aperepair.aperepair.authorization.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.model.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.model.enums.Role;
import com.aperepair.aperepair.authorization.repository.ProviderRepository;
import com.aperepair.aperepair.authorization.service.ProviderService;
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
    public ResponseEntity<ProviderDto> create(@RequestBody Provider provider) {
        provider.setRole(Role.PROVIDER);

        provider.setPassword(encoder.encode(provider.getPassword()));
        logger.info("Provider password encrypted with successfully");

        provider.setAuthenticated(false);

        providerRepository.save(provider);

        ProviderDto providerDto = ProviderDtoFactory.toDto(provider);
        logger.info(String.format("Provider: %s registered successfully", providerDto.toString()));

        return ResponseEntity.status(201).body(providerDto);
    }

    @Override
    public ResponseEntity<List<ProviderDto>> findAll() {
        List<Provider> providers = new ArrayList(providerRepository.findAll());

        if (providers.isEmpty()) {
            logger.info("There are no registered providers");
            return ResponseEntity.status(204).build();
        }

        List<ProviderDto> providerDtos = new ArrayList();

        for (Provider provider : providers) {
            ProviderDto providerDto = ProviderDtoFactory.toDto(provider);
            providerDtos.add(providerDto);
        }

        logger.info("Success in finding registered providers");
        return ResponseEntity.status(200).body(providerDtos);
    }

    @Override
    public ResponseEntity<ProviderDto> findById(Integer id) {
        if (providerRepository.existsById(id)) {
            Optional<Provider> optionalProvider = providerRepository.findById(id);
            logger.info(String.format("Provider of id %d found", id));

            Provider provider = optionalProvider.get();

            ProviderDto providerDto = ProviderDtoFactory.toDto(provider);

            return ResponseEntity.status(200).body(providerDto);
        }

        logger.error(String.format("Provider of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<ProviderDto> update(Integer id, Provider updatedProvider) {
        if (providerRepository.existsById(id)) {
            logger.info(String.format("Provider of id %d found", id));

            providerRepository.save(updatedProvider);

            ProviderDto updatedProviderDto = ProviderDtoFactory.toDto(updatedProvider);
            logger.info(String.format("Updated provider: %s registration data!", updatedProviderDto.toString()));


            return ResponseEntity.status(200).body(updatedProviderDto);
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

            ProviderDto providerDto = ProviderDtoFactory.toDto(provider);

            provider.setRole(Role.DELETED);
            providerRepository.save(provider);

            logger.info(String.format("Provider: %s successfully deleted", providerDto.toString()));
            success = true;

            return ResponseEntity.status(200).body(success);
        }

        logger.error(String.format("Provider of id %d not found", id));
        return ResponseEntity.status(404).body(success);
    }

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginDto loginDto) {
        LoginResponseDto loginResponseDto = new LoginResponseDto(false, Role.PROVIDER);

        String emailAttempt = loginDto.getEmail();
        String passwordAttempt = loginDto.getPassword();

        logger.info(String.format("Searching for provider by email: [%s]", emailAttempt));
        Optional<Provider> optionalProvider = providerRepository.findByEmail(emailAttempt);

        if (optionalProvider.isEmpty()) {
            logger.warn(String.format("Email provider: [%s] - Not Found!", emailAttempt));
            return ResponseEntity.status(400).body(loginResponseDto);
        }

        Provider provider = optionalProvider.get();
        logger.info(String.format("Trying to login with email: [%s] - as a provider", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, provider);

        if (valid && !isAuthenticatedProvider(provider)) {
            loginResponseDto.setSuccess(true);
        } else {
            if (!valid) logger.info("Password invalid!");

            if (isAuthenticatedProvider(provider)) {
                logger.info("Provider was already authenticated");
                return ResponseEntity.status(401).body(loginResponseDto);
            }

            if (provider.getRole() != Role.PROVIDER) {
                logger.fatal(String.format("[%S] - Incorrect role for this flow", provider.getRole()));
                return ResponseEntity.status(403).build();
            }

            return ResponseEntity.status(401).body(loginResponseDto);
        }

        provider.setAuthenticated(true);
        providerRepository.save(provider);

        logger.info("Login successfully");
        return ResponseEntity.status(200).body(loginResponseDto);
    }

    public ResponseEntity<LogoutResponseDto> logout(LoginDto loginDto) {
        LogoutResponseDto logoutResponse = new LogoutResponseDto(false);

        String emailAttempt = loginDto.getEmail();
        String passwordAttempt = loginDto.getPassword();

        Optional<Provider> optionalProvider = providerRepository.findByEmail(emailAttempt);

        if (optionalProvider.isEmpty()) {
            logger.warn(String.format("Email provider: [%s] - Not Found!", emailAttempt));
            return ResponseEntity.status(404).body(logoutResponse);
        }

        Provider provider = optionalProvider.get();
        logger.info(String.format("Initiating logout from email provider [%s]", emailAttempt));

        boolean valid = isValidPassword(passwordAttempt, provider);

        if (valid && isAuthenticatedProvider(provider) && provider.getRole().equals(Role.PROVIDER)) {
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

            if (provider.getRole() != Role.PROVIDER) {
                logger.fatal(String.format("[%S] - Incorrect role for this flow", provider.getRole()));
                return ResponseEntity.status(403).build();
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

    private static final Logger logger = LogManager.getLogger(ProviderServiceImpl.class.getName());
}
