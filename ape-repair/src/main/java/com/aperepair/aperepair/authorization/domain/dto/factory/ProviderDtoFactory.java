package com.aperepair.aperepair.authorization.domain.dto.factory;

import com.aperepair.aperepair.authorization.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.ProviderUpdateRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.AddressResponseDto;
import com.aperepair.aperepair.authorization.application.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.authorization.domain.model.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProviderDtoFactory {

    public static Provider toEntity(ProviderRequestDto providerRequestDto) {
        Provider provider = new Provider(
                null,
                providerRequestDto.getName(),
                providerRequestDto.getEmail(),
                providerRequestDto.getPassword(),
                providerRequestDto.getGenre(),
                providerRequestDto.getCpf(),
                providerRequestDto.getPhone(),
                null,
                providerRequestDto.getRole(),
                providerRequestDto.isAuthenticated()
        );

        logger.info("ProviderRequestDto transformed to Provider entity with success!");
        return provider;
    }

    public static Provider updateToEntity(ProviderUpdateRequestDto providerRequestDto) {
        Provider provider = new Provider(
                null,
                providerRequestDto.getName(),
                providerRequestDto.getEmail(),
                providerRequestDto.getPassword(),
                providerRequestDto.getGenre(),
                providerRequestDto.getCpf(),
                providerRequestDto.getPhone(),
                null,
                providerRequestDto.getRole(),
                providerRequestDto.isAuthenticated()
        );

        logger.info("ProviderRequestDto transformed to Provider entity with success!");
        return provider;
    }

    public static ProviderResponseDto toResponseFullDto(Provider provider, AddressResponseDto addressResponseDto) {
        ProviderResponseDto providerResponseDto = new ProviderResponseDto(
                provider.getName(),
                provider.getEmail(),
                provider.getGenre(),
                provider.getCpf(),
                provider.getPhone(),
                provider.getRole(),
                provider.isAuthenticated(),
                addressResponseDto
        );

        logger.info("Provider transformed to ProviderResponseFullDto successfully");
        return providerResponseDto;
    }

    public static ProviderResponseDto toResponsePartialDto(Provider provider) {
        ProviderResponseDto providerResponseDto = new ProviderResponseDto(
                provider.getName(),
                provider.getEmail(),
                provider.getGenre(),
                provider.getCpf(),
                provider.getPhone(),
                provider.getRole(),
                provider.isAuthenticated()
        );

        logger.info("Provider transformed to ProviderResponsePartialDto with success!");
        return providerResponseDto;
    }

    private static final Logger logger = LogManager.getLogger(ProviderDtoFactory.class.getName());
}
