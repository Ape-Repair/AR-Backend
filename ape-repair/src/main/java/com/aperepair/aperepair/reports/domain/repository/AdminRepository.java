package com.aperepair.aperepair.reports.domain.repository;

import com.aperepair.aperepair.reports.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    String findByUsername(String username);
}
