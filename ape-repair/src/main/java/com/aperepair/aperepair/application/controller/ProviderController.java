package com.aperepair.aperepair.application.controller;

import com.aperepair.aperepair.application.dto.request.CreateProposalRequestDto;
import com.aperepair.aperepair.application.dto.request.CredentialsRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.application.dto.request.ProviderUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.domain.exception.*;
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

@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProviderResponseDto create(@RequestBody @Valid ProviderRequestDto newProvider) throws AlreadyRegisteredException, BadRequestException {
        logger.info("Calling ProviderService to create a provider!");

        return providerService.create(newProvider);
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponseDto>> findAll() {
        return providerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponseDto> findById(@PathVariable Integer id) {
        return providerService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProviderResponseDto update(
            @PathVariable Integer id,
            @RequestBody @Valid ProviderUpdateRequestDto updatedProvider
    ) throws NotAuthenticatedException, NotFoundException, BadRequestException {
        logger.info("Calling ProviderService to update provider!");

        return providerService.update(id, updatedProvider);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteResponseDto delete(@PathVariable Integer id) throws NotFoundException {
        logger.info("Calling ProviderService to delete a provider");

        return providerService.delete(id);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException {
        logger.info("Calling ProviderService to authenticate a customer");

        return providerService.login(credentialsRequestDto);
    }

    @PatchMapping("/logout")
    public LogoutResponseDto logout(@RequestBody @Valid CredentialsRequestDto credentialsRequestDto) throws NotAuthenticatedException, NotFoundException, InvalidRoleException, BadCredentialsException {
        logger.info("Calling ProviderService to logout a customer");

        return providerService.logout(credentialsRequestDto);
    }

    @PutMapping("/profile-picture")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfilePictureCreationResponseDto profilePictureCreation(
            @RequestBody @Valid ProfilePictureCreationRequestDto request
    ) throws IOException, AwsUploadException, NotFoundException {
        logger.info("Calling ProviderService to upload customer profile image!");

        return providerService.profilePictureCreation(request);
    }

    @PostMapping("/profile-picture")
    @ResponseStatus(HttpStatus.OK)
    public GetProfilePictureResponseDto getProfilePicture(
            @RequestBody @Valid GetProfilePictureRequestDto request
    ) throws Exception {
        logger.info("Calling ProviderService to get customer profile image!");

        return providerService.getProfilePicture(request);
    }

    @PostMapping("/in/create-proposal")
    @ResponseStatus(HttpStatus.CREATED)
    public ProposalResponseDto createProposal(@Valid @RequestBody CreateProposalRequestDto request) throws NotFoundException, NotAuthenticatedException, BadRequestException, SpecialtyNotMatchWithServiceTypeException, InvalidProposalForThisOrderException {
        logger.info("Calling ProviderService to create proposal!");

        return providerService.createProposal(request);
    }

    @GetMapping("/in/available-orders/{providerId}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> getAllAvailableOrders(@PathVariable Integer providerId) throws NotAuthenticatedException, BadRequestException, NotFoundException, NoContentException {
        logger.info("Calling ProviderService to list all available orders");

        return providerService.getAllAvailableOrders(providerId);
    }

    private static final Logger logger = LogManager.getLogger(ProviderController.class.getName());
}