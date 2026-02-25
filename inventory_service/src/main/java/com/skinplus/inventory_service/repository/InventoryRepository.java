package com.skinplus.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.skinplus.inventory_service.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}