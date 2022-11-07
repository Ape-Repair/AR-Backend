package com.aperepair.aperepair.authorization.application.controllers;

import com.aperepair.aperepair.authorization.domain.model.Address;
import com.aperepair.aperepair.authorization.domain.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> create(@RequestBody @Valid Address newAddress) {
        return addressService.create(newAddress);
    }
    @GetMapping
    public ResponseEntity<List<Address>> findAll() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> findById(@PathVariable Integer id) {
        return addressService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Address> update(
            @PathVariable Integer id,
            @RequestBody @Valid Address updatedAddress
    ) {
        return addressService.update(id, updatedAddress);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return addressService.delete(id);
    }
}
