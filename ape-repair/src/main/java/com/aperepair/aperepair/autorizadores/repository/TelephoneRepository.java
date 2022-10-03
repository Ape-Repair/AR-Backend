package com.aperepair.aperepair.autorizadores.repository;

import com.aperepair.aperepair.autorizadores.model.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelephoneRepository extends JpaRepository<Telephone, Integer> {
}
