package com.aperepair.aperepair.authorization.service.impl;

import com.aperepair.aperepair.authorization.model.Customer;
import com.aperepair.aperepair.authorization.model.Provider;
import com.aperepair.aperepair.authorization.model.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.model.enums.Role;
import com.aperepair.aperepair.authorization.repository.CustomerRepository;
import com.aperepair.aperepair.authorization.repository.ProviderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProviderRepository providerRepository;

    private LoginResponseDto loginResponseDto;

    public ResponseEntity<LoginResponseDto> logon(LoginRequestDto loginDto) {
        String emailAttempt = loginDto.getEmail();
        String passwordAttempt = loginDto.getPassword();

        try {
            logger.info("Starting login validation");
            loginResponseDto.setRole(Role.CUSTOMER);

            Customer customer = customerRepository.findByEmail(emailAttempt);

            if (customer.getRole().equals(loginResponseDto.getRole())) {
                //TODO - metodo que compara senhas criptogradas (customer)
            }
        } catch (Exception ex) {
            loginResponseDto.setRole(Role.PROVIDER);
            Provider provider = providerRepository.findByEmail(emailAttempt);

            if (provider.getRole().equals(loginResponseDto.getRole())) {
                //TODO - metodo que compara senhas criptogradas (provider)
            }
        } /*catch (Exception ex) {
            logger.warn("Email and/or password invalid");
            ex.printStackTrace();
        } */

        return null;
    }

    private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class.getName());
}