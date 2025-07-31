package com.teguh.ecommerce.repository;

import com.teguh.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    // Find all cart items by user ID
    List<Cart> findByUserId(Long userId);
    
    // Find specific cart item by user and product
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
    
    // Delete all cart items by user ID
    void deleteByUserId(Long userId);
    
    // Delete specific cart items by IDs and user ID (for security)
    void deleteByIdInAndUserId(List<Long> ids, Long userId);
    
    // Check if user has items in cart
    boolean existsByUserId(Long userId);
    
    // Count total items in user's cart
    @Query("SELECT COALESCE(SUM(c.quantity), 0) FROM Cart c WHERE c.userId = :userId")
    Integer countTotalItemsByUserId(@Param("userId") Long userId);
    
    // Get cart items with product details using JOIN
    @Query("SELECT c FROM Cart c JOIN FETCH c.product WHERE c.userId = :userId")
    List<Cart> findByUserIdWithProducts(@Param("userId") Long userId);
}