package com.aperepair.aperepair.authorization.domain.service.impl;

import com.aperepair.aperepair.authorization.application.dto.request.AddressRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.AddressResponseDto;
import com.aperepair.aperepair.authorization.domain.dto.factory.AddressDtoFactory;
import com.aperepair.aperepair.authorization.domain.model.Address;
import com.aperepair.aperepair.authorization.domain.repository.AddressRepository;
import com.aperepair.aperepair.authorization.domain.service.AddressService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public ResponseEntity<List<Address>> findAll() {
        List<Address> addresses = new ArrayList(addressRepository.findAll());

        if (addresses.isEmpty()) {
            logger.info("There are no registered addresses");
            return ResponseEntity.status(204).build();
        }

        logger.info("Success in finding registered addresses");
        return ResponseEntity.status(200).body(addresses);
    }

    @Override
    public ResponseEntity<Address> findById(Integer id) {
        if (addressRepository.existsById(id)) {
            Optional<Address> optionalAddress = addressRepository.findById(id);
            logger.info(String.format("Address of id %d found", id));

            Address address = optionalAddress.get();

            return ResponseEntity.status(200).body(address);
        }

        logger.error(String.format("Address of id %d not found", id));
        return ResponseEntity.status(404).build();
    }

    private static final Logger logger = LogManager.getLogger(AddressServiceImpl.class.getName());
}