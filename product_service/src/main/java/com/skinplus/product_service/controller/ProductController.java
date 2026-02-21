package com.skinplus.product_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

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
	public List<ProductResponseDTO> search(@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction) {

		return service.searchProducts(name, page, size, sortBy, direction);
	}

	@GetMapping("/{id}")
	public ProductResponseDTO getById(@PathVariable Long id) {
		return service.getById(id);
	}

	@PutMapping("/{id}")
	public ProductResponseDTO update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {
		return service.update(id, request);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}
}