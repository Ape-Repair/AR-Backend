package com.aperepair.aperepair.authorization.service;

import com.aperepair.aperepair.authorization.model.Provider;
import com.aperepair.aperepair.authorization.model.enums.dto.ProviderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ProviderService {

    public ResponseEntity<ProviderDto> create(@RequestBody Provider provider);

    public ResponseEntity<List<Provider>> findAll();

    public ResponseEntity<Provider> findById(Integer id);

    public ResponseEntity<Provider> update(Integer id, Provider updatedProvider);

    public ResponseEntity<Boolean> delete(Integer id);
}
