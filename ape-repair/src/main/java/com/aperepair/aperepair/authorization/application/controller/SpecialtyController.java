package com.aperepair.aperepair.authorization.application.controller;

import com.aperepair.aperepair.authorization.domain.model.Specialty;
import com.aperepair.aperepair.authorization.domain.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;


    @PostMapping
    public ResponseEntity<Specialty> create(@RequestBody @Valid Specialty newSpecialty) {
        return specialtyService.create(newSpecialty);
    }

    @GetMapping
    public ResponseEntity<List<Specialty>> findAll() {
        return specialtyService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> findById(@PathVariable Integer id) {
        return specialtyService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Specialty> update(
            @PathVariable Integer id,
            @RequestBody @Valid Specialty updatedSpecialty
    ) {
        return specialtyService.update(id, updatedSpecialty);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return specialtyService.delete(id);
    }
}
