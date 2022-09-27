package com.aperepair.aperepair.autorizadores.repository;

import com.aperepair.aperepair.autorizadores.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
