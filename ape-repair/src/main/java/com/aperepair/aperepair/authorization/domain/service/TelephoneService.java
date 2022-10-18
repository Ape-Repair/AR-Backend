package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.domain.model.Telephone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TelephoneService {

    public ResponseEntity<Telephone> create(@RequestBody Telephone telephone);

    public ResponseEntity<List<Telephone>> findAll();

    public ResponseEntity<Telephone> findById(Integer id);

    public ResponseEntity<Telephone> update(Integer id, Telephone updatedTelephone);

    public ResponseEntity<Boolean> delete(Integer id);

}
