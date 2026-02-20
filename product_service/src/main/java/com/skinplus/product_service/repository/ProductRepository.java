package com.skinplus.product_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skinplus.product_service.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}