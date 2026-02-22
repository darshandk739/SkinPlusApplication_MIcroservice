package com.skinplus.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skinplus.inventory_service.dto.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
