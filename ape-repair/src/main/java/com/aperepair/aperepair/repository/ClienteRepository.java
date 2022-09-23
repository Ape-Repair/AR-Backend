package com.aperepair.aperepair.repository;

import com.aperepair.aperepair.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
