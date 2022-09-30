package com.aperepair.aperepair.autorizadores.controllers;

import com.aperepair.aperepair.autorizadores.model.Customer;
import com.aperepair.aperepair.autorizadores.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl clienteServiceImpl;

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody @Valid Customer novoCliente) {
        return clienteServiceImpl.create(novoCliente);
    }


}
