package com.teguh.ecommerce.repository;

import com.teguh.ecommerce.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    
    // Find stores by owner ID
    List<Store> findByOwnerId(Long ownerId);
    
    // Find active stores
    List<Store> findByActiveTrue();
    
    // Find stores by location
    List<Store> findByLocationContainingIgnoreCase(String location);
    
    // Find stores by name
    List<Store> findByNameContainingIgnoreCase(String name);
    
    // Find stores with rating above threshold
    List<Store> findByRatingGreaterThanEqual(Float rating);
}