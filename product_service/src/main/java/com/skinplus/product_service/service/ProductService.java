package com.skinplus.product_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.skinplus.product_service.entity.Product;
import com.skinplus.product_service.entity.ProductRequestDTO;
import com.skinplus.product_service.entity.ProductResponseDTO;
import com.skinplus.product_service.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // CREATE
    public ProductResponseDTO create(ProductRequestDTO request) {

        Product product = new Product();

        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setSkinType(request.getSkinType());
        product.setMfgDate(request.getMfgDate());
        product.setExpDate(request.getExpDate());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = repo.save(product);

        return mapToResponse(saved);
    }

    // GET ALL
    public List<ProductResponseDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public ProductResponseDTO getById(Long id) {

        Product product = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return mapToResponse(product);
    }

    // DELETE
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        repo.deleteById(id);
    }

    // MAPPER
    private ProductResponseDTO mapToResponse(Product product) {

        ProductResponseDTO response = new ProductResponseDTO();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setBrand(product.getBrand());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setDescription(product.getDescription());
        response.setSkinType(product.getSkinType());
        response.setMfgDate(product.getMfgDate());
        response.setExpDate(product.getExpDate());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }
}
