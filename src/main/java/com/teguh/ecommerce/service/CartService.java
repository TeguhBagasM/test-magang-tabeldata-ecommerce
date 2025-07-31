package com.teguh.ecommerce.service;

import com.teguh.ecommerce.dto.CartDto;
import com.teguh.ecommerce.entity.Cart;
import com.teguh.ecommerce.entity.Product;
import com.teguh.ecommerce.repository.CartRepository;
import com.teguh.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    // Get all cart items for a user
    public List<CartDto> getCartByUserId(Long userId) {
        List<Cart> carts = cartRepository.findByUserIdWithProducts(userId);
        return carts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Add item to cart
    @Transactional
    public CartDto addToCart(CartDto cartDto) {
        // Check if product exists
        Product product = productRepository.findById(cartDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Check if product has enough stock
        if (product.getStock() < cartDto.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        
        // Check if item already exists in cart
        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(
                cartDto.getUserId(), cartDto.getProductId());
        
        Cart cart;
        if (existingCart.isPresent()) {
            // Update quantity if item already exists
            cart = existingCart.get();
            int newQuantity = cart.getQuantity() + cartDto.getQuantity();
            
            // Check total quantity against stock
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Insufficient stock for total quantity");
            }
            
            cart.setQuantity(newQuantity);
        } else {
            // Create new cart item
            cart = new Cart(cartDto.getUserId(), cartDto.getProductId(), cartDto.getQuantity());
        }
        
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }
    
    // Update cart item quantity
    @Transactional
    public CartDto updateCartItem(Long cartId, Integer quantity, Long userId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        // Check if cart belongs to user
        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }
        
        // Check if product has enough stock
        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }
        
        cart.setQuantity(quantity);
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    }
    
    // Remove item from cart
    @Transactional
    public void removeFromCart(Long cartId, Long userId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        // Check if cart belongs to user
        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }
        
        cartRepository.delete(cart);
    }
    
    // Clear all cart items for user
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
    
    // Get cart item count for user
    public Integer getCartItemCount(Long userId) {
        return cartRepository.countTotalItemsByUserId(userId);
    }
    
    // Convert Entity to DTO
    private CartDto convertToDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setProductId(cart.getProductId());
        dto.setQuantity(cart.getQuantity());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        
        // Add product details if available
        if (cart.getProduct() != null) {
            dto.setProductName(cart.getProduct().getName());
            dto.setProductPrice(cart.getProduct().getPrice());
            dto.setProductImageUrl(cart.getProduct().getImageUrl());
            dto.setProductStock(cart.getProduct().getStock());
        }
        
        return dto;
    }
}