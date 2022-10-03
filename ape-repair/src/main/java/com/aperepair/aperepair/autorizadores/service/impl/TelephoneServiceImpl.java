package com.aperepair.aperepair.autorizadores.service.impl;

import com.aperepair.aperepair.autorizadores.repository.TelephoneRepository;
import com.aperepair.aperepair.autorizadores.service.TelephoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelephoneServiceImpl implements TelephoneService {

    @Autowired
    private TelephoneRepository telephoneRepository;

    //TODO (Address) - definir lógica e contrato dos endpoints da controller

}
