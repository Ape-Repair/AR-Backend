package com.aperepair.aperepair.authorization.service;

import com.aperepair.aperepair.authorization.model.Customer;
import com.aperepair.aperepair.authorization.model.dto.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

public interface CustomerService {

    public ResponseEntity<CustomerDto> create(@RequestBody Customer customer);

    public ResponseEntity<List<CustomerDto>> findAll();

    public ResponseEntity<CustomerDto> findById(Integer id);

    public ResponseEntity<CustomerDto> update(Integer id, Customer updatedCustomer);

    public ResponseEntity<Boolean> delete(Integer id);
}
