package com.teguh.ecommerce.service;

import com.teguh.ecommerce.dto.*;
import com.teguh.ecommerce.entity.*;
import com.teguh.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    // @Autowired
    // private UserRepository userRepository;
    
    // @Autowired
    // private StoreRepository storeRepository;
    
    // Get all orders for a user
    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdWithOrderDetails(userId);
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Get order by ID
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findByIdWithOrderDetails(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDto(order);
    }
    
    // Get order by invoice number
    public OrderDto getOrderByInvoiceNumber(String invoiceNumber) {
        Order order = orderRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return convertToDto(order);
    }
    
    // Checkout - Create order from cart items
    @Transactional
    public OrderDto checkout(CheckoutDto checkoutDto) {
        // Get cart items
        List<Cart> cartItems = cartRepository.findByUserIdWithProducts(checkoutDto.getUserId());
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Filter cart items based on provided IDs (if specified)
        if (checkoutDto.getCartItemIds() != null && !checkoutDto.getCartItemIds().isEmpty()) {
            cartItems = cartItems.stream()
                    .filter(cart -> checkoutDto.getCartItemIds().contains(cart.getId()))
                    .collect(Collectors.toList());
        }
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("No valid cart items found");
        }
        
        // Validate stock and calculate total
        BigDecimal totalAmount = BigDecimal.ZERO;
        Long storeId = null;
        
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            
            // Check stock
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            // Calculate subtotal
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
            
            // Set store ID (assuming all items from same store for simplicity)
            if (storeId == null) {
                storeId = product.getStoreId();
            }
        }
        
        // Create order
        Order order = new Order();
        order.setInvoiceNumber(generateInvoiceNumber());
        order.setUserId(checkoutDto.getUserId());
        order.setStoreId(storeId);
        order.setStatus("PENDING");
        order.setTotalAmount(totalAmount);
        order.setShippingInfo(checkoutDto.getShippingInfo());
        
        Order savedOrder = orderRepository.save(order);
        
        // Create order details and update product stock
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            
            // Create order detail
            OrderDetail orderDetail = new OrderDetail(
                    savedOrder.getId(),
                    product.getId(),
                    cartItem.getQuantity(),
                    product.getPrice()
            );
            orderDetailRepository.save(orderDetail);
            
            // Update product stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        // Clear cart items
        cartRepository.deleteByIdInAndUserId(
                cartItems.stream().map(Cart::getId).collect(Collectors.toList()),
                checkoutDto.getUserId()
        );
        
        return convertToDto(savedOrder);
    }
    
    // Update order status
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }
    
    // Generate unique invoice number
    private String generateInvoiceNumber() {
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        return "INV-" + dateTime + "-" + timestamp;
    }
    
    // Convert Entity to DTO
    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setInvoiceNumber(order.getInvoiceNumber());
        dto.setUserId(order.getUserId());
        dto.setStoreId(order.getStoreId());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingInfo(order.getShippingInfo());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        // Add user and store names if available
        if (order.getUser() != null) {
            dto.setUserName(order.getUser().getUsername());
        }
        if (order.getStore() != null) {
            dto.setStoreName(order.getStore().getName());
        }
        
        // Add order details
        if (order.getOrderDetails() != null) {
            dto.setOrderDetails(order.getOrderDetails().stream()
                    .map(this::convertOrderDetailToDto)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    // Convert OrderDetail Entity to DTO
    private OrderDetailDto convertOrderDetailToDto(OrderDetail orderDetail) {
        OrderDetailDto dto = new OrderDetailDto();
        dto.setId(orderDetail.getId());
        dto.setOrderId(orderDetail.getOrderId());
        dto.setProductId(orderDetail.getProductId());
        dto.setQuantity(orderDetail.getQuantity());
        dto.setPrice(orderDetail.getPrice());
        dto.setSubtotal(orderDetail.getSubtotal());
        dto.setCreatedAt(orderDetail.getCreatedAt());
        
        // Add product details if available
        if (orderDetail.getProduct() != null) {
            dto.setProductName(orderDetail.getProduct().getName());
            dto.setProductImageUrl(orderDetail.getProduct().getImageUrl());
        }
        
        return dto;
    }
}