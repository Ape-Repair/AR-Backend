package com.aperepair.aperepair.authorization.service.impl;

import com.aperepair.aperepair.authorization.model.Provider;
import com.aperepair.aperepair.authorization.model.dto.LoginDto;
import com.aperepair.aperepair.authorization.model.dto.ProviderDto;
import com.aperepair.aperepair.authorization.model.dto.factory.ProviderDtoFactory;
import com.aperepair.aperepair.authorization.model.dto.response.LoginResponseDto;
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

    public ResponseEntity<LoginResponseDto> login(LoginDto loginDto) {
       //TODO (will be implemented! - copy for CustomerServiceImpl);
        return null;
    }

    private Boolean isAuthenticatedProvider(Provider provider) {
        if (provider.getAuthenticated() == true) return true;

        return false;
    }

    private static final Logger logger = LogManager.getLogger(ProviderServiceImpl.class.getName());
}
