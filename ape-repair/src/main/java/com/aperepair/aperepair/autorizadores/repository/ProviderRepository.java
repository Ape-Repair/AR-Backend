package com.aperepair.aperepair.autorizadores.repository;

import com.aperepair.aperepair.autorizadores.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {
}
