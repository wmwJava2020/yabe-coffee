package com.yabe.coffee_inventory.service;

import com.yabe.coffee_inventory.dto.InventoryDTO;
import com.yabe.coffee_inventory.entity.CoffeeInventory;

public interface InventoryService {
    InventoryDTO coffeeByName(String coffeeName);
    InventoryDTO coffeeByType(String coffeeType);

    void saveCoffeeToDb(CoffeeInventory coffeeInventory);
}
