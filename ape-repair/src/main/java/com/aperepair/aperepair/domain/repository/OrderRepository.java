package com.aperepair.aperepair.domain.repository;

import com.aperepair.aperepair.domain.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {

    @Query("select co from Customer_Order co where customer_id = ?1")
    List<CustomerOrder> getAllOrdersFromCustomerId(Integer id);

    @Transactional
    @Modifying
    @Query("update Customer_Order co set co.status = ?2 where co.id = ?1")
    void updateOrderIdByStatus(Integer id, String status);
}