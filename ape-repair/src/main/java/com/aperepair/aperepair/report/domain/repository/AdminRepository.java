package com.aperepair.aperepair.report.domain.repository;

import com.aperepair.aperepair.report.domain.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    Optional<Admin> findByUsername(String username);

    Boolean existsByUsername(String username);
}
