package com.skinplus.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.skinplus.order_service.dto.ProductResponseDTO;

@FeignClient(name = "product-service")
public interface ProductClient {

	@GetMapping("/products/{id}")
	ProductResponseDTO getProduct(@PathVariable Long id);

	@PutMapping("/products/{id}/reduce-stock")
	void reduceStock(@PathVariable Long id, @RequestParam int quantity);

}