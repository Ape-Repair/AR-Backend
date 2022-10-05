package com.aperepair.aperepair.autorizadores.service;

import com.aperepair.aperepair.autorizadores.model.Provider;
import com.aperepair.aperepair.autorizadores.model.dto.ProviderDto;
import com.aperepair.aperepair.autorizadores.model.dto.factory.ProviderDtoFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProviderService {

    public ResponseEntity<ProviderDto> create(@RequestBody Provider provider);

    public ResponseEntity<List<Provider>> findAll();

    public ResponseEntity<Provider> findById(Integer id);

    public ResponseEntity<Provider> update(Integer id, Provider updatedProvider);

    public ResponseEntity<Boolean> delete(Integer id);
}
