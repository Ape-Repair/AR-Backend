package com.aperepair.aperepair.authorization.application.controllers;

import com.aperepair.aperepair.authorization.domain.model.Address;
import com.aperepair.aperepair.authorization.domain.service.impl.AddressServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressServiceImpl addressServiceImpl;

    @PostMapping
    public ResponseEntity<Address> create(@RequestBody @Valid Address newAddress) {
        return addressServiceImpl.create(newAddress);
    }
    @GetMapping
    public ResponseEntity<List<Address>> findAll() {
        return addressServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> findById(@PathVariable Integer id) {
        return addressServiceImpl.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Address> update(
            @PathVariable Integer id,
            @RequestBody @Valid Address updatedAddress
    ) {
        return addressServiceImpl.update(id, updatedAddress);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return addressServiceImpl.delete(id);
    }
}
