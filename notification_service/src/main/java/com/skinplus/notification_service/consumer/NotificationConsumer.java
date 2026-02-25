package com.skinplus.notification_service.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinplus.notification_service.event.OrderCreatedEvent;

@Service
public class NotificationConsumer {

	private final ObjectMapper objectMapper;

	public NotificationConsumer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = "order-created-topic", groupId = "notification-group")
	public void consume(ConsumerRecord<String, String> record) {
		try {
			OrderCreatedEvent event = objectMapper.readValue(record.value(), OrderCreatedEvent.class);

			System.out.println("=================================");
			System.out.println("NOTIFICATION SERVICE TRIGGERED");
			System.out.println("Order ID: " + event.getOrderId());
			System.out.println("Product ID: " + event.getProductId());
			System.out.println("Quantity: " + event.getQuantity());
			System.out.println("Sending notification to user...");
			System.out.println("=================================");

		} catch (Exception e) {
			System.err.println("❌ Failed to process notification event: " + e.getMessage());
		}
	}
}