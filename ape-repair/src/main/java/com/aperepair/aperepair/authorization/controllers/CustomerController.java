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
    private CustomerServiceImpl customerServiceImpl;

    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody @Valid Customer newCustomer) {
        return customerServiceImpl.create(newCustomer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        return customerServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> findById(@PathVariable Integer id) {
        return customerServiceImpl.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(
            @PathVariable Integer id,
            @RequestBody @Valid Customer updatedCustomer
    ) {
        return customerServiceImpl.update(id, updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return customerServiceImpl.delete(id);
    }
}
