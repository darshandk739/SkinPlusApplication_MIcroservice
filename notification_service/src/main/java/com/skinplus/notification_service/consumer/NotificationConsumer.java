package com.skinplus.notification_service.consumer;

import com.skinplus.order_service.event.OrderCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

	@KafkaListener(topics = "order-created-topic", groupId = "notification-group")
	public void consume(OrderCreatedEvent event) {

		System.out.println("=================================");
		System.out.println("NOTIFICATION SERVICE TRIGGERED");
		System.out.println("Order ID: " + event.getOrderId());
		System.out.println("Product ID: " + event.getProductId());
		System.out.println("Quantity: " + event.getQuantity());
		System.out.println("Sending notification to user...");
		System.out.println("=================================");
	}
}