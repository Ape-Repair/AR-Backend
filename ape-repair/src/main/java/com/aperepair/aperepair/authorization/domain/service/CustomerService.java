package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.*;
import com.aperepair.aperepair.authorization.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface CustomerService {

    ResponseEntity<CustomerResponseDto> create(CustomerRequestDto customer);

    ResponseEntity<List<CustomerResponseDto>> findAll();

    ResponseEntity<CustomerResponseDto> findById(Integer id);

    ResponseEntity<CustomerResponseDto> update(Integer id, CustomerRequestDto updatedCustomer);

    ResponseEntity<Boolean> delete(Integer id);

    ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto);

    ResponseEntity<LogoutResponseDto> logout(LoginRequestDto loginRequestDto);

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request)
            throws IOException;

    GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws IOException;
}
