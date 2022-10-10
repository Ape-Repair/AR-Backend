package com.aperepair.aperepair.authorization.repository;

import com.aperepair.aperepair.authorization.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
}
