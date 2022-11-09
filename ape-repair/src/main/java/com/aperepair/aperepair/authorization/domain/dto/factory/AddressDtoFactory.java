package com.aperepair.aperepair.authorization.domain.dto.factory;

import com.aperepair.aperepair.authorization.application.dto.request.AddressRequestDto;
import com.aperepair.aperepair.authorization.domain.model.Address;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AddressDtoFactory {

    public static Address toEntity(AddressRequestDto addressRequestDto) {
        Address address = new Address(
                addressRequestDto.getStreet(),
                addressRequestDto.getStreetNumber(),
                addressRequestDto.getComplement(),
                addressRequestDto.getCep(),
                addressRequestDto.getDistrict(),
                addressRequestDto.getCity(),
                addressRequestDto.getUf()
                );

        logger.info("AddressRequestDto transformed to Address entity with successfully");
        return address;
    }

    private static final Logger logger = LogManager.getLogger(AddressDtoFactory.class.getName());
}