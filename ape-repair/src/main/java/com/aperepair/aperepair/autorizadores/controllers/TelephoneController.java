package com.aperepair.aperepair.autorizadores.controllers;

import com.aperepair.aperepair.autorizadores.model.Telephone;
import com.aperepair.aperepair.autorizadores.service.impl.TelephoneServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/telephones")
public class TelephoneController {

    @Autowired
    private TelephoneServiceImpl telephoneServiceImpl;

    @PostMapping
    public ResponseEntity<Telephone> create(@RequestBody @Valid Telephone newTelephone) {
        return telephoneServiceImpl.create(newTelephone);
    }

    @GetMapping
    public ResponseEntity<List<Telephone>> findAll() {
        return telephoneServiceImpl.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telephone> findById(@PathVariable Integer id) {
        return telephoneServiceImpl.findById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Telephone> update(
            @PathVariable Integer id,
            @RequestBody @Valid Telephone updatedTelephone
    ) {
        return telephoneServiceImpl.update(id, updatedTelephone);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return telephoneServiceImpl.delete(id);
    }
}
