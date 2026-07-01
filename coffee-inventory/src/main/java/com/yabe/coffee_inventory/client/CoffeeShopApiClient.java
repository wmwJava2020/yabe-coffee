package com.yabe.coffee_inventory.client;

import com.yabe.coffee_inventory.dto.CoffeeShopApiResponse;
import java.util.Optional;

/**
 * Client interface for calling external Coffee Shop Database API.
 * Retrieves price and quantity sold by coffee name or type.
 */
public interface CoffeeShopApiClient {

    /**
     * Get coffee data by name from the external API.
     * @param coffeeName the name of the coffee
     * @return optional containing API response with price and quantity
     */
    Optional<CoffeeShopApiResponse> getCoffeeByName(String coffeeName);

    /**
     * Get coffee data by type from the external API.
     * @param coffeeType the type of the coffee
     * @return optional containing API response with price and quantity
     */
    Optional<CoffeeShopApiResponse> getCoffeeByType(String coffeeType);
}

