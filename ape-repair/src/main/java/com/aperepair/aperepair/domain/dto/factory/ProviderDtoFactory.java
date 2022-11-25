package com.aperepair.aperepair.domain.dto.factory;

import com.aperepair.aperepair.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.AddressResponseDto;
import com.aperepair.aperepair.application.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.domain.model.Provider;

public class ProviderDtoFactory {

    public static Provider toEntity(ProviderRequestDto providerRequestDto) {

        return new Provider(
                null,
                providerRequestDto.getName(),
                providerRequestDto.getEmail(),
                providerRequestDto.getPassword(),
                providerRequestDto.getGenre(),
                providerRequestDto.getCpf(),
                providerRequestDto.getPhone(),
                null,
                providerRequestDto.getSpecialtyType(),
                providerRequestDto.getRole(),
                providerRequestDto.isAuthenticated()
        );
    }

    public static Provider updateToEntity(ProviderUpdateRequestDto providerRequestDto) {

        return new Provider(
                null,
                providerRequestDto.getName(),
                providerRequestDto.getEmail(),
                providerRequestDto.getPassword(),
                providerRequestDto.getGenre(),
                providerRequestDto.getCpf(),
                providerRequestDto.getPhone(),
                null,
                providerRequestDto.getSpecialtyType(),
                providerRequestDto.getRole(),
                providerRequestDto.isAuthenticated()
        );
    }

    public static ProviderResponseDto toResponseFullDto(Provider provider, AddressResponseDto addressResponseDto) {

        return new ProviderResponseDto(
                provider.getName(),
                provider.getEmail(),
                provider.getGenre(),
                provider.getCpf(),
                provider.getPhone(),
                provider.getRole(),
                provider.isAuthenticated(),
                addressResponseDto,
                provider.getSpecialtyType()
        );
    }

    public static ProviderResponseDto toResponsePartialDto(Provider provider) {

        return new ProviderResponseDto(
                provider.getName(),
                provider.getEmail(),
                provider.getGenre(),
                provider.getCpf(),
                provider.getPhone(),
                provider.getRole(),
                provider.isAuthenticated(),
                provider.getSpecialtyType()
        );
    }
}