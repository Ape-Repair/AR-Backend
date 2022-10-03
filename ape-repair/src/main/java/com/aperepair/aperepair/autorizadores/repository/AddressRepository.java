package com.aperepair.aperepair.autorizadores.repository;

import com.aperepair.aperepair.autorizadores.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
