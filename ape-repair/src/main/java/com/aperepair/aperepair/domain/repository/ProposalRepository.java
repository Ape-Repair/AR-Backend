package com.aperepair.aperepair.domain.repository;

import com.aperepair.aperepair.domain.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Integer> {

    @Transactional
    @Modifying
    @Query("select p from Proposal p where order_id = ?1")
    List<Proposal> getAllByOrderId(String id);

    @Query("SELECT p FROM Proposal p WHERE provider_id = ?1 AND order_id = ?2")
    Optional<Proposal> providerHasAlreadyMadeProposalForThisOrder(Integer providerId, String orderId);
}
