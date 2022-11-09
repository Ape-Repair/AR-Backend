package com.aperepair.aperepair.authorization.domain.dto.factory;

import com.aperepair.aperepair.authorization.application.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.authorization.domain.model.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProviderDtoFactory {

    public static ProviderResponseDto toDto(Provider provider) {
        ProviderResponseDto providerResponseDto = new ProviderResponseDto(
                provider.getName(),
                provider.getEmail(),
                provider.getGenre(),
                provider.getRole(),
                provider.getAuthenticated()
        );

        logger.info("Provider transformed to DTO with successfully");
        return providerResponseDto;
    }

    private static final Logger logger = LogManager.getLogger(ProviderDtoFactory.class.getName());
}
