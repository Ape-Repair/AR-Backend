package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.application.dto.request.DeleteRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.DeleteResponseDto;
import com.aperepair.aperepair.authorization.application.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.application.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.application.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.authorization.domain.exception.*;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ProviderService {

    ProviderResponseDto create(ProviderRequestDto request) throws BadRequestException, AlreadyRegisteredException;

    ResponseEntity<List<ProviderResponseDto>> findAll();

    ResponseEntity<ProviderResponseDto> findById(Integer id);

    ProviderResponseDto update(Integer id, ProviderRequestDto updatedProvider) throws NotAuthenticatedException, NotFoundException;

    DeleteResponseDto delete(DeleteRequestDto request) throws NotFoundException;

    LoginResponseDto login(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException;

    LogoutResponseDto logout(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException, NotAuthenticatedException;

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request) throws AwsUploadException, IOException, NotFoundException;

    GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws AwsServiceInternalException, IOException, AwsS3ImageException, NotFoundException;
}
