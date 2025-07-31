package com.teguh.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CheckoutDto {
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Cart items are required")
    private List<Long> cartItemIds;
    
    @NotBlank(message = "Shipping info is required")
    private String shippingInfo;
    
    // Constructors
    public CheckoutDto() {}
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public List<Long> getCartItemIds() { return cartItemIds; }
    public void setCartItemIds(List<Long> cartItemIds) { this.cartItemIds = cartItemIds; }
    
    public String getShippingInfo() { return shippingInfo; }
    public void setShippingInfo(String shippingInfo) { this.shippingInfo = shippingInfo; }
}