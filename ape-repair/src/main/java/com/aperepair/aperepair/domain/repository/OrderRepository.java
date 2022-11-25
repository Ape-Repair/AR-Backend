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

    @Query("SELECT co FROM Customer_Order co WHERE customer_id = ?1")
    List<CustomerOrder> getAllOrdersFromCustomerId(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Customer_Order co SET co.status = ?2 WHERE co.id = ?1")
    void updateOrderIdByStatus(Integer id, String status);

    @Query("SELECT co FROM Customer_Order co WHERE service_type = ?1 AND paid = false")
    List<CustomerOrder> findByServiceTypeAvailableOrders(String serviceType);
}