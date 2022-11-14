package com.aperepair.aperepair.authorization.domain.repository;

import com.aperepair.aperepair.authorization.domain.model.Address;
import com.aperepair.aperepair.authorization.domain.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    Optional<Provider> findByEmail(String emailAttempt);

    Optional<Provider> findByCpf(String cpf);

    Boolean existsByCpf(String cpf);

    Boolean existsByEmail(String email);

    Boolean existsByCnpj(String cnpj);

    Boolean existsByPhone(String phone);

    List<Provider> findByIsAuthenticatedTrue();

    @Transactional
    @Modifying
    @Query("UPDATE Provider p SET p.addressId = ?1 WHERE p.id = ?2")
    void updateAddressIdById(Address addressId, Integer customerId);
}