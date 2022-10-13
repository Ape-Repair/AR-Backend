package com.aperepair.aperepair.authorization.controllers;

import com.aperepair.aperepair.authorization.model.Provider;
import com.aperepair.aperepair.authorization.model.Specialty;
import com.aperepair.aperepair.authorization.model.enums.dto.ProviderDto;
import com.aperepair.aperepair.authorization.service.impl.SpecialtyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    @Autowired
    private SpecialtyServiceImpl specialtyServiceImpl;


    @PostMapping
    public ResponseEntity<Specialty> create(@RequestBody @Valid Specialty newSpecialty) {
        return specialtyServiceImpl.create(newSpecialty);
    }

    @GetMapping
    public ResponseEntity<List<Specialty>> findAll() {
        return specialtyServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> findById(@PathVariable Integer id) {
        return specialtyServiceImpl.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Specialty> update(
            @PathVariable Integer id,
            @RequestBody @Valid Specialty updatedSpecialty
    ) {
        return specialtyServiceImpl.update(id, updatedSpecialty);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return specialtyServiceImpl.delete(id);
    }
}
