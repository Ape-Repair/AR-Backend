package com.aperepair.aperepair.authorization.service;

import com.aperepair.aperepair.authorization.model.Customer;
import com.aperepair.aperepair.authorization.model.dto.CustomerDto;
import com.aperepair.aperepair.authorization.model.dto.LoginDto;
import com.aperepair.aperepair.authorization.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.model.dto.response.LogoutResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CustomerService {

    ResponseEntity<CustomerDto> create(@RequestBody Customer customer);

    ResponseEntity<List<CustomerDto>> findAll();

    ResponseEntity<CustomerDto> findById(Integer id);

    ResponseEntity<CustomerDto> update(Integer id, Customer updatedCustomer);

    ResponseEntity<Boolean> delete(Integer id);

    ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto);

    ResponseEntity<LogoutResponseDto> logout(@RequestBody LoginDto loginDto);
}
