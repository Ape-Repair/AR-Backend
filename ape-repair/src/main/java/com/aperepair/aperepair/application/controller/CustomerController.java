package com.aperepair.aperepair.application.controller;

import com.amazonaws.util.IOUtils;
import com.aperepair.aperepair.application.dto.request.*;
import com.aperepair.aperepair.application.dto.response.*;
import com.aperepair.aperepair.application.exception.*;
import com.aperepair.aperepair.domain.service.CustomerService;
import com.aperepair.aperepair.report.resources.QueueObj;
import com.aperepair.aperepair.resources.aws.dto.request.GetProfilePictureRequestDto;
import com.aperepair.aperepair.resources.aws.dto.request.ProfilePictureCreationRequestDto;
import com.aperepair.aperepair.resources.aws.dto.response.GetProfilePictureResponseDto;
import com.aperepair.aperepair.resources.aws.dto.response.ProfilePictureCreationResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDto create(@Valid @RequestBody CustomerRequestDto newCustomer) throws AlreadyRegisteredException, BadRequestException {
        logger.info("Calling service to create a customer!");

        return customerService.create(newCustomer);
    }

    @GetMapping
    public ResponseEntity<QueueObj<CustomerResponseDto>> findAll() {
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
        logger.info(String.format("Calling service to update customer with id [%d]", id));

        return customerService.update(id, updatedCustomer);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteResponseDto delete(@PathVariable Integer id) throws NotFoundException {
        logger.info(String.format("Calling service to delete a customer with id [%d]", id));

        return customerService.delete(id);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) throws
            NotFoundException, InvalidRoleException, BadCredentialsException {
        logger.info(String.format("Calling service to authenticate a customer with email [%s]", loginRequestDto.getEmail()));

        return customerService.login(loginRequestDto);
    }

    @PatchMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public LogoutResponseDto logout(@RequestBody @Valid LogoutRequestDto logoutRequestDto) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, BadCredentialsException {
        logger.info(String.format("Calling service to logout a customer with email [%s]", logoutRequestDto.getEmail()));

        return customerService.logout(logoutRequestDto);
    }

    @PutMapping("/profile-picture")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfilePictureCreationResponseDto profilePictureCreation(
            @RequestBody @Valid ProfilePictureCreationRequestDto request
    ) throws IOException, AwsUploadException, NotFoundException {
        logger.info(String.format("Calling service to upload customer profile image for customer with email: [%s]", request.getEmail()));

        return customerService.profilePictureCreation(request);
    }

    @PostMapping("/profile-picture")
    public GetProfilePictureResponseDto getProfilePicture(
            @RequestBody @Valid GetProfilePictureRequestDto request
    ) throws Exception {
        logger.info(String.format("Calling service to get customer profile image for customer with email: [%s]", request.getEmail()));

        return customerService.getProfilePicture(request);
    }

    @PostMapping("in/create-order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderUlidResponseDto createOrder(@Valid @RequestBody CreateOrderRequestDto request) throws NotAuthenticatedException, NotFoundException, InvalidRoleException, InvalidServiceTypeException, StatusInvalidToCreateOrder, NoContentException {
        logger.info(String.format("Calling service to create order of customer with id: [%d]", request.getCustomerId()));

        return customerService.createOrder(request);
    }

    @GetMapping("/{customerId}/orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponseDto> allOrders(@PathVariable Integer customerId) throws NotFoundException, NotAuthenticatedException, InvalidRoleException, NoContentException {
        logger.info(String.format("Calling service to get all orders of customer: [%d]", customerId));

        return customerService.getAllOrders(customerId);
    }

    @GetMapping("/order/{orderId}/available-proposals")
    @ResponseStatus(HttpStatus.OK)
    public List<ProposalResponseDto> getProposalsForOrder(@PathVariable String orderId) throws NotFoundException, NoContentException {
        logger.info(String.format("Calling MatchService to get proposals for an order with id: [%s]", orderId));

        return customerService.getProposalsForOrder(orderId);
    }

    @PostMapping("/order/{orderId}/accept-proposal/{proposalId}")
    @ResponseStatus(HttpStatus.OK)
    public void acceptProposal(@PathVariable String orderId, @PathVariable Integer proposalId) throws InvalidProposalForThisOrderException, NotFoundException {
        logger.info(String.format("Calling service to accept proposal with id: [%s] for an order with id [%d]", orderId, proposalId));

        customerService.acceptProposal(orderId, proposalId);
    }

    @PostMapping("/order/{orderId}/payment")
    @ResponseStatus(HttpStatus.OK)
    public void makePayment(@PathVariable String orderId) throws NotFoundException, InvalidOrderForPaymentException, NotAuthenticatedException, InvalidRoleException, AwsServiceInternalException, IOException {
        logger.info(String.format("Calling service to make payment for an order with id: [%s]", orderId));

        customerService.makePayment(orderId);
    }

    @PutMapping("/order/{orderId}/conclude")
    @ResponseStatus(HttpStatus.OK)
    public void concludeOrder(@PathVariable String orderId) throws NotFoundException, InvalidOrderToConcludeException, NotAuthenticatedException, InvalidRoleException {
        logger.info(String.format("Calling service to conclude an order with id: [%s]", orderId));

        customerService.concludeOrder(orderId);
    }

    @PutMapping("/order/{orderId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@PathVariable String orderId) throws NotFoundException, InvalidOrderToCanceledException {
        logger.info(String.format("Calling service to cancel an order with id: [%s]", orderId));

        customerService.cancelOrder(orderId);
    }

    @GetMapping(
            value = "/order/{orderId}/receipt",
            produces = "text/plain.openxmlformats-officedocument.spreadsheetml.sheet"
    )
    public ResponseEntity<byte[]> getReceipt(@PathVariable String orderId) throws IOException, NotAuthenticatedException, InvalidOrderStatusException, NotFoundException, InvalidRoleException, AwsServiceInternalException, AwsS3ImageException {
        logger.info(String.format("Calling service to get a receipt for order: [%s]", orderId));

        String fileName = String.format("receipt-%s", orderId);
        fileName += ".txt";

        InputStream receiptInputStream = customerService.getReceipt(orderId);

        byte[] receiptByteArray = IOUtils.toByteArray(receiptInputStream);

        return ResponseEntity.status(200)
                .header(
                        "content-disposition",
                        "attachment; " + "filename="+ fileName
                ).body(receiptByteArray);
    }

    private static final Logger logger = LogManager.getLogger(CustomerController.class.getName());
}