package com.skinplus.product_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skinplus.product_service.entity.ProductRequestDTO;
import com.skinplus.product_service.entity.ProductResponseDTO;
import com.skinplus.product_service.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ProductResponseDTO create(@Valid @RequestBody ProductRequestDTO request) {
        return service.create(request);
    }

    @GetMapping
    public List<ProductResponseDTO> getAll() {
        return service.getAll();
    }
}