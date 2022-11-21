package com.aperepair.aperepair.application.controller;

import com.aperepair.aperepair.application.dto.request.CreateOrderRequestDto;
import com.aperepair.aperepair.application.dto.request.CredentialsRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerRequestDto;
import com.aperepair.aperepair.application.dto.request.CustomerUpdateRequestDto;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.domain.exception.*;
import com.aperepair.aperepair.domain.service.CustomerService;
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
@RequestMapping("/customers")
public class CustomerController {
//TODO: Atualizar repo de collections
    @Autowired
    private CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDto create(@Valid @RequestBody CustomerRequestDto newCustomer) throws AlreadyRegisteredException, BadRequestException {
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
            @RequestBody @Valid CustomerUpdateRequestDto updatedCustomer
    ) throws NotFoundException, NotAuthenticatedException {
        logger.info("Calling CustomerService to update customer!");

        return customerService.update(id, updatedCustomer);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteResponseDto delete(@PathVariable Integer id) throws NotFoundException {
        logger.info("Calling CustomerService to delete a customer");

        return customerService.delete(id);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid CredentialsRequestDto credentialsRequestDto) throws NotFoundException, InvalidRoleException, BadCredentialsException {
        logger.info("Calling CustomerService to authenticate a customer");

        return customerService.login(credentialsRequestDto);
    }

    @PatchMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public LogoutResponseDto logout(@RequestBody @Valid CredentialsRequestDto credentialsRequestDto) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, BadCredentialsException {
        logger.info("Calling CustomerService to logout a customer");

        return customerService.logout(credentialsRequestDto);
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

    @PostMapping("in/create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public void createOrder(@Valid @RequestBody CreateOrderRequestDto request) throws NotAuthenticatedException, NotFoundException, InvalidRoleException, InvalidServiceTypeException {
        logger.info("Calling CustomerService to create order");

        customerService.createOrder(request);
    }

    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> allOrders(@PathVariable Integer id) {
        logger.info("Calling CustomerService to get all orders of customer");

        return customerService.getAllOrders(id);
    }
//TODO(2): Criar controller (MatchController), e endpoint para trazer todas proposal de uma order
    private static final Logger logger = LogManager.getLogger(CustomerController.class.getName());
}