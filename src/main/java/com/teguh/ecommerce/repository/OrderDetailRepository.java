package com.teguh.ecommerce.repository;

import com.teguh.ecommerce.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    
    // Find order details by order ID
    List<OrderDetail> findByOrderId(Long orderId);
    
    // Find order details by product ID
    List<OrderDetail> findByProductId(Long productId);
    
    // Find order details with product information
    @Query("SELECT od FROM OrderDetail od JOIN FETCH od.product WHERE od.orderId = :orderId")
    List<OrderDetail> findByOrderIdWithProducts(@Param("orderId") Long orderId);
    
    // Get total quantity sold for a product
    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od WHERE od.productId = :productId")
    Integer getTotalQuantitySoldByProductId(@Param("productId") Long productId);
}