package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.DeleteRequestDto;
import com.aperepair.aperepair.authorization.domain.exception.*;
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

    CustomerResponseDto create(CustomerRequestDto customer) throws AlreadyRegisteredException;

    ResponseEntity<List<CustomerResponseDto>> findAll();

    ResponseEntity<CustomerResponseDto> findById(Integer id) throws NotFoundException;

    CustomerResponseDto update(Integer id, CustomerRequestDto updatedCustomer) throws NotFoundException, NotAuthenticatedException;

    DeleteResponseDto delete(DeleteRequestDto request) throws NotFoundException;

    LoginResponseDto login(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException;

    LogoutResponseDto logout(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, NotAuthenticatedException, BadCredentialsException;

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request)
            throws IOException, AwsUploadException, NotFoundException;

    GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws Exception;
}
