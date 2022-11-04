package com.aperepair.aperepair.matchs.domain.service.impl;

import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.model.enums.Genre;
import com.aperepair.aperepair.authorization.domain.repository.ProviderRepository;
import com.aperepair.aperepair.matchs.domain.model.Solicitation;
import com.aperepair.aperepair.matchs.domain.repository.MatchRepository;
import com.aperepair.aperepair.matchs.domain.service.MatchFactoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchFactoryServiceImpl implements MatchFactoryService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ProviderRepository providerRepository;

    public ResponseEntity requestMatch(Solicitation solicitation) {
        Customer requestingCustomer = solicitation.getCustomer();

        if (requestingCustomer.getAuthenticated()) {
            if (requestingCustomer.getGenre() == Genre.F && findByFemaleProvider()) {
                logger.info("Finding for female providers");

                return ResponseEntity.status(202).build();
            }

            List<Provider> authenticatedProviders = providerRepository.findByIsAuthenticatedTrue();
            authenticatedProviders.notificar();

            return ResponseEntity.status(202).build();
        }

        logger.error("Customer is not authenticated");

        return ResponseEntity.status(403).build();

    }

    private boolean findByFemaleProvider() {
        Optional<List<Provider>> femaleProvidersOpt = providerRepository
                .findByGenreIsFAndIsAuthenticatedTrue();

        if (femaleProvidersOpt.isEmpty()) {
            logger.info("Female providers not found");
            return false;
        }

        logger.info("Female providers were found");

        List<Provider> femaleProviders = femaleProvidersOpt.get();
        femaleProviders.notificar();

        logger.info("Female providers notified!");

        return true;
    }

    private static final Logger logger = LogManager.getLogger(MatchFactoryServiceImpl.class.getName());
}