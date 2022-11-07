package com.aperepair.aperepair.authorization.application.controllers;

import com.aperepair.aperepair.authorization.domain.model.Telephone;
import com.aperepair.aperepair.authorization.domain.service.TelephoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/telephones")
public class TelephoneController {

    @Autowired
    private TelephoneService telephoneService;

    @PostMapping
    public ResponseEntity<Telephone> create(@RequestBody @Valid Telephone newTelephone) {
        return telephoneService.create(newTelephone);
    }

    @GetMapping
    public ResponseEntity<List<Telephone>> findAll() {
        return telephoneService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telephone> findById(@PathVariable Integer id) {
        return telephoneService.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Telephone> update(
            @PathVariable Integer id,
            @RequestBody @Valid Telephone updatedTelephone
    ) {
        return telephoneService.update(id, updatedTelephone);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return telephoneService.delete(id);
    }
}
