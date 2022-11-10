package com.aperepair.aperepair.authorization.application.controllers;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.application.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.authorization.application.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.application.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.domain.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    //TODO: após finalizar parte de customers, refatorar toda parte de providers;

    @Autowired
    private ProviderService providerService;

    @PostMapping
    public ResponseEntity<ProviderResponseDto> create(@RequestBody @Valid Provider newProvider) {
        return providerService.create(newProvider);
    }

    @GetMapping
    public ResponseEntity<List<ProviderResponseDto>> findAll() {
        return providerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponseDto> findById(@PathVariable Integer id) {
        return providerService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponseDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid Provider updatedProvider
    ) {
        return providerService.update(id, updatedProvider);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return providerService.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return providerService.login(loginRequestDto);
    }

    @DeleteMapping("/in/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return providerService.logout(loginRequestDto);
    }
}
