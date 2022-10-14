package com.aperepair.aperepair.authorization.repository;

import com.aperepair.aperepair.authorization.model.Customer;
import com.aperepair.aperepair.authorization.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    @Query("select (email) from provider p where p.email = emailAttempt")
    Provider findByEmail(String emailAttempt);
}
