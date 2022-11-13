package com.aperepair.aperepair.authorization.application.controller;

import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.domain.exception.AwsUploadException;
import com.aperepair.aperepair.authorization.domain.exception.CustomerNotFoundException;
import com.aperepair.aperepair.authorization.domain.service.impl.CustomerServiceImpl;
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
    public ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CustomerRequestDto newCustomer) {
        return customerService.create(newCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable Integer id) throws CustomerNotFoundException {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid CustomerRequestDto updatedCustomer
    ) throws CustomerNotFoundException {
        return customerService.update(id, updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) throws CustomerNotFoundException {
        return customerService.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) throws CustomerNotFoundException {
        return customerService.login(loginRequestDto);
    }

    @DeleteMapping("/in/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestBody @Valid LoginRequestDto loginRequestDto) throws CustomerNotFoundException {
        return customerService.logout(loginRequestDto);
    }

    @PutMapping("/profile-picture")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfilePictureCreationResponseDto profilePictureCreation(
            @RequestBody @Valid ProfilePictureCreationRequestDto request
    ) throws IOException, AwsUploadException, CustomerNotFoundException {
        logger.info("Calling CustomerService to upload customer profile image!");

        return customerService.profilePictureCreation(request);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<GetProfilePictureResponseDto> getProfilePicture(
            @RequestBody @Valid GetProfilePictureRequestDto request) throws Exception {
        GetProfilePictureResponseDto response = customerService.getProfilePicture(request);
        return ResponseEntity.status(200).body(response);
    }

    private static final Logger logger = LogManager.getLogger(CustomerController.class.getName());
}
