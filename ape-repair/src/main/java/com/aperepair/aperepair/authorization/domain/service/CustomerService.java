package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.model.dto.CustomerDto;
import com.aperepair.aperepair.authorization.domain.model.dto.request.LoginDto;
import com.aperepair.aperepair.authorization.domain.model.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    ResponseEntity<CustomerDto> create(Customer customer);

    ResponseEntity<List<CustomerDto>> findAll();

    ResponseEntity<CustomerDto> findById(Integer id);

    ResponseEntity<CustomerDto> update(Integer id, Customer updatedCustomer);

    ResponseEntity<Boolean> delete(Integer id);

    ResponseEntity<LoginResponseDto> login(LoginDto loginDto);

    ResponseEntity<LogoutResponseDto> logout(LoginDto loginDto);

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request)
            throws IOException;
}
