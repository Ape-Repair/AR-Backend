package com.aperepair.aperepair.autorizadores.controllers;

import com.aperepair.aperepair.autorizadores.model.Customer;
import com.aperepair.aperepair.autorizadores.model.dto.SaveCustomerDto;
import com.aperepair.aperepair.autorizadores.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl clienteServiceImpl;

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody @Valid Customer newCliente) {
        return clienteServiceImpl.create(newCliente);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        return clienteServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> findById(@PathVariable Integer id) {
        return clienteServiceImpl.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Customer> update(
            @PathVariable Integer id,
            @RequestBody @Valid Customer updatedCustomer
    ) {
        return clienteServiceImpl.update(id, updatedCustomer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return clienteServiceImpl.delete(id);
    }

}
