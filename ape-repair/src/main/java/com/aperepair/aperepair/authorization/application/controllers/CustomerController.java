package com.aperepair.aperepair.authorization.application.controllers;

import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.model.dto.CustomerDto;
import com.aperepair.aperepair.authorization.domain.model.dto.request.LoginDto;
import com.aperepair.aperepair.authorization.domain.model.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.ProfilePictureCreationResponseDto;
import com.aperepair.aperepair.authorization.domain.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    //TODO: Criar dto de request para criação de customer

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody @Valid Customer newCustomer) {
        return customerService.create(newCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable Integer id) {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid Customer updatedCustomer
    ) {
        return customerService.update(id, updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return customerService.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        return customerService.login(loginDto);
    }

    @DeleteMapping("/in/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestBody @Valid LoginDto loginDto) {
        return customerService.logout(loginDto);
    }

    @PutMapping("/profile-picture")
    public ResponseEntity<ProfilePictureCreationResponseDto> profilePictureCreation(
            @RequestBody @Valid ProfilePictureCreationRequestDto request
    ) throws IOException {
        ProfilePictureCreationResponseDto response = customerService.profilePictureCreation(request);
        return ResponseEntity.status(201).body(response);
    }
}
