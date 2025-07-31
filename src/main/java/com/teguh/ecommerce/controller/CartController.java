package com.teguh.ecommerce.controller;

import com.teguh.ecommerce.dto.CartDto;
import com.teguh.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    // GET /api/cart/{userId} - Get user's cart items
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartDto>> getUserCart(@PathVariable Long userId) {
        try {
            List<CartDto> cartItems = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // POST /api/cart - Add item to cart
    @PostMapping
    public ResponseEntity<CartDto> addToCart(@Valid @RequestBody CartDto cartDto) {
        try {
            CartDto addedItem = cartService.addToCart(cartDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // PUT /api/cart/{cartId} - Update cart item quantity
    @PutMapping("/{cartId}")
    public ResponseEntity<CartDto> updateCartItem(@PathVariable Long cartId, 
                                                 @RequestParam Integer quantity,
                                                 @RequestParam Long userId) {
        try {
            CartDto updatedItem = cartService.updateCartItem(cartId, quantity, userId);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // DELETE /api/cart/{cartId} - Remove item from cart
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartId, 
                                             @RequestParam Long userId) {
        try {
            cartService.removeFromCart(cartId, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // DELETE /api/cart/clear/{userId} - Clear user's cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET /api/cart/count/{userId} - Get cart item count
    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> getCartItemCount(@PathVariable Long userId) {
        try {
            Integer count = cartService.getCartItemCount(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}