package com.aperepair.aperepair.autorizadores.service;

import com.aperepair.aperepair.autorizadores.model.Customer;
import com.aperepair.aperepair.autorizadores.model.dto.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

public interface CustomerService {

    public ResponseEntity<CustomerDto> create(@RequestBody Customer customer);

    public ResponseEntity<List<Customer>> findAll();

    public ResponseEntity<Customer> findById(Integer id);

    public ResponseEntity<Customer> update(Integer id, Customer updatedCustomer);

    public ResponseEntity<Boolean> delete(Integer id);
}
