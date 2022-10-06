package com.aperepair.aperepair.authorization.repository;

import com.aperepair.aperepair.authorization.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
