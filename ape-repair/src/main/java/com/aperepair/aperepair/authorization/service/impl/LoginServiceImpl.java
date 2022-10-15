package com.aperepair.aperepair.authorization.service.impl;

import com.aperepair.aperepair.authorization.model.Customer;
import com.aperepair.aperepair.authorization.model.Provider;
import com.aperepair.aperepair.authorization.model.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.model.enums.Role;
import com.aperepair.aperepair.authorization.repository.CustomerRepository;
import com.aperepair.aperepair.authorization.repository.ProviderRepository;
import com.aperepair.aperepair.authorization.service.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PasswordEncoder encoder;

    private LoginResponseDto loginResponseDto;

    public ResponseEntity<LoginResponseDto> logon(LoginRequestDto loginRequestDto) {
        boolean valid;
        String emailAttempt = loginRequestDto.getEmail();
        String passwordAttempt = loginRequestDto.getPassword();

        logger.info(String.format("Trying to login with email: %s - as a customer", loginRequestDto.getEmail()));

        loginResponseDto.setRole(Role.CUSTOMER);
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(emailAttempt);

        if (optionalCustomer.isPresent() &&
                optionalCustomer.get().getRole()
                        .equals(loginResponseDto.getRole())
        ) {
            Customer customer = optionalCustomer.get();
            valid = encoder.matches(passwordAttempt, customer.getPassword());

            if (valid && !isAuthenticatedCustomer(customer)) {
                customer.setAuthenticated(true);
                loginResponseDto.setSuccess(true);

                logger.info(String.format("Login successfully for customer name: %s", customer.getName()));

                return ResponseEntity.status(200).body(loginResponseDto);
            }
        }

        logger.info(String.format("Trying to login with email: %s - as a provider", emailAttempt));

        loginResponseDto.setRole(Role.PROVIDER);
        Optional<Provider> optionalProvider = providerRepository.findByEmail(emailAttempt);

        if (optionalProvider.isPresent() &&
                optionalProvider.get().getRole().equals(loginResponseDto.getRole())) {
            Provider provider = optionalProvider.get();
            valid = encoder.matches(passwordAttempt, provider.getPassword());

            if (valid && !isAuthenticatedProvider(provider)) {
                provider.setAuthenticated(true);
                loginResponseDto.setSuccess(true);

                logger.info(String.format("Login successfully for provider name: %s", provider.getName()));

                return ResponseEntity.status(200).body(loginResponseDto);
            }
        }

        logger.info("Email and/or password invalid!\n\n" +
                "Login attempt failed!\n");

        loginResponseDto.setSuccess(false);
        loginResponseDto.setRole(Role.INVALID);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponseDto);
    }

    private Boolean isAuthenticatedCustomer(Customer customer) {
        if (customer.getAuthenticated() == true) {
            logger.info("Login attempt failed -- Customer was already authenticated");
            return true;
        }
        return false;
    }

    private Boolean isAuthenticatedProvider(Provider provider) {
        if (provider.getAuthenticated() == true) {
            logger.info("Login attempt failed -- Provider was already authenticated");
            return true;
        }

        return false;
    }

    private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class.getName());
}