package com.aperepair.aperepair.authorization.controllers;

import com.aperepair.aperepair.authorization.model.Provider;
import com.aperepair.aperepair.authorization.model.enums.dto.ProviderDto;
import com.aperepair.aperepair.authorization.service.impl.ProviderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private ProviderServiceImpl providerServiceImpl;

    @PostMapping
    public ResponseEntity<ProviderDto> create(@RequestBody @Valid Provider newProvider) {
        return providerServiceImpl.create(newProvider);
    }

    @GetMapping
    public ResponseEntity<List<Provider>> findAll() {
        return providerServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Provider> findById(@PathVariable Integer id) {
        return providerServiceImpl.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Provider> update(
            @PathVariable Integer id,
            @RequestBody @Valid Provider updatedProvider
    ) {
        return providerServiceImpl.update(id, updatedProvider);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return providerServiceImpl.delete(id);
    }

}
