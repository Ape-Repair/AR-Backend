package com.aperepair.aperepair.authorization.controllers;

import com.aperepair.aperepair.authorization.model.dto.LoginDto;
import com.aperepair.aperepair.authorization.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginServiceImpl loginServiceImpl;

    public ResponseEntity<Boolean> logon(@RequestBody @Valid LoginDto loginAttempt) {
        return loginServiceImpl.logon(loginAttempt);
    }
}
