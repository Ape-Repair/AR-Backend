package com.aperepair.aperepair.reports.domain.service;

import com.aperepair.aperepair.reports.domain.entity.Admin;
import com.aperepair.aperepair.reports.domain.entity.dto.response.AdminResponseDto;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<AdminResponseDto> create(Admin admin);

    ResponseEntity<AdminResponseDto> findByUsername(String username);

    ResponseEntity<AdminResponseDto> update(Admin adminUpdated);

    ResponseEntity<Void> delete(Integer id);

    ResponseEntity<AdminLoginResponseDto> login(AdminLoginRequestDto adminLoginRequestDto);

    ResponseEntity<Void> generateCsvFile();
}
