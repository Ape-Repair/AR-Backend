package com.aperepair.aperepair.authorization.application.controller;

import com.aperepair.aperepair.authorization.application.dto.request.ProviderRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.DeleteResponseDto;
import com.aperepair.aperepair.authorization.domain.exception.*;
import com.aperepair.aperepair.authorization.application.dto.request.CredentialsRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.authorization.application.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.application.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.domain.service.ProviderService;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.ProfilePictureCreationResponseDto;
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

    //TODO: Resolver questão do id já que não retorna no front
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProviderResponseDto update(
            @PathVariable Integer id,
            @RequestBody @Valid ProviderRequestDto updatedProvider
    ) throws NotAuthenticatedException, NotFoundException {
        logger.info("Calling ProviderService to update provider!");

        return providerService.update(id, updatedProvider);
    }

    //TODO: Fazer dto de request de delete (provider)
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
    public GetProfilePictureResponseDto getProfilePicture(
            @RequestBody @Valid GetProfilePictureRequestDto request
    ) throws Exception {
        logger.info("Calling ProviderService to get customer profile image!");

        return providerService.getProfilePicture(request);
    }

    private static final Logger logger = LogManager.getLogger(ProviderController.class.getName());
}