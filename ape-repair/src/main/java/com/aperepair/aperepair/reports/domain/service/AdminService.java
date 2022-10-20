package com.aperepair.aperepair.reports.domain.service;

import com.aperepair.aperepair.reports.domain.model.Admin;
import com.aperepair.aperepair.reports.domain.model.dto.request.AdminLoginRequestDto;
import com.aperepair.aperepair.reports.domain.model.dto.response.AdminLoginResponseDto;
import com.aperepair.aperepair.reports.domain.model.dto.response.AdminResponseDto;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<AdminResponseDto> create(Admin admin);

    ResponseEntity<AdminResponseDto> findByUsername(String username);

    ResponseEntity<AdminResponseDto> update(Admin adminUpdated);

    ResponseEntity<Void> delete(Integer id);

    ResponseEntity<AdminLoginResponseDto> login(AdminLoginRequestDto adminLoginRequestDto);

    ResponseEntity<Void> generateCsvFile();
}
