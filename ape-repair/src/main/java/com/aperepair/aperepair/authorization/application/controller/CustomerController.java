package com.aperepair.aperepair.authorization.application.controller;

import com.aperepair.aperepair.authorization.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.*;
import com.aperepair.aperepair.authorization.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.service.CustomerService;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.authorization.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable Integer id) {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid CustomerRequestDto updatedCustomer
    ) {
        return customerService.update(id, updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return customerService.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return customerService.login(loginRequestDto);
    }

    @DeleteMapping("/in/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return customerService.logout(loginRequestDto);
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<ProfilePictureCreationResponseDto> profilePictureCreation(
            @RequestBody @Valid ProfilePictureCreationRequestDto request
    ) throws IOException {
        ProfilePictureCreationResponseDto response = customerService.profilePictureCreation(request);

        if (response.isSuccess()) return ResponseEntity.status(201).body(response);

        return ResponseEntity.status(404).body(response);
    }

    @PostMapping("/profile-picture")
    public ResponseEntity<GetProfilePictureResponseDto> getProfilePicture(
            @RequestBody @Valid GetProfilePictureRequestDto request) throws IOException {
        GetProfilePictureResponseDto response = customerService.getProfilePicture(request);
        return ResponseEntity.status(200).body(response);
    }
}
