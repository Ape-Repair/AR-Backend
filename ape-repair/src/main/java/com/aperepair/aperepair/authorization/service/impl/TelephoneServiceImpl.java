package com.aperepair.aperepair.authorization.service.impl;

import com.aperepair.aperepair.authorization.model.Telephone;
import com.aperepair.aperepair.authorization.repository.TelephoneRepository;
import com.aperepair.aperepair.authorization.service.TelephoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TelephoneServiceImpl implements TelephoneService {

    @Autowired
    private TelephoneRepository telephoneRepository;

    @Override
    public ResponseEntity<Telephone> create(Telephone telephone) {
        telephoneRepository.save(telephone);

        logger.info(String.format("Telephone: %s registered successfully", telephone.toString()));

        return ResponseEntity.status(201).body(telephone);
    }

    @Override
    public ResponseEntity<List<Telephone>> findAll() {
        List<Telephone> telephones = new ArrayList(telephoneRepository.findAll());

        if (telephones.isEmpty()) {
            logger.info("There are no registered telephones");
            return ResponseEntity.status(204).build();
        }

        logger.info("Success in finding registered telephones");
        return ResponseEntity.status(200).body(telephones);
    }

    @Override
    public ResponseEntity<Telephone> findById(Integer id) {
        if (telephoneRepository.existsById(id)) {
            Optional<Telephone> optionalTelephone = telephoneRepository.findById(id);
            logger.info(String.format("Telephone of id %d found", id));

            Telephone telephone = optionalTelephone.get();

            return ResponseEntity.status(200).body(telephone);
        }

        logger.error(String.format("Telephone of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Telephone> update(Integer id, Telephone updatedTelephone) {
        if (
                telephoneRepository.existsById(id) &&
                        telephoneRepository.findById(id).equals(updatedTelephone.getId())
        ) {

            logger.info(String.format("Telephone of id %d found", id));

            telephoneRepository.save(updatedTelephone);
            logger.info(String.format("Updated telephone: %s registration data!", updatedTelephone.toString()));

            return ResponseEntity.status(200).body(updatedTelephone);
        }

        logger.error(String.format("Telephone of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    @Override
    public ResponseEntity<Boolean> delete(Integer id) {
        Boolean success = false;
        if (telephoneRepository.existsById(id)) {
            Telephone telephone = telephoneRepository.findById(id).get();
            logger.info(String.format("Telephone of id %d found", id));

            telephoneRepository.deleteById(id);
            logger.info(String.format("Telephone: %s successfully deleted", telephone.toString()));
            success = true;

            return ResponseEntity.status(200).body(success);
        }

        logger.error(String.format("Telephone of id %d not found", id));
        return ResponseEntity.status(404).body(success);
    }
    private static final Logger logger = LogManager.getLogger(TelephoneServiceImpl.class.getName());
}
