package com.skinplus.inventory_service.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinplus.inventory_service.entity.Inventory;
import com.skinplus.inventory_service.repository.InventoryRepository;

@Service
public class InventoryConsumer {

    private final InventoryRepository repository;
    private final ObjectMapper objectMapper;

    public InventoryConsumer(InventoryRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-created-topic", groupId = "inventory-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(record.value(), OrderCreatedEvent.class);

            System.out.println("📦 INVENTORY EVENT RECEIVED - productId: "
                + event.getProductId() + " quantity: " + event.getQuantity());

            Inventory inventory = repository.findById(event.getProductId())
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setProductId(event.getProductId());
                    newInventory.setAvailableQuantity(0);
                    return newInventory;
                });

            inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - event.getQuantity()
            );

            repository.save(inventory);

            System.out.println("✅ Inventory updated for productId: "
                + event.getProductId()
                + " | New quantity: " + inventory.getAvailableQuantity());

        } catch (Exception e) {
            System.err.println("❌ Failed to process inventory event: " + e.getMessage());
        }
    }
}