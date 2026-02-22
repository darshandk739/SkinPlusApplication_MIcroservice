package com.skinplus.inventory_service.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.skinplus.inventory_service.dto.Inventory;
import com.skinplus.inventory_service.repository.InventoryRepository;
import com.skinplus.order_service.event.OrderCreatedEvent;

@Service
public class InventoryConsumer {

    private final InventoryRepository repository;

    public InventoryConsumer(InventoryRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "order-created-topic", groupId = "inventory-group")
    public void consume(OrderCreatedEvent event) {

        Inventory inventory = repository.findById(event.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found in inventory"));

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - event.getQuantity()
        );

        repository.save(inventory);

        System.out.println("Inventory updated for product " + event.getProductId());
    }
}