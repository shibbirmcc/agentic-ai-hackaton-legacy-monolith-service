package com.legacy.monolith.repository;

import com.legacy.monolith.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Order findByOrderNumber(String orderNumber);
    
    List<Order> findByUser_Id(Long userId);
    
    List<Order> findByOrderStatus(String orderStatus);
    
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId ORDER BY created_date DESC", nativeQuery = true)
    List<Order> findUserOrdersNative(@Param("userId") Long userId);
    
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus = :status")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}