package com.aperepair.aperepair.authorization.repository;

import com.aperepair.aperepair.authorization.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String emailAttempt);

    Optional<Customer> findByCpf(String cpf);
}
