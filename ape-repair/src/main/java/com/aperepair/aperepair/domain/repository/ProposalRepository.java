package com.aperepair.aperepair.domain.repository;

import com.aperepair.aperepair.domain.model.CustomerOrder;
import com.aperepair.aperepair.domain.model.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProposalRepository extends JpaRepository<Proposal, Integer> {

    @Transactional
    @Modifying
    @Query("select p from Proposal p where customer_order_id = ?1")
    List<Proposal> getAllByCustomerOrderId(Integer id);
}
