package com.aperepair.aperepair.authorization.service.impl;

import com.aperepair.aperepair.authorization.model.Specialty;
import com.aperepair.aperepair.authorization.repository.SpecialtyRepository;
import com.aperepair.aperepair.authorization.service.SpecialtyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Override
    public ResponseEntity<Specialty> create(Specialty specialty) {
        specialtyRepository.save(specialty);
        logger.info(String.format("Specialty: %s registered successfully", specialty.toString()));

        return ResponseEntity.status(201).body(specialty);
    }

    @Override
    public ResponseEntity<List<Specialty>> findAll() {
        List<Specialty> specialties = specialtyRepository.findAll();
        logger.info("Starting a search for specialties");

        if (specialties.isEmpty()) {
            logger.info("Specialty list is empty");
            return ResponseEntity.status(204).build();
        }

        logger.info("List of specialties found");
        return ResponseEntity.status(200).body(specialties);
    }

    @Override
    public ResponseEntity<Specialty> findById(Integer id) {
        logger.info(String.format("Searching specialty for id: %d", id));
        if (specialtyRepository.existsById(id)) {
            Optional<Specialty> optionalSpecialty = specialtyRepository.findById(id);
            logger.info(String.format("Specialty of id %d found", id));

            Specialty specialty = optionalSpecialty.get();

            return ResponseEntity.status(200).body(specialty);
        }

        logger.error(String.format("Specialty of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Specialty> update(Integer id, Specialty updatedSpecialty) {
        if (
                specialtyRepository.existsById(id)
                        && specialtyRepository.findById(id).equals(updatedSpecialty.getId())
        ) {

            logger.info(String.format("Specialty of id %d found", id));

            specialtyRepository.save(updatedSpecialty);
            logger.info(String.format("Updated specialty: %s registration data!", updatedSpecialty.toString()));

            return ResponseEntity.status(200).body(updatedSpecialty);
        }

        logger.error(String.format("Specialty of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Boolean> delete(Integer id) {
            Boolean success = false;
            if (specialtyRepository.existsById(id)) {
                Specialty specialty = specialtyRepository.findById(id).get();
                logger.info(String.format("Specialty of id %d found", id));

                specialtyRepository.deleteById(id);
                logger.info(String.format("Specialty: %s successfully deleted", specialty.toString()));
                success = true;

                return ResponseEntity.status(200).body(success);
            }

            logger.error(String.format("Specialty of id %d not found", id));
            return ResponseEntity.status(404).body(success);
    }

    private static final Logger logger = LogManager.getLogger(SpecialtyServiceImpl.class.getName());
}
