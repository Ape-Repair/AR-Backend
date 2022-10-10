package com.aperepair.aperepair.authorization.service.impl;

import com.aperepair.aperepair.authorization.repository.SpecialtyRepository;
import com.aperepair.aperepair.authorization.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtyRepository;
}
