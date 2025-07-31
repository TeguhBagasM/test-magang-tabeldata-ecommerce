package com.teguh.ecommerce.repository;

import com.teguh.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Find orders by user ID
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Find order by invoice number
    Optional<Order> findByInvoiceNumber(String invoiceNumber);
    
    // Find orders by status
    List<Order> findByStatus(String status);
    
    // Find orders by user and status
    List<Order> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
    
    // Find orders by store ID
    List<Order> findByStoreIdOrderByCreatedAtDesc(Long storeId);
    
    // Check if invoice number exists
    boolean existsByInvoiceNumber(String invoiceNumber);
    
    // Get order with order details
    @Query("SELECT o FROM Order o JOIN FETCH o.orderDetails WHERE o.id = :orderId")
    Optional<Order> findByIdWithOrderDetails(@Param("orderId") Long orderId);
    
    // Get orders with order details by user ID
    @Query("SELECT o FROM Order o JOIN FETCH o.orderDetails WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUserIdWithOrderDetails(@Param("userId") Long userId);
}