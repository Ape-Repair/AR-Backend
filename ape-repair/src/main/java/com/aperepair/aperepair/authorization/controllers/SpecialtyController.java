package com.aperepair.aperepair.authorization.controllers;

import com.aperepair.aperepair.authorization.service.impl.SpecialtyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    @Autowired
    private SpecialtyServiceImpl specialtyServiceImpl;
}
