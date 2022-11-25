package com.aperepair.aperepair.domain.dto.factory;

import com.aperepair.aperepair.application.dto.request.AddressRequestDto;
import com.aperepair.aperepair.application.dto.response.AddressResponseDto;
import com.aperepair.aperepair.domain.model.Address;

public class AddressDtoFactory {

    public static Address toEntity(AddressRequestDto addressRequestDto) {

        return new Address(
                addressRequestDto.getStreet(),
                addressRequestDto.getStreetNumber(),
                addressRequestDto.getComplement(),
                addressRequestDto.getCep(),
                addressRequestDto.getDistrict(),
                addressRequestDto.getCity(),
                addressRequestDto.getUf()
        );
    }

    public static AddressResponseDto toResponseDto(Address address) {

        return new AddressResponseDto(
                address.getStreet(),
                address.getStreetNumber(),
                address.getComplement(),
                address.getCep(),
                address.getDistrict(),
                address.getCity(),
                address.getUf()
        );
    }
}