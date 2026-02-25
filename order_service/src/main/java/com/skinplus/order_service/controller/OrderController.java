package com.skinplus.order_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skinplus.order_service.dto.OrderRequest;
import com.skinplus.order_service.entity.Order;
import com.skinplus.order_service.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	@PostMapping
	public Order create(@RequestBody OrderRequest request) {
		return service.createOrder(request.getProductId(), request.getQuantity());
	}
}