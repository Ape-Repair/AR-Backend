package com.aperepair.aperepair.authorization.service;

import com.aperepair.aperepair.authorization.model.Specialty;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface SpecialtyService {

    public ResponseEntity<Specialty> create(Specialty specialty);
    public ResponseEntity<List<Specialty>> findAll();

    public ResponseEntity<Specialty> findById(Integer id);

    public ResponseEntity<Specialty> update(Integer id, Specialty updatedSpecialty);

    public ResponseEntity<Boolean> delete(Integer id);
}
