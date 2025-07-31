-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 31, 2025 at 06:15 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `teguh_ecommerce`
--

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`id`, `name`, `parent_id`, `created_at`) VALUES
(1, 'Elektronik', NULL, '2023-01-10 06:00:00'),
(2, 'Fashion', NULL, '2023-01-10 06:00:00'),
(3, 'Laptop', 1, '2023-01-10 07:00:00'),
(4, 'Smartphone', 1, '2023-01-10 07:00:00'),
(5, 'Tablet', 1, '2023-01-10 07:00:00'),
(6, 'Pria', 2, '2023-01-10 08:00:00'),
(7, 'Wanita', 2, '2023-01-10 08:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `invoice_number` varchar(50) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `store_id` int(11) DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  `total_amount` decimal(12,2) NOT NULL,
  `shipping_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`shipping_info`)),
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `invoice_number`, `user_id`, `store_id`, `status`, `total_amount`, `shipping_info`, `created_at`, `updated_at`) VALUES
(1, 'INV-20230201-001', 1, 1, 'COMPLETED', 86497000.00, '{\"courier\": \"JNE\", \"service\": \"REG\", \"address\": \"Jl. Merdeka No.1, Bandung\"}', '2023-02-01 14:00:00', '2023-02-05 08:00:00'),
(2, 'INV-20230202-002', 2, 2, 'PROCESSING', 22999000.00, '{\"courier\": \"SiCepat\", \"service\": \"EXPRESS\", \"address\": \"Jl. Sudirman No.45, Jakarta\"}', '2023-02-02 09:00:00', '2023-02-02 09:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(12,2) NOT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `weight` decimal(10,2) DEFAULT NULL,
  `condition` varchar(20) DEFAULT NULL,
  `store_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `image_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `description`, `price`, `stock`, `weight`, `condition`, `store_id`, `category_id`, `created_at`, `updated_at`, `image_url`) VALUES
(1, 'MacBook Pro 14\" M2', 'Apple MacBook Pro 14 inch dengan chip M2', 36499000.00, 10, 1.60, 'Baru', 1, 3, '2023-01-15 08:00:00', '2023-01-15 08:00:00', NULL),
(2, 'iPhone 15 Pro', 'Apple iPhone 15 Pro 128GB', 24999000.00, 15, 0.20, 'Baru', 1, 4, '2023-01-16 09:00:00', '2023-01-16 09:00:00', NULL),
(3, 'Dell XPS 15', 'Laptop Dell XPS 15 dengan Intel i7', 22999000.00, 5, 1.80, 'Baru', 2, 3, '2023-01-17 10:00:00', '2023-01-17 10:00:00', NULL),
(4, 'Samsung Galaxy S23', 'Smartphone Samsung Galaxy S23 256GB', 17999000.00, 8, 0.19, 'Baru', 2, 4, '2023-01-18 11:00:00', '2023-01-18 11:00:00', NULL),
(5, 'iPad Air', 'Apple iPad Air generasi terbaru', 12999000.00, 12, 0.46, 'Baru', 3, 5, '2023-01-19 12:00:00', '2023-01-19 12:00:00', NULL),
(6, 'Laptop Gaming', 'High performance gaming laptop', 15000000.00, 10, NULL, NULL, NULL, NULL, '2025-07-31 20:09:55', '2025-07-31 20:09:55', 'https://example.com/laptop.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `stores`
--

CREATE TABLE `stores` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `active` tinyint(1) DEFAULT 1,
  `rating` float DEFAULT 0,
  `logo_url` varchar(255) DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stores`
--

INSERT INTO `stores` (`id`, `name`, `description`, `location`, `active`, `rating`, `logo_url`, `owner_id`, `created_at`) VALUES
(1, 'iStoreBdg', 'Official Apple Reseller', 'Bandung', 1, 4.9, 'https://example.com/istorebdg.jpg', 1, '2023-01-05 07:00:00'),
(2, 'TechGadget', 'Teknologi Terkini', 'Jakarta', 1, 4.7, 'https://example.com/techgadget.jpg', 2, '2023-01-06 08:00:00'),
(3, 'Elektronik Murah', 'Harga Terjangkau', 'Surabaya', 1, 4.5, 'https://example.com/elektronikmurah.jpg', 3, '2023-01-07 09:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `created_at`) VALUES
(1, 'john_doe', 'john@example.com', 'password123', '2023-01-01 08:00:00'),
(2, 'jane_smith', 'jane@example.com', 'password123', '2023-01-02 09:00:00'),
(3, 'budi_utama', 'budi@example.com', 'password123', '2023-01-03 10:00:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD KEY `parent_id` (`parent_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `invoice_number` (`invoice_number`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `store_id` (`store_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `store_id` (`store_id`),
  ADD KEY `category_id` (`category_id`);

--
-- Indexes for table `stores`
--
ALTER TABLE `stores`
  ADD PRIMARY KEY (`id`),
  ADD KEY `owner_id` (`owner_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `stores`
--
ALTER TABLE `stores`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `categories`
--
ALTER TABLE `categories`
  ADD CONSTRAINT `categories_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`);

--
-- Constraints for table `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `products_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `products_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

--
-- Constraints for table `stores`
--
ALTER TABLE `stores`
  ADD CONSTRAINT `stores_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
