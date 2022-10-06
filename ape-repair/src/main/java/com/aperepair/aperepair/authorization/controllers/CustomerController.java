package com.aperepair.aperepair.authorization.controllers;

import com.aperepair.aperepair.authorization.model.Customer;
import com.aperepair.aperepair.authorization.model.dto.CustomerDto;
import com.aperepair.aperepair.authorization.service.impl.CustomerServiceImpl;
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
    public ResponseEntity<CustomerDto> create(@RequestBody @Valid Customer newCustomer) {
        return clienteServiceImpl.create(newCustomer);
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
