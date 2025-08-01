-- PostgreSQL Database Schema for teguh_ecommerce
-- Converted from MySQL

-- Create database (run this separately as superuser)
-- CREATE DATABASE teguh_ecommerce;

-- Connect to the database
-- \c teguh_ecommerce;

-- Enable UUID extension (optional, for future use)
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- --------------------------------------------------------

--
-- Table structure for table users
--

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------

--
-- Table structure for table categories
--

CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    parent_id INTEGER REFERENCES categories(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------

--
-- Table structure for table stores
--

CREATE TABLE stores (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(100),
    active BOOLEAN DEFAULT TRUE,
    rating REAL DEFAULT 0,
    logo_url VARCHAR(255),
    owner_id INTEGER REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------

--
-- Table structure for table products
--

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(12,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    weight DECIMAL(10,2),
    condition VARCHAR(20),
    store_id INTEGER REFERENCES stores(id),
    category_id INTEGER REFERENCES categories(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    image_url VARCHAR(255)
);

-- --------------------------------------------------------

--
-- Table structure for table carts
--

CREATE TABLE carts (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id)
);

-- --------------------------------------------------------

--
-- Table structure for table orders
--

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    user_id INTEGER REFERENCES users(id),
    store_id INTEGER REFERENCES stores(id),
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    shipping_info JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------

--
-- Table structure for table order_details
--

CREATE TABLE order_details (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id INTEGER NOT NULL REFERENCES products(id),
    quantity INTEGER NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------

-- Create functions for auto-updating timestamps

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for auto-updating updated_at columns

CREATE TRIGGER update_products_updated_at 
    BEFORE UPDATE ON products 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_carts_updated_at 
    BEFORE UPDATE ON carts 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_orders_updated_at 
    BEFORE UPDATE ON orders 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- --------------------------------------------------------

-- Insert sample data

-- Insert users
INSERT INTO users (id, username, email, password, created_at) VALUES
(1, 'teguh', 'teguh@example.com', 'teguh', '2023-01-01 08:00:00'),
(2, 'ronaldo', 'ronaldo@example.com', 'ronaldo', '2023-01-02 09:00:00'),
(3, 'bagas', 'bagas@example.com', 'bagas', '2023-01-03 10:00:00');

-- Reset sequence for users
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- Insert categories
INSERT INTO categories (id, name, parent_id, created_at) VALUES
(1, 'Elektronik', NULL, '2023-01-10 06:00:00'),
(2, 'Fashion', NULL, '2023-01-10 06:00:00'),
(3, 'Laptop', 1, '2023-01-10 07:00:00'),
(4, 'Smartphone', 1, '2023-01-10 07:00:00'),
(5, 'Tablet', 1, '2023-01-10 07:00:00'),
(6, 'Pria', 2, '2023-01-10 08:00:00'),
(7, 'Wanita', 2, '2023-01-10 08:00:00');

-- Reset sequence for categories
SELECT setval('categories_id_seq', (SELECT MAX(id) FROM categories));

-- Insert stores
INSERT INTO stores (id, name, description, location, active, rating, logo_url, owner_id, created_at) VALUES
(1, 'iStoreBdg', 'Official Apple Reseller', 'Bandung', TRUE, 4.9, 'https://example.com/istorebdg.jpg', 1, '2023-01-05 07:00:00'),
(2, 'TechGadget', 'Teknologi Terkini', 'Jakarta', TRUE, 4.7, 'https://example.com/techgadget.jpg', 2, '2023-01-06 08:00:00'),
(3, 'Elektronik Murah', 'Harga Terjangkau', 'Surabaya', TRUE, 4.5, 'https://example.com/elektronikmurah.jpg', 3, '2023-01-07 09:00:00');

-- Reset sequence for stores
SELECT setval('stores_id_seq', (SELECT MAX(id) FROM stores));

-- Insert products
INSERT INTO products (id, name, description, price, stock, weight, condition, store_id, category_id, created_at, updated_at, image_url) VALUES
(1, 'MacBook Pro 14" M2', 'Apple MacBook Pro 14 inch dengan chip M2', 36499000.00, 10, 1.60, 'Baru', 1, 3, '2023-01-15 08:00:00', '2023-01-15 08:00:00', NULL),
(2, 'iPhone 15 Pro', 'Apple iPhone 15 Pro 128GB', 24999000.00, 15, 0.20, 'Baru', 1, 4, '2023-01-16 09:00:00', '2023-01-16 09:00:00', NULL),
(3, 'Dell XPS 15', 'Laptop Dell XPS 15 dengan Intel i7', 22999000.00, 5, 1.80, 'Baru', 2, 3, '2023-01-17 10:00:00', '2023-01-17 10:00:00', NULL),
(4, 'Samsung Galaxy S23', 'Smartphone Samsung Galaxy S23 256GB', 17999000.00, 8, 0.19, 'Baru', 2, 4, '2023-01-18 11:00:00', '2023-01-18 11:00:00', NULL),
(5, 'iPad Air', 'Apple iPad Air generasi terbaru', 12999000.00, 12, 0.46, 'Baru', 3, 5, '2023-01-19 12:00:00', '2023-01-19 12:00:00', NULL),
(6, 'Laptop Gaming', 'High performance gaming laptop', 15000000.00, 10, NULL, NULL, NULL, NULL, '2025-07-31 20:09:55', '2025-07-31 20:09:55', 'https://example.com/laptop.jpg');

-- Reset sequence for products
SELECT setval('products_id_seq', (SELECT MAX(id) FROM products));

-- Insert carts
INSERT INTO carts (id, user_id, product_id, quantity, created_at, updated_at) VALUES
(4, 2, 4, 2, '2025-07-31 17:02:15', '2025-07-31 17:02:15'),
(5, 1, 2, 3, '2025-08-01 00:02:37', '2025-08-01 00:02:37'),
(6, 3, 1, 2, '2025-08-01 00:03:02', '2025-08-01 00:03:02');

-- Reset sequence for carts
SELECT setval('carts_id_seq', (SELECT MAX(id) FROM carts));

-- Insert orders
INSERT INTO orders (id, invoice_number, user_id, store_id, status, total_amount, shipping_info, created_at, updated_at) VALUES
(1, 'INV-20230201-001', 1, 1, 'COMPLETED', 86497000.00, '{"courier": "JNE", "service": "REG", "address": "Jl. Merdeka No.1, Bandung"}', '2023-02-01 14:00:00', '2023-02-05 08:00:00'),
(2, 'INV-20230202-002', 2, 2, 'SHIPPED', 22999000.00, '{"courier": "SiCepat", "service": "EXPRESS", "address": "Jl. Sudirman No.45, Jakarta"}', '2023-02-02 09:00:00', '2025-08-01 00:11:21');

-- Reset sequence for orders
SELECT setval('orders_id_seq', (SELECT MAX(id) FROM orders));

-- Insert order_details
INSERT INTO order_details (id, order_id, product_id, quantity, price, subtotal, created_at) VALUES
(1, 1, 1, 2, 36499000.00, 72998000.00, '2025-07-31 17:02:15'),
(2, 1, 5, 1, 12999000.00, 12999000.00, '2025-07-31 17:02:15'),
(3, 1, 2, 0, 24999000.00, 500000.00, '2025-07-31 17:02:15'),
(4, 2, 3, 1, 22999000.00, 22999000.00, '2025-07-31 17:02:15');

-- Reset sequence for order_details
SELECT setval('order_details_id_seq', (SELECT MAX(id) FROM order_details));

-- --------------------------------------------------------

-- Create indexes for better performance

CREATE INDEX idx_carts_user_id ON carts(user_id);
CREATE INDEX idx_carts_product_id ON carts(product_id);
CREATE INDEX idx_categories_parent_id ON categories(parent_id);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_store_id ON orders(store_id);
CREATE INDEX idx_order_details_order_id ON order_details(order_id);
CREATE INDEX idx_order_details_product_id ON order_details(product_id);
CREATE INDEX idx_products_store_id ON products(store_id);
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_stores_owner_id ON stores(owner_id);

-- Create additional indexes for JSON operations
CREATE INDEX idx_orders_shipping_info_gin ON orders USING GIN (shipping_info);