package com.skinplus.product_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skinplus.product_service.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
	
	Page<Product> findByDeletedFalse(Pageable pageable);

	Page<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);
}