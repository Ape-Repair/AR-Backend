package com.aperepair.aperepair.authorization.service;

import com.aperepair.aperepair.authorization.model.dto.request.LoginRequestDto;
import com.aperepair.aperepair.authorization.model.dto.response.LoginResponseDto;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    ResponseEntity<LoginResponseDto> logon(LoginRequestDto loginDto);
}
