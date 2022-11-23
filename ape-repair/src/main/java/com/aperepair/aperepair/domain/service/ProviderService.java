package com.aperepair.aperepair.domain.service;

import com.aperepair.aperepair.application.dto.request.CreateProposalRequestDto;
import com.aperepair.aperepair.application.dto.request.CredentialsRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.domain.exception.*;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ProviderService {

    ProviderResponseDto create(ProviderRequestDto request) throws BadRequestException, AlreadyRegisteredException;

    ResponseEntity<List<ProviderResponseDto>> findAll();

    ResponseEntity<ProviderResponseDto> findById(Integer id);

    ProviderResponseDto update(Integer id, ProviderUpdateRequestDto updatedProvider) throws NotAuthenticatedException, NotFoundException, BadRequestException;

    DeleteResponseDto delete(Integer id) throws NotFoundException;

    LoginResponseDto login(CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException;

    LogoutResponseDto logout(CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException, NotAuthenticatedException;

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request) throws AwsUploadException, IOException, NotFoundException;

    GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws AwsServiceInternalException, IOException, AwsS3ImageException, NotFoundException;

    ProposalResponseDto createProposal(CreateProposalRequestDto request) throws NotFoundException, NotAuthenticatedException, BadRequestException, SpecialtyNotMatchWithServiceTypeException;
}