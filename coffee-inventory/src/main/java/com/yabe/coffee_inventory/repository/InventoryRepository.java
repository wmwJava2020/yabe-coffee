package com.yabe.coffee_inventory.repository;

import com.yabe.coffee_inventory.entity.CoffeeInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<CoffeeInventory, Long> {

	CoffeeInventory findFirstByCoffeeName(String coffeeName);

	CoffeeInventory findFirstByCoffeeType(String coffeeType);

	List<CoffeeInventory> findAllByCoffeeType(String coffeeType);
}
