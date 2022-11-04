package com.aperepair.aperepair.authorization.domain.repository;

import com.aperepair.aperepair.authorization.domain.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    Optional<Provider> findByEmail(String emailAttempt);

    Optional<Provider> findByCpf(String cpf);

    Boolean existsByCpf(String cpf);

    Boolean existsByCnpj(String cnpj);

    Optional<List<Provider>> findByGenreIsFAndIsAuthenticatedTrue();

    List<Provider> findByIsAuthenticatedTrue();
}