package com.aperepair.aperepair.reports.application.controller;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.repository.ProviderRepository;
import com.aperepair.aperepair.reports.domain.CsvFile;
import com.aperepair.aperepair.reports.domain.entity.Admin;
import com.aperepair.aperepair.reports.domain.entity.dto.request.AdminLoginRequestDto;
import com.aperepair.aperepair.reports.domain.entity.dto.response.AdminLoginResponseDto;
import com.aperepair.aperepair.reports.domain.entity.dto.response.AdminResponseDto;
import com.aperepair.aperepair.reports.domain.service.impl.AdminServiceImpl;
import com.aperepair.aperepair.reports.domain.service.impl.ListObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @PostMapping
    public ResponseEntity<AdminResponseDto> create(@RequestBody @Valid Admin admin) {
        return adminServiceImpl.create(admin);
    }

    @GetMapping("/{username}")
    public ResponseEntity<AdminResponseDto> findByUsername(@PathVariable String username) {
        return adminServiceImpl.findByUsername(username);
    }

    @PutMapping
    public ResponseEntity<AdminResponseDto> update(@RequestBody @Valid Admin adminUpdated) {
        return adminServiceImpl.update(adminUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return adminServiceImpl.deleteById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(
            @RequestBody @Valid AdminLoginRequestDto adminLoginRequestDto
    ) {
        return adminServiceImpl.login(adminLoginRequestDto);
    }

    @GetMapping("/write-csv-file")
    public ResponseEntity<Void> generateCsvFile() {
       return adminServiceImpl.generateCsvFile();
    }
}
