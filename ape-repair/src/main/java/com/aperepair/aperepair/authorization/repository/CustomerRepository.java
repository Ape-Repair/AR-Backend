package com.aperepair.aperepair.authorization.repository;

import com.aperepair.aperepair.authorization.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("select (email) from customer c where c.email = emailAttempt")
    Customer findByEmail(String emailAttempt);
}
