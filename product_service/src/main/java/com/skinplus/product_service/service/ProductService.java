package com.skinplus.product_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.skinplus.product_service.entity.*;
import com.skinplus.product_service.exception.ProductNotFoundException;
import com.skinplus.product_service.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository repo;
	private static final Logger log = LoggerFactory.getLogger(ProductService.class);

	public ProductService(ProductRepository repo) {
		this.repo = repo;
	}

	// ================= CREATE =================
	@CacheEvict(value = { "products", "product" }, allEntries = true)
	public ProductResponseDTO create(ProductRequestDTO request) {

		log.info("Creating product: {}", request.getName());

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

		Product saved = repo.save(product);

		log.info("Product saved id: {}", saved.getId());

		return mapToResponse(saved);
	}

	// ================= GET BY ID =================
	@Cacheable(value = "product", key = "#id")
	public ProductResponseDTO getById(Long id) {

		log.info("Fetching product from DB id: {}", id);

		Product product = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		return mapToResponse(product);
	}

	// ================= UPDATE =================
	@CacheEvict(value = { "products", "product" }, key = "#id", allEntries = true)
	public ProductResponseDTO update(Long id, ProductRequestDTO request) {

		Product product = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		product.setName(request.getName());
		product.setBrand(request.getBrand());
		product.setCategory(request.getCategory());
		product.setPrice(request.getPrice());
		product.setStock(request.getStock());
		product.setDescription(request.getDescription());
		product.setSkinType(request.getSkinType());
		product.setMfgDate(request.getMfgDate());
		product.setExpDate(request.getExpDate());
		product.setUpdatedAt(LocalDateTime.now());

		Product updated = repo.save(product);

		log.info("Product updated id: {}", id);

		return mapToResponse(updated);
	}

	// ================= DELETE =================
	@CacheEvict(value = { "products", "product" }, key = "#id", allEntries = true)
	public void delete(Long id) {

		Product product = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		product.setDeleted(true);
		repo.save(product);

		log.warn("Soft deleted product id: {}", id);
	}

	// ================= SEARCH + PAGINATION =================
	@Cacheable(value = "products", key = "#name + '-' + #page + '-' + #size + '-' + #sortBy + '-' + #direction")
	public List<ProductResponseDTO> searchProducts(String name, int page, int size, String sortBy, String direction) {

		log.info("Fetching products from DB (not cache)");

		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Product> productPage;

		if (name == null || name.isEmpty()) {
			productPage = repo.findByDeletedFalse(pageable);
		} else {
			productPage = repo.findByNameContainingIgnoreCaseAndDeletedFalse(name, pageable);
		}

		return productPage.stream().map(this::mapToResponse).toList();
	}

	// ================= MAPPER =================
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

	public void reduceStock(Long id, int quantity) {
		Product product = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		if (product.getStock() < quantity) {
			throw new RuntimeException("Insufficient stock");
		}

		product.setStock(product.getStock() - quantity);
		repo.save(product);
	}
}