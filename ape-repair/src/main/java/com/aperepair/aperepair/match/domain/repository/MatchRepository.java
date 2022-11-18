package com.aperepair.aperepair.match.domain.repository;

import com.aperepair.aperepair.authorization.domain.model.Customer;
import com.aperepair.aperepair.authorization.domain.model.Provider;
import com.aperepair.aperepair.match.domain.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {

    List<Match> findByCustomer(Customer customer);

    List<Match> findByProvider(Provider provider);

    Optional<List<Match>> findByStatus(String status);
}
