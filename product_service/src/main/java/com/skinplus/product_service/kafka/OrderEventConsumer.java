package com.skinplus.product_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.skinplus.product_service.event.OrderCreatedEvent;

@Service
public class OrderEventConsumer {

	@KafkaListener(topics = "order-created-topic", groupId = "product-group")
	public void consume(OrderCreatedEvent event) {
		System.out.println("🔥 RECEIVED ORDER EVENT: " + event);
		System.out.println("Received Order Event:");
		System.out.println("OrderId: " + event.getOrderId());
		System.out.println("ProductId: " + event.getProductId());
		System.out.println("Quantity: " + event.getQuantity());
	}
}