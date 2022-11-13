package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.domain.exception.AwsUploadException;
import com.aperepair.aperepair.authorization.domain.exception.CustomerNotFoundException;
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

    ResponseEntity<CustomerResponseDto> findById(Integer id) throws CustomerNotFoundException;

    ResponseEntity<CustomerResponseDto> update(Integer id, CustomerRequestDto updatedCustomer) throws CustomerNotFoundException;

    ResponseEntity<Boolean> delete(Integer id) throws CustomerNotFoundException;

    ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto) throws CustomerNotFoundException;

    ResponseEntity<LogoutResponseDto> logout(LoginRequestDto loginRequestDto) throws CustomerNotFoundException;

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request)
            throws IOException, AwsUploadException, CustomerNotFoundException;

    GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws Exception;
}
