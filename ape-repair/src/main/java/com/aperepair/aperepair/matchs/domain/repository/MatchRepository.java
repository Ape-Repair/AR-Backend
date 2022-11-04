package com.aperepair.aperepair.matchs.domain.repository;

import com.aperepair.aperepair.matchs.domain.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
}
