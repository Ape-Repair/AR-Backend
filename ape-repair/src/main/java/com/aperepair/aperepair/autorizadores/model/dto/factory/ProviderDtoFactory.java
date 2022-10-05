package com.aperepair.aperepair.autorizadores.model.dto.factory;

import com.aperepair.aperepair.autorizadores.model.Provider;
import com.aperepair.aperepair.autorizadores.model.dto.ProviderDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProviderDtoFactory {

    public static ProviderDto toDto(Provider provider) {
        ProviderDto providerDto = new ProviderDto(
                provider.getName(),
                provider.getEmail(),
                provider.getGenre(),
                provider.getCpf(),
                provider.getCnpj()
        );

        logger.info("Provider transformed to DTO with successfully");
        return providerDto;
    }

    private static final Logger logger = LogManager.getLogger(ProviderDtoFactory.class.getName());
}
