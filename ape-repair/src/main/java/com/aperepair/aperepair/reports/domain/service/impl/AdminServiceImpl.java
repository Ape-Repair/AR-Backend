package com.aperepair.aperepair.reports.domain.service.impl;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.model.enums.Role;
import com.aperepair.aperepair.authorization.domain.repository.ProviderRepository;
import com.aperepair.aperepair.reports.domain.CsvFile;
import com.aperepair.aperepair.reports.domain.entity.Admin;
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

    //TODO - Will be implements other methods os contract

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

    private static final Logger logger = LogManager.getLogger(AdminServiceImpl.class.getName());
}
