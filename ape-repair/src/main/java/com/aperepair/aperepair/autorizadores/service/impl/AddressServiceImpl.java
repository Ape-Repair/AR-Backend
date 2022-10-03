package com.aperepair.aperepair.autorizadores.service.impl;

import com.aperepair.aperepair.autorizadores.repository.AddressRepository;
import com.aperepair.aperepair.autorizadores.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    //TODO (Address) - definir lógica e contrato dos endpoints da controller

}
