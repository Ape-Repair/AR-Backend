package com.aperepair.aperepair.application.controller;

import com.aperepair.aperepair.application.dto.request.CreateProposalRequestDto;
import com.aperepair.aperepair.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.application.exception.*;
import com.aperepair.aperepair.domain.service.ProviderService;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProviderResponseDto create(@RequestBody @Valid ProviderRequestDto newProvider) throws AlreadyRegisteredException, BadRequestException, InvalidRoleException {
        logger.info("Calling service to create a provider!");

        return providerService.create(newProvider);
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponseDto>> findAll() {
        return providerService.findAll();
    }

    @GetMapping("/{providerId}")
    public ResponseEntity<ProviderResponseDto> findById(@PathVariable Integer providerId) {
        return providerService.findById(providerId);
    }

    @PutMapping("/{providerId}")
    @ResponseStatus(HttpStatus.OK)
    public ProviderResponseDto update(
            @PathVariable Integer providerId,
            @RequestBody @Valid ProviderUpdateRequestDto updatedProvider
    ) throws NotAuthenticatedException, NotFoundException, BadRequestException, InvalidRoleException {
        logger.info(String.format("Calling service to update provider with id: [%d]", providerId));

        return providerService.update(providerId, updatedProvider);
    }

    @PatchMapping("/{providerId}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteResponseDto delete(@PathVariable Integer providerId) throws NotFoundException {
        logger.info(String.format("Calling service to delete a provider with id: [%d]", providerId));

        return providerService.delete(providerId);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException {
        logger.info(String.format("Calling service to authenticate a provider with email: %s", loginRequestDto.getEmail()));

        return providerService.login(loginRequestDto);
    }

    @PatchMapping("/logout")
    public LogoutResponseDto logout(@RequestBody @Valid LoginRequestDto loginRequestDto) throws NotAuthenticatedException, NotFoundException, InvalidRoleException, BadCredentialsException {
        logger.info(String.format("Calling service to logout a provider with email: [%s]", loginRequestDto.getEmail()));

        return providerService.logout(loginRequestDto);
    }

    @PutMapping("/profile-picture")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfilePictureCreationResponseDto profilePictureCreation(
            @RequestBody @Valid ProfilePictureCreationRequestDto request
    ) throws IOException, AwsUploadException, NotFoundException {
        logger.info(String.format("Calling service to upload provider profile image for email: [%s]", request.getEmail()));

        return providerService.profilePictureCreation(request);
    }

    @PostMapping("/profile-picture")
    @ResponseStatus(HttpStatus.OK)
    public GetProfilePictureResponseDto getProfilePicture(
            @RequestBody @Valid GetProfilePictureRequestDto request
    ) throws Exception {
        logger.info(String.format("Calling service to get provider profile image for email: [%s]", request.getEmail()));

        return providerService.getProfilePicture(request);
    }

    @PostMapping("/in/create-proposal")
    @ResponseStatus(HttpStatus.CREATED)
    public ProposalResponseDto createProposal(@Valid @RequestBody CreateProposalRequestDto request) throws NotFoundException, NotAuthenticatedException, BadRequestException, SpecialtyNotMatchWithServiceTypeException, InvalidProposalForThisOrderException, ProviderAlreadyMadeProposalForOrderException, InvalidRoleException {
        logger.info(String.format("Calling service to create proposal for order: [%s]", request.getOrderId()));

        return providerService.createProposal(request);
    }

    @GetMapping("/in/available-orders/{providerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> getAllAvailableOrders(@PathVariable Integer providerId) throws NotAuthenticatedException, BadRequestException, NotFoundException, NoContentException, InvalidRoleException {
        logger.info(String.format("Calling service to list all available orders for provider with id: [%d]", providerId));

        return providerService.getAllAvailableOrders(providerId);
    }

    @PutMapping("/order/{orderId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@PathVariable String orderId) throws NotFoundException, InvalidOrderToCanceledException {
        logger.info(String.format("Calling service to cancel an order with id: [%s]", orderId));

        providerService.cancelOrder(orderId);
    }

    @GetMapping("/{providerId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> allOrders(@PathVariable Integer providerId) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, NoContentException {
        logger.info(String.format("Calling service to get all orders of provider with id: [%d]", providerId));

        return providerService.getAllOrders(providerId);
    }

    private static final Logger logger = LogManager.getLogger(ProviderController.class.getName());
}