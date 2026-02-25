package com.skinplus.order_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skinplus.order_service.client.PaymentClient;
import com.skinplus.order_service.client.ProductClient;
import com.skinplus.order_service.dto.PaymentRequest;
import com.skinplus.order_service.dto.ProductResponseDTO;
import com.skinplus.order_service.entity.Order;
import com.skinplus.order_service.event.OrderCreatedEvent;
import com.skinplus.order_service.kafka.OrderEventProducer;
import com.skinplus.order_service.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class OrderService {

	private final OrderRepository repo;
	private final ProductClient productClient;
	private final OrderEventProducer producer;

	@Autowired
	private PaymentClient paymentClient;

	public OrderService(OrderRepository repo, ProductClient productClient, OrderEventProducer producer) {
		this.repo = repo;
		this.productClient = productClient;
		this.producer = producer;
	}

	@CircuitBreaker(name = "productService", fallbackMethod = "productFallback")
	public Order createOrder(Long productId, int quantity) {

		System.out.println("STEP 1 → Calling product service");

		ProductResponseDTO product = productClient.getProduct(productId);

		System.out.println("STEP 2 → Product received: " + product);

		if (product.getStock() < quantity) {
			System.out.println("❌ Not enough stock");
			throw new RuntimeException("Not enough stock");
		}

		System.out.println("STEP 3 → Reducing stock");
		productClient.reduceStock(productId, quantity);

		BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(quantity));

		Order order = new Order();
		order.setProductId(productId);
		order.setQuantity(quantity);
		order.setTotalPrice(total);
		order.setStatus("CREATED");
		order.setCreatedAt(LocalDateTime.now());

		System.out.println("STEP 4 → Saving order to DB");

		Order savedOrder = repo.save(order);

		System.out.println("STEP 5 → Order saved with ID: " + savedOrder.getId());

		/* ================= PAYMENT CALL ================= */

		System.out.println("STEP 6 → Calling payment service");

		PaymentRequest paymentRequest = new PaymentRequest(savedOrder.getId(), total.doubleValue());

		String paymentStatus = paymentClient.processPayment(paymentRequest);

		System.out.println("Payment response: " + paymentStatus);

		if (!"PAYMENT_SUCCESS".equals(paymentStatus)) {
			throw new RuntimeException("Payment failed");
		}

		/* ================= KAFKA EVENT ================= */

		System.out.println("STEP 7 → Sending Kafka event");

		OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), productId, quantity);

		producer.sendOrderCreatedEvent(event);

		System.out.println("STEP 8 → Method finished successfully");

		return savedOrder;
	}

	public Order productFallback(Long productId, int quantity, Throwable ex) {
		System.out.println("⚠️ FALLBACK TRIGGERED: " + ex.getMessage());
		ex.printStackTrace();

		Order failedOrder = new Order();
		failedOrder.setProductId(productId);
		failedOrder.setQuantity(quantity);
		failedOrder.setStatus("FAILED");
		failedOrder.setCreatedAt(LocalDateTime.now());

		return failedOrder;
	}
}