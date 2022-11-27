package com.aperepair.aperepair.domain.service;

import com.aperepair.aperepair.application.dto.request.CreateOrderRequestDto;
import com.aperepair.aperepair.application.dto.request.CredentialsRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.domain.exception.*;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.List;

public interface CustomerService {

    CustomerResponseDto create(CustomerRequestDto customer) throws AlreadyRegisteredException, BadRequestException;

    ResponseEntity<List<CustomerResponseDto>> findAll();

    ResponseEntity<CustomerResponseDto> findById(Integer id) throws NotFoundException;

    CustomerResponseDto update(Integer id, CustomerUpdateRequestDto updatedCustomer) throws NotFoundException, NotAuthenticatedException;

    DeleteResponseDto delete(Integer id) throws NotFoundException;

    LoginResponseDto login(CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException;

    LogoutResponseDto logout(CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, NotAuthenticatedException, BadCredentialsException;

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request)
            throws IOException, AwsUploadException, NotFoundException;

    GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws Exception;

    void createOrder(CreateOrderRequestDto request) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, InvalidServiceTypeException;

    List<OrderResponseDto> getAllOrders(Integer id) throws NotFoundException, InvalidRoleException, NotAuthenticatedException, NoContentException;

    List<ProposalResponseDto> getProposalsForOrder(Integer orderId) throws NoContentException, NotFoundException;

    void acceptProposal(Integer orderId, Integer proposalId) throws NotFoundException, InvalidProposalForThisOrderException;
}