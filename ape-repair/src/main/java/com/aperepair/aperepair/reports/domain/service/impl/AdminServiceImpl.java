package com.aperepair.aperepair.reports.domain.service.impl;

import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.model.dto.CustomerDto;
import com.aperepair.aperepair.authorization.domain.model.dto.factory.CustomerDtoFactory;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.model.enums.Role;
import com.aperepair.aperepair.authorization.domain.repository.ProviderRepository;
import com.aperepair.aperepair.reports.domain.CsvFile;
import com.aperepair.aperepair.reports.domain.entity.Admin;
import com.aperepair.aperepair.reports.domain.entity.dto.request.AdminLoginRequestDto;
import com.aperepair.aperepair.reports.domain.entity.dto.response.AdminLoginResponseDto;
import com.aperepair.aperepair.reports.domain.entity.dto.response.AdminResponseDto;
import com.aperepair.aperepair.reports.domain.repository.AdminRepository;
import com.aperepair.aperepair.reports.domain.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<AdminResponseDto> create(Admin admin) {
        admin.setRole(Role.ADMIN);

        admin.setPassword(encoder.encode(admin.getPassword()));
        logger.info("Admin password encrypted with successfully");

        adminRepository.save(admin);

        AdminResponseDto adminResponseDto = AdminResponseDto.toDto(admin);
        logger.info(String.format("AdminResponseDto: %s registered successfully", adminResponseDto.toString()));


        return ResponseEntity.status(201).body(adminResponseDto);
    }

    @Override
    public ResponseEntity<AdminResponseDto> findByUsername(String username) {
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(username);

        if (optionalAdmin.isEmpty()) {
            logger.info(String.format("Admin of username %s not found!", username));
            return ResponseEntity.status(404).build();
        }
        logger.info(String.format("Admin of username %s found!", username));
        Admin admin = optionalAdmin.get();

        AdminResponseDto adminResponseDto = new AdminResponseDto(admin.getUsername(), admin.getRole());

        return ResponseEntity.status(200).body(adminResponseDto);
    }

    @Override
    public ResponseEntity<AdminResponseDto> update(Admin adminUpdated) {
        if (adminRepository.existsById(adminUpdated.getId())) {

            Admin admin = adminRepository.findById(adminUpdated.getId()).get();

            logger.info(String.format("Customer of id %d found", admin.getId()));

            admin.setUsername(adminUpdated.getUsername());
            admin.setPassword(encoder.encode(adminUpdated.getPassword()));

            adminRepository.save(admin);
            logger.info(String.format("Updated admin of id: %d registration data!", admin.getId()));

            AdminResponseDto adminResponseDto = AdminResponseDto.toDto(admin);

            return ResponseEntity.status(200).body(adminResponseDto);
        }

        logger.error(String.format("Admin of id %d not found", adminUpdated.getId()));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);

        if (optionalAdmin.isEmpty()) {
            logger.info(String.format("Admin of id %d not found!", id));
            return ResponseEntity.status(404).build();
        }

        Admin admin = optionalAdmin.get();
        logger.info(String.format("Admin of id %d found!", id));

        admin.setRole(Role.DELETED);
        adminRepository.save(admin);

        return ResponseEntity.status(200).build();
    }

    @Override
    public ResponseEntity<AdminLoginResponseDto> login(AdminLoginRequestDto adminLoginRequestDto) {
        String usernameAttempt = adminLoginRequestDto.getUsername();
        String passwordAttempt = adminLoginRequestDto.getPassword();

        AdminLoginResponseDto adminLoginResponseDto = new AdminLoginResponseDto(usernameAttempt, false);

        logger.info(String.format("Searching for admin by username: [%s]", usernameAttempt));
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(usernameAttempt);

        if (optionalAdmin.isEmpty()) {
            logger.warn(String.format("Username admin: [%s] - Not Found!", usernameAttempt));
            return ResponseEntity.status(404).body(adminLoginResponseDto);
        }

        Admin admin = optionalAdmin.get();
        logger.info(String.format("Trying to login with username: [%s] - as a admin", usernameAttempt));

        boolean valid = isValidPassword(passwordAttempt, admin);

        if (valid) {
            adminLoginResponseDto.setSuccess(true);
        } else {
            if (!valid) logger.info("Password invalid!");

            if (admin.getRole() != Role.ADMIN) {
                logger.fatal(String.format("[%S] - Incorrect role for this flow", admin.getRole()));
                return ResponseEntity.status(403).build();
            }

            return ResponseEntity.status(401).body(adminLoginResponseDto);
        }

        logger.info("Login successfully executed!");
        return ResponseEntity.status(200).body(adminLoginResponseDto);
    }

    public ResponseEntity<Void> generateCsvFile() {
        List<Provider> providers = providerRepository.findAll();
        if (providers.isEmpty()) return ResponseEntity.noContent().build();

        int capacity = providers.size();

        ListObj<Provider> providersListObj = new ListObj(capacity);

        for (Provider provider : providers) {
            providersListObj.adiciona(provider);
        }

        CsvFile.writeCsvFile(providersListObj);
        return ResponseEntity.status(201).build();
    }

    private boolean isValidPassword(String passwordAttempt, Admin admin) {
        if (encoder.matches(passwordAttempt, admin.getPassword())) return true;

        return false;
    }

    private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class.getName());
}