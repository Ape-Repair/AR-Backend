package com.aperepair.aperepair.authorization.domain.service;

import com.aperepair.aperepair.authorization.application.dto.request.AddressRequestDto;
import com.aperepair.aperepair.authorization.application.dto.response.AddressResponseDto;
import com.aperepair.aperepair.authorization.domain.model.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AddressService {

    public ResponseEntity<AddressResponseDto> create(@RequestBody AddressRequestDto addressRequestDto);

    public ResponseEntity<List<Address>> findAll();

    public ResponseEntity<Address> findById(Integer id);

    public ResponseEntity<AddressResponseDto> update(Integer id, AddressRequestDto addressRequestDto);

    public ResponseEntity<Boolean> delete(Integer id);
}
