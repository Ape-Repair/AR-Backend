package com.aperepair.aperepair.autorizadores.service.impl;

import com.aperepair.aperepair.autorizadores.model.Provider;
import com.aperepair.aperepair.autorizadores.repository.ProviderRepository;
import com.aperepair.aperepair.autorizadores.service.ProviderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    public ResponseEntity<Provider> create(@RequestBody Provider newProvider) {
        if (validateCNPJNewProvider(newProvider)) {
            providerRepository.save(newProvider);
            logger.info(String.format("Provider: %s registered successfully", newProvider.toString()));

            return ResponseEntity.status(201).body(newProvider);
        }

        logger.error(String.format("There was an error registering the provider", newProvider.toString()));
        return ResponseEntity.status(400).build();
    }

    public ResponseEntity<List<Provider>> findAll() {
        List<Provider> providers = new ArrayList(providerRepository.findAll());

        if (providers.isEmpty()) {
            logger.info("There are no registered providers");
            return ResponseEntity.status(204).build();
        }

        logger.info("Success in finding registered providers");
        return ResponseEntity.status(200).body(providers);
    }

    public ResponseEntity<Provider> findById(Integer id) {
        if (providerRepository.existsById(id)) {
            Optional<Provider> optionalProvider = providerRepository.findById(id);
            logger.info(String.format("Provider of id %d found", id));

            Provider provider = optionalProvider.get();

            return ResponseEntity.status(200).body(provider);
        }

        logger.error(String.format("Provider of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    public ResponseEntity<Provider> update(Integer id, Provider updatedProvider) {
        if (providerRepository.existsById(id)) {
            logger.info(String.format("Provider of id %d found", id));

            providerRepository.save(updatedProvider);
            logger.info(String.format("Updated provider: %s registration data!", updatedProvider.toString()));

            return ResponseEntity.status(200).body(updatedProvider);
        }

        logger.error(String.format("Provider of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    public ResponseEntity<Boolean> delete(Integer id) {
        Boolean success = false;
        if (providerRepository.existsById(id)) {
            Provider provider = providerRepository.findById(id).get();
            logger.info(String.format("Provider of id %d found", id));

            providerRepository.deleteById(id);
            logger.info(String.format("Provider: %s successfully deleted", provider.toString()));

            success = true;

            return ResponseEntity.status(200).body(success);
        }

        logger.error(String.format("Provider of id %d not found", id));
        return ResponseEntity.status(404).body(success);
    }

    private boolean validateCNPJNewProvider(Provider provider) {
        boolean isValid = true; // alterar aqui apos concluir TODO
        if (isValid) {
            logger.info(String.format("CNPJ: %s is valid", provider.getCnpj()));
            return true;
        }
        logger.info("CNPJ: %s is invalid", provider.getCnpj());
        return false;
        //TODO (API CNPJ) - implementar API de validar cnpj
    }

    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class.getName());
}