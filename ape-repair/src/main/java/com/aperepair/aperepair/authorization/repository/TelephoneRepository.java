package com.aperepair.aperepair.authorization.repository;

import com.aperepair.aperepair.authorization.model.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelephoneRepository extends JpaRepository<Telephone, Integer> {
}
