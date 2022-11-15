package com.aperepair.aperepair.authorization.application.controller;

import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.application.dto.request.DeleteRequestDto;
import com.aperepair.aperepair.authorization.domain.exception.*;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.*;
import com.aperepair.aperepair.authorization.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.service.CustomerService;
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
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDto create(@Valid @RequestBody CustomerRequestDto newCustomer) throws AlreadyRegisteredException {
        logger.info("Calling CustomerService to create a customer!");

        return customerService.create(newCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable Integer id) throws NotFoundException {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponseDto update(
            @PathVariable Integer id,
            @RequestBody @Valid CustomerRequestDto updatedCustomer
    ) throws NotFoundException, NotAuthenticatedException {
        logger.info("Calling CustomerService to update customer!");

        return customerService.update(id, updatedCustomer);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public DeleteResponseDto delete(@RequestBody @Valid DeleteRequestDto request) throws NotFoundException {
        logger.info("Calling CustomerService to delete a customer");

        return customerService.delete(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException {
        logger.info("Calling CustomerService to authenticate a customer");

        return customerService.login(loginRequestDto);
    }

    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public LogoutResponseDto logout(@RequestBody @Valid LoginRequestDto loginRequestDto) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, BadCredentialsException {
        logger.info("Calling CustomerService to logout a customer");

        return customerService.logout(loginRequestDto);
    }

    @PutMapping("/profile-picture")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfilePictureCreationResponseDto profilePictureCreation(
            @RequestBody @Valid ProfilePictureCreationRequestDto request
    ) throws IOException, AwsUploadException, NotFoundException {
        logger.info("Calling CustomerService to upload customer profile image!");

        return customerService.profilePictureCreation(request);
    }

    @PostMapping("/profile-picture")
    public GetProfilePictureResponseDto getProfilePicture(
            @RequestBody @Valid GetProfilePictureRequestDto request
    ) throws Exception {
        logger.info("Calling CustomerService to get customer profile image!");

        return customerService.getProfilePicture(request);
    }

    private static final Logger logger = LogManager.getLogger(CustomerController.class.getName());
}