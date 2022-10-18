package com.aperepair.aperepair.authorization.domain.repository;

import com.aperepair.aperepair.authorization.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
