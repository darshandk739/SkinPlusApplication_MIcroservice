package com.skinplus.payment_service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skinplus.payment_service.entity.PaymentRequest;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	@PostMapping
	public String processPayment(@RequestBody PaymentRequest request) {

		System.out.println("Processing payment for Order ID: " + request.getOrderId());
		System.out.println("Amount: " + request.getAmount());

		// Mock logic
		return "PAYMENT_SUCCESS";
	}
}