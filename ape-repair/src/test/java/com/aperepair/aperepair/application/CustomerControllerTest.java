package com.aperepair.aperepair.application;

import controller.CustomerController;
import com.aperepair.aperepair.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.application.dto.response.LoginResponseDto;
import com.aperepair.aperepair.application.exception.BadCredentialsException;
import com.aperepair.aperepair.application.exception.InvalidRoleException;
import com.aperepair.aperepair.application.exception.NotFoundException;
import com.aperepair.aperepair.domain.model.Address;
import com.aperepair.aperepair.domain.model.Customer;
import com.aperepair.aperepair.domain.repository.CustomerRepository;
import com.aperepair.aperepair.domain.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerControllerTest {

    @Autowired
    private CustomerController controller;

    @MockBean
    private CustomerService service;

    @MockBean
    private CustomerRepository repository;

    private Address address = new Address(
        "Rua central",
        123,
        "Apto 34",
        "13610020",
        "Centro",
        "São Paulo",
        "SP"
    );

    private Customer customer = new Customer(
            1,
            "Dummy",
            "teste@email.com",
            "123456",
            "MALE",
            "35754445873",
            "11992340910",
            address,
            "CUSTOMER",
            false
    );

    @Test
    @DisplayName(value = "should login a customer with success")
    void shouldLoginACustomerWithSuccess() throws NotFoundException, InvalidRoleException, BadCredentialsException {
        LoginRequestDto request = new LoginRequestDto(customer.getEmail(), customer.getPassword());
        LoginResponseDto expectedResponse = new LoginResponseDto(1, true, customer.getRole());

        when(service.login(request)).thenReturn(expectedResponse);

        LoginResponseDto response = controller.login(request);

        assertEquals(expectedResponse, response);
    }
}