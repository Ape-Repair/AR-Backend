package com.aperepair.aperepair.autorizadores.model.dto;

import com.aperepair.aperepair.autorizadores.model.Customer;

public class CustomerFactory {

    public static SaveCustomerDto toDto(Customer customer) {
        SaveCustomerDto customerDto = new SaveCustomerDto(
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getGenre(),
                customer.getCpf()
        );

        return customerDto;
    }
}
