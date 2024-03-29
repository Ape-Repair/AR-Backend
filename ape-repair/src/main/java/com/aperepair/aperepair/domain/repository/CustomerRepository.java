package com.aperepair.aperepair.domain.repository;

import com.aperepair.aperepair.domain.model.Address;
import com.aperepair.aperepair.domain.model.Customer;
import com.aperepair.aperepair.domain.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Customer c SET c.addressId = ?1 WHERE c.id = ?2")
    void updateAddressIdById(Address addressId, Integer customerId);

    @Query("SELECT c From Customer c WHERE c.email = ?1")
    Optional<Customer> findByEmail(String emailAttempt);

    @Query("SELECT count(c.id) FROM Customer c WHERE c.role = 'CUSTOMER'")
    long countActives();

    Boolean existsByCpf(String cpf);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    @Query("select c from Customer c where c.id = ?1")
    Customer findCustomerById(Integer id);
}
