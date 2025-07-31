package com.teguh.ecommerce.service;

import com.teguh.ecommerce.dto.ProductDto;
import com.teguh.ecommerce.entity.Product;
import com.teguh.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    // Get all products
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Get product by ID
    public ProductDto getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return convertToDto(product.get());
        }
        throw new RuntimeException("Product not found with id: " + id);
    }
    
    // Create new product
    public ProductDto createProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }
    
    // Update product
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStock(productDto.getStock());
            product.setImageUrl(productDto.getImageUrl());
            
            Product updatedProduct = productRepository.save(product);
            return convertToDto(updatedProduct);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }
    
    // Delete product
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }
    
    // Search products by keyword
    public List<ProductDto> searchProducts(String keyword) {
        List<Product> products = productRepository.findByKeyword(keyword);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Get products with available stock
    public List<ProductDto> getAvailableProducts() {
        List<Product> products = productRepository.findByStockGreaterThan(0);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Helper method to convert Entity to DTO
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
    
    // Helper method to convert DTO to Entity
    private Product convertToEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        return product;
    }
}