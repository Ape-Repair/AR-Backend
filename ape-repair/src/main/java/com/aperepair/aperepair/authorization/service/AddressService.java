package com.aperepair.aperepair.authorization.service;

import com.aperepair.aperepair.authorization.model.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AddressService {

    public ResponseEntity<Address> create(@RequestBody Address address);

    public ResponseEntity<List<Address>> findAll();

    public ResponseEntity<Address> findById(Integer id);

    public ResponseEntity<Address> update(Integer id, Address updatedAddress);

    public ResponseEntity<Boolean> delete(Integer id);
}
