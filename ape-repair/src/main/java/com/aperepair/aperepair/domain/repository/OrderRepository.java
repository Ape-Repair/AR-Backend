package com.aperepair.aperepair.domain.repository;

import com.aperepair.aperepair.application.dto.response.OrderResponseDto;
import com.aperepair.aperepair.domain.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Customer_Order co SET co.status = ?2 WHERE co.id = ?1")
    void updateOrderIdByStatus(String id, String status);

    @Query("SELECT co FROM Customer_Order co WHERE service_type = ?1 AND paid = false")
    List<CustomerOrder> findByServiceTypeAvailableOrders(String serviceType);

    @Query("SELECT co FROM Customer_Order co WHERE customer_id = ?1 ORDER BY created_at")
    List<CustomerOrder> findByAscendingOrderOfCustomer(Integer customerId);

    @Query("SELECT co FROM Customer_Order co WHERE provider_id = ?1 ORDER BY created_at")
    List<CustomerOrder> findByAscendingOrderOfProvider(Integer providerId);

    @Query("SELECT co FROM Customer_Order co WHERE id = ?1")
    Optional<CustomerOrder> existsAndFindByOrderUlid(String orderId);

    @Query("SELECT co FROM Customer_Order co WHERE customer_id = ?1")
    Optional<List<OrderResponseDto>> findOrdersByCustomerId(Integer customerId);
}