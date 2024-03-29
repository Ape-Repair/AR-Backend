package com.aperepair.aperepair.domain.service;

import com.aperepair.aperepair.application.dto.request.*;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.application.exception.*;
import com.aperepair.aperepair.report.resources.QueueObj;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface CustomerService {

    CustomerResponseDto create(CustomerRequestDto customer) throws AlreadyRegisteredException, BadRequestException;

    ResponseEntity<QueueObj<CustomerResponseDto>> findAll();

    ResponseEntity<CustomerResponseDto> findById(Integer id) throws NotFoundException;

    CustomerResponseDto update(Integer id, CustomerUpdateRequestDto updatedCustomer) throws NotFoundException, NotAuthenticatedException;

    DeleteResponseDto delete(Integer id) throws NotFoundException;

    LoginResponseDto login(LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException;

    LogoutResponseDto logout();

    ProfilePictureCreationResponseDto profilePictureCreation(ProfilePictureCreationRequestDto request)
            throws IOException, AwsUploadException, NotFoundException;

    GetProfilePictureResponseDto getProfilePicture(GetProfilePictureRequestDto request) throws Exception;

    OrderUlidResponseDto createOrder(CreateOrderRequestDto request) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, InvalidServiceTypeException, StatusInvalidToCreateOrder;

    List<OrderResponseDto> getAllOrders(Integer id) throws NotFoundException, InvalidRoleException, NotAuthenticatedException, NoContentException;

    List<ProposalResponseDto> getProposalsForOrder(String orderId) throws NoContentException, NotFoundException;

    void acceptProposal(String orderId, Integer proposalId) throws NotFoundException, InvalidProposalForThisOrderException;

    void makePayment(String orderId) throws NotFoundException, InvalidOrderForPaymentException, InvalidRoleException, NotAuthenticatedException, IOException, AwsServiceInternalException;

    void concludeOrder(String orderId) throws NotFoundException, InvalidOrderToConcludeException, InvalidRoleException, NotAuthenticatedException;

    void cancelOrder(String orderId) throws NotFoundException, InvalidOrderToCanceledException;

    InputStream getReceipt(String orderId) throws InvalidRoleException, NotAuthenticatedException, InvalidOrderStatusException, NotFoundException, AwsServiceInternalException, AwsS3ImageException;
}