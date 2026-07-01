package com.yabe.coffee_inventory.controller;

import com.yabe.coffee_inventory.dto.InventoryDTO;
import com.yabe.coffee_inventory.entity.CoffeeInventory;
import com.yabe.coffee_inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

/**
 * Controller for Coffee Inventory endpoints.
 * Delegates to InventoryService for business logic.
 * Service calls external Coffee Shop API and falls back to local repository.
 */
@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private static final Logger LOGGER = Logger.getLogger(InventoryController.class.getName());

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Get coffee inventory by name.
     * Calls external API first, falls back to local repository.
     * Returns price and total quantity sold.
     *
     * @param coffeeName the name of the coffee (required, non-empty)
     * @return 200 OK with InventoryDTO, or 400 Bad Request if invalid, or 404 Not Found
     */
    @GetMapping("/by-name")
    public ResponseEntity<InventoryDTO> coffeeByName(@RequestParam String coffeeName) {

        // Validate input
        if (coffeeName == null || coffeeName.trim().isEmpty()) {
            LOGGER.warning("Invalid request: coffeName is null or empty");
            return ResponseEntity.badRequest().build();
        }

        LOGGER.info("Fetching coffee by name: " + coffeeName);

        try {
            InventoryDTO result = inventoryService.coffeeByName(coffeeName.trim());
            if (result == null) {
                LOGGER.warning("Coffee not found: " + coffeeName);
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            LOGGER.severe("Error fetching coffee by name: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get coffee inventory by type.
     * Calls external API first, falls back to local repository.
     * Returns price and total quantity sold.
     *
     * @param coffeeType the type of the coffee (required, non-empty)
     * @return 200 OK with InventoryDTO, or 400 Bad Request if invalid, or 404 Not Found
     */
    @GetMapping("/by-type")
    public ResponseEntity<InventoryDTO> coffeeByType(@RequestParam String coffeeType) {

        InventoryDTO dto = inventoryService.coffeeByType(coffeeType);

        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(dto);

    }

    /**
     * Health check endpoint
     * @return 200 OK indicating service is healthy
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Coffee Inventory Service is healthy");
    }


    @GetMapping("/coffee-inv")
    public ResponseEntity<String> coffeeToDb(@RequestBody CoffeeInventory coffeeInventory) {
      inventoryService.saveCoffeeToDb(coffeeInventory);
        LOGGER.info("Saving individual sale data: Price - " + coffeeInventory.getPrice() + ", Quantity - " + coffeeInventory.getQuantity() + ", Coffee Type - " + coffeeInventory.getCoffeeType() + ", Coffee Name - " + coffeeInventory.getCoffeeName());
        return ResponseEntity.ok("Coffee Inventory saved successfully");
    }
}

