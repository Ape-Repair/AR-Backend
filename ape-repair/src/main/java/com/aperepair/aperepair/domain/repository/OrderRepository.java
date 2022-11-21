package com.aperepair.aperepair.domain.repository;

import com.aperepair.aperepair.domain.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {

//    @Transactional
//    @Modifying
//    @Query("INSERT INTO order(" +
//            "id, " +
//            "service_type," +
//            " description," +
//            " customer_id," +
//            " provider_id," +
//            " amount," +
//            " status," +
//            " paid," +
//            " created_at" +
//            ") values ()")
}
