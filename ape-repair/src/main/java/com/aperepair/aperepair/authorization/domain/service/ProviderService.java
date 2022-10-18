package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.authorization.domain.model.dto.LoginDto;
import com.aperepair.aperepair.authorization.domain.model.dto.ProviderDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LoginResponseDto;
import com.aperepair.aperepair.authorization.domain.model.dto.response.LogoutResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProviderService {

    ResponseEntity<ProviderDto> create(@RequestBody Provider provider);

    ResponseEntity<List<ProviderDto>> findAll();

    ResponseEntity<ProviderDto> findById(Integer id);

    ResponseEntity<ProviderDto> update(Integer id, Provider updatedProvider);

    ResponseEntity<Boolean> delete(Integer id);

    ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto);

    ResponseEntity<LogoutResponseDto> logout(@RequestBody LoginDto loginDto);
}
