package com.aperepair.aperepair.domain.service;

import com.aperepair.aperepair.application.dto.request.CreateProposalRequestDto;
import com.aperepair.aperepair.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.application.exception.*;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface ProviderService {

    ProviderResponseDto create(ProviderRequestDto request) throws BadRequestException, AlreadyRegisteredException, InvalidRoleException;

    ResponseEntity<List<ProviderResponseDto>> findAll();

    ResponseEntity<ProviderResponseDto> findById(Integer providerId);

    ProviderResponseDto update(Integer providerId, ProviderUpdateRequestDto updatedProvider) throws NotAuthenticatedException, NotFoundException, BadRequestException, InvalidRoleException;

    DeleteResponseDto delete(Integer providerId) throws NotFoundException;

    LoginResponseDto login(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException;

    LogoutResponseDto logout(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException, NotAuthenticatedException;

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request) throws AwsUploadException, IOException, NotFoundException;

    GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws AwsServiceInternalException, IOException, AwsS3ImageException, NotFoundException;

    ProposalResponseDto createProposal(CreateProposalRequestDto request) throws NotFoundException, NotAuthenticatedException, BadRequestException, SpecialtyNotMatchWithServiceTypeException, InvalidProposalForThisOrderException, ProviderAlreadyMadeProposalForOrderException, InvalidRoleException;

    List<OrderResponseDto> getAllAvailableOrders(Integer providerId) throws NotAuthenticatedException, BadRequestException, NotFoundException, NoContentException, InvalidRoleException;

    List<OrderResponseDto> getAllOrders(Integer providerId) throws NotFoundException, InvalidRoleException, NotAuthenticatedException, NoContentException;

    void cancelOrder(String orderId) throws InvalidOrderToCanceledException, NotFoundException;
}