package com.aperepair.aperepair.domain.dto.factory;

import com.aperepair.aperepair.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.AddressResponseDto;
import com.aperepair.aperepair.application.dto.response.CustomerResponseDto;
import com.aperepair.aperepair.domain.model.Customer;

public class CustomerDtoFactory {

    public static Customer toEntity(CustomerRequestDto customerRequestDto) {

        return new Customer(
                null,
                customerRequestDto.getName(),
                customerRequestDto.getEmail(),
                customerRequestDto.getPassword(),
                customerRequestDto.getGenre(),
                customerRequestDto.getCpf(),
                customerRequestDto.getPhone(),
                null,
                customerRequestDto.getRole(),
                customerRequestDto.isAuthenticated()
        );
    }

    public static Customer updatedToEntity(CustomerUpdateRequestDto customerRequestDto) {

        return new Customer(
                null,
                customerRequestDto.getName(),
                customerRequestDto.getEmail(),
                customerRequestDto.getPassword(),
                customerRequestDto.getGenre(),
                customerRequestDto.getCpf(),
                customerRequestDto.getPhone(),
                null,
                customerRequestDto.getRole(),
                customerRequestDto.isAuthenticated()
        );
    }

    public static CustomerResponseDto toResponseFullDto(Customer customer, AddressResponseDto addressResponseDto) {

        return new CustomerResponseDto(
                customer.getName(),
                customer.getEmail(),
                customer.getGenre(),
                customer.getCpf(),
                customer.getPhone(),
                customer.getRole(),
                customer.isAuthenticated(),
                addressResponseDto
        );
    }

    public static CustomerResponseDto toResponsePartialDto(Customer customer) {

        return new CustomerResponseDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getGenre(),
                customer.getCpf(),
                customer.getPhone(),
                customer.getRole(),
                customer.isAuthenticated()
        );
    }
}