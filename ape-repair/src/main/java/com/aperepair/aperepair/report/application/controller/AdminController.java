package com.aperepair.aperepair.report.application.controller;

import com.aperepair.aperepair.domain.model.Dashboard;
import com.aperepair.aperepair.domain.service.impl.DashboardServiceImpl;
import com.aperepair.aperepair.report.domain.model.Admin;
import com.aperepair.aperepair.report.domain.model.dto.request.AdminLoginRequestDto;
import com.aperepair.aperepair.report.domain.model.dto.response.AdminLoginResponseDto;
import com.aperepair.aperepair.report.domain.model.dto.response.AdminResponseDto;
import com.aperepair.aperepair.report.domain.service.impl.AdminServiceImpl;
import com.aperepair.aperepair.report.domain.service.impl.ListObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @Autowired
    private DashboardServiceImpl dashboardService;

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
        return adminServiceImpl.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDto> login(
            @RequestBody @Valid AdminLoginRequestDto adminLoginRequestDto
    ) {
        return adminServiceImpl.login(adminLoginRequestDto);
    }

    @GetMapping("/dashboards")
    public Dashboard getAnalytics() {
        return dashboardService.allAnalytics();
    }

}
