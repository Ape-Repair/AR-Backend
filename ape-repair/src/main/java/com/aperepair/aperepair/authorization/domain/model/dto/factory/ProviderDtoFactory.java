package com.aperepair.aperepair.authorization.domain.model.dto.factory;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.model.dto.ProviderDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProviderDtoFactory {

    public static ProviderDto toDto(Provider provider) {
        ProviderDto providerDto = new ProviderDto(
                provider.getName(),
                provider.getEmail(),
                provider.getGenre(),
                provider.getRole(),
                provider.getAuthenticated()
        );

        logger.info("Provider transformed to DTO with successfully");
        return providerDto;
    }

    private static final Logger logger = LogManager.getLogger(ProviderDtoFactory.class.getName());
}