package com.aperepair.aperepair.domain.repository;

import com.aperepair.aperepair.domain.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Integer> {
}
