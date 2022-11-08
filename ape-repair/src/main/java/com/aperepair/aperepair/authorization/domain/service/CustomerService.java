package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.dto.response.CustomerResponseDto;
import com.aperepair.aperepair.authorization.domain.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.domain.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.domain.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    ResponseEntity<CustomerResponseDto> create(Customer customer);

    ResponseEntity<List<CustomerResponseDto>> findAll();

    ResponseEntity<CustomerResponseDto> findById(Integer id);

    ResponseEntity<CustomerResponseDto> update(Integer id, Customer updatedCustomer);

    ResponseEntity<Boolean> delete(Integer id);

    ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto);

    ResponseEntity<LogoutResponseDto> logout(LoginRequestDto loginRequestDto);

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request)
            throws IOException;
}
