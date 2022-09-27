package com.aperepair.aperepair.autorizadores.repository;

import com.aperepair.aperepair.autorizadores.model.Prestador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestadorRepository extends JpaRepository<Prestador, Integer> {
}
