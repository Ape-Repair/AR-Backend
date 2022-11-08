package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.domain.dto.response.ProviderResponseDto;
import com.aperepair.aperepair.authorization.domain.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.dto.response.LogoutResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProviderService {

    ResponseEntity<ProviderResponseDto> create(@RequestBody Provider provider);

    ResponseEntity<List<ProviderResponseDto>> findAll();

    ResponseEntity<ProviderResponseDto> findById(Integer id);

    ResponseEntity<ProviderResponseDto> update(Integer id, Provider updatedProvider);

    ResponseEntity<Boolean> delete(Integer id);

    ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto);

    ResponseEntity<LogoutResponseDto> logout(@RequestBody LoginRequestDto loginRequestDto);
}
