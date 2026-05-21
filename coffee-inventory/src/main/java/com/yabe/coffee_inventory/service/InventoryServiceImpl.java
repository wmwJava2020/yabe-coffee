package com.yabe.coffee_inventory.service;

import com.yabe.coffee_inventory.client.CoffeeShopApiClient;
import com.yabe.coffee_inventory.dto.CoffeeShopApiResponse;
import com.yabe.coffee_inventory.dto.InventoryDTO;
import com.yabe.coffee_inventory.entity.CoffeeInventory;
import com.yabe.coffee_inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger LOGGER = Logger.getLogger(InventoryServiceImpl.class.getName());

    private final InventoryRepository repository;
    private final CoffeeShopApiClient coffeeShopApiClient;
    private CoffeeInventory inventory;

    public InventoryServiceImpl(InventoryRepository repository, CoffeeShopApiClient coffeeShopApiClient) {
        this.repository = repository;
        this.coffeeShopApiClient = coffeeShopApiClient;
    }

    /**
     * Get coffee inventory by name from external Coffee Shop Database API.
     * Returns price and total quantity sold.
     * Falls back to local repository if API call fails.
     *
     * @param coffeeName the name of the coffee
     * @return InventoryDTO with quantity and price, or null if not found
     */
    @Override
    public InventoryDTO coffeeByName(String coffeeName) {
        LOGGER.info("Fetching coffee by name from external API: " + coffeeName);

        // Try external API first
        var apiResponse = coffeeShopApiClient.getCoffeeByName(coffeeName);
        if (apiResponse.isPresent()) {
            CoffeeShopApiResponse response = apiResponse.get();
            InventoryDTO dto = new InventoryDTO();
            dto.setQuantity(response.getQuantitySold() != null ? response.getQuantitySold() : 0);
            dto.setPrice(response.getPrice());
            dto.setDate(java.time.LocalDate.now());
            LOGGER.info("Successfully retrieved coffee from external API: " + coffeeName);
            return dto;
        }

        LOGGER.warning("Failed to get coffee from external API, falling back to local repository: " + coffeeName);

        // Fallback to local repository
        CoffeeInventory found = repository.findFirstByCoffeeName(coffeeName);
        if (found == null) {
            LOGGER.warning("Coffee not found in local repository: " + coffeeName);
            return null;
        }

        InventoryDTO dto = new InventoryDTO();
        dto.setQuantity(found.getQuantity());
        dto.setPrice(found.getPrice());
        //dto.setDate(found.getInvDate());
        LOGGER.info("Retrieved coffee from local repository: " + coffeeName);
        return dto;
    }

    /**
     * Get coffee inventory by type from external Coffee Shop Database API.
     * Returns price and total quantity sold.
     * Falls back to local repository if API call fails.
     *
     * @param coffeeType the type of the coffee
     * @return InventoryDTO with quantity and price, or null if not found
     */
    @Override
    public InventoryDTO coffeeByType(String coffeeType) {
        LOGGER.info("Fetching total quantity and price for coffee type: " + coffeeType);

        List<CoffeeInventory> coffeeList = repository.findAllByCoffeeType(coffeeType);

        if (coffeeList == null || coffeeList.isEmpty()) {
            LOGGER.warning("No coffee found for type: " + coffeeType);
            return null;
        }

        int totalQuantity = coffeeList.stream()
                .mapToInt(CoffeeInventory::getQuantity)
                .sum();

        double totalPrice = coffeeList.stream()
                .mapToDouble(CoffeeInventory::getPrice)
                .sum();

        InventoryDTO dto = new InventoryDTO();
        dto.setTotalQuantity(totalQuantity);
        dto.setTotalPrice(Math.round(totalPrice * 100.0) / 100.0); // round to 2 decimal places
        dto.setDate(LocalDate.now());

        LOGGER.info("Total quantity: " + totalQuantity + ", Total price: " + totalPrice
                + " for coffee type: " + coffeeType);
        return dto;
    }

    @Override
    public void saveCoffeeToDb(CoffeeInventory coffeeInventory) {
        LOGGER.info("Saving coffee inventory to database: " + coffeeInventory.getCoffeeName());

        CoffeeInventory inventory = new CoffeeInventory();
        inventory.setCoffeeName(coffeeInventory.getCoffeeName());
        inventory.setCoffeeType(coffeeInventory.getCoffeeType());
        inventory.setPrice(coffeeInventory.getPrice());
        inventory.setInvDate(coffeeInventory.getInvDate());
        inventory.setQuantity(coffeeInventory.getQuantity());
        LOGGER.info("Saving individual sale data: Price - " + coffeeInventory.getPrice() + ", Quantity - " + coffeeInventory.getQuantity() + ", Coffee Type - " + coffeeInventory.getCoffeeType() + ", Coffee Name - " + coffeeInventory.getCoffeeName());
        repository.save(inventory);
    }
}
