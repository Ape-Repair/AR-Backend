package com.aperepair.aperepair.authorization.repository;

import com.aperepair.aperepair.authorization.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    Optional<Provider> findByEmail(String emailAttempt);

    Optional<Provider> findByCpf(String cpf);
}