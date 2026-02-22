package com.skinplus.order_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.skinplus.order_service.event.OrderCreatedEvent;

@Service
public class OrderEventProducer {

	private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

	public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendOrderCreatedEvent(OrderCreatedEvent event) {
		System.out.println(" 🔥 SENDING EVENT TO KAFKA: " + event);
		kafkaTemplate.send("order-created-topic", event);
	}
}