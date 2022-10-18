package com.aperepair.aperepair.authorization.application.controllers;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.model.dto.LoginDto;
import com.aperepair.aperepair.authorization.domain.model.dto.ProviderDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LogoutResponseDto;
import com.aperepair.aperepair.authorization.domain.service.impl.ProviderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private ProviderServiceImpl providerServiceImpl;

    @PostMapping
    public ResponseEntity<ProviderDto> create(@RequestBody @Valid Provider newProvider) {
        return providerServiceImpl.create(newProvider);
    }

    @GetMapping
    public ResponseEntity<List<ProviderDto>> findAll() {
        return providerServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderDto> findById(@PathVariable Integer id) {
        return providerServiceImpl.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid Provider updatedProvider
    ) {
        return providerServiceImpl.update(id, updatedProvider);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return providerServiceImpl.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        return providerServiceImpl.login(loginDto);
    }

    @DeleteMapping("/in/logout")
    public ResponseEntity<LogoutResponseDto> logout(@RequestBody @Valid LoginDto loginDto) {
        return providerServiceImpl.logout(loginDto);
    }
}
