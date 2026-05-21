package com.yabe.coffee_inventory.client;

import com.yabe.coffee_inventory.dto.CoffeeShopApiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of CoffeeShopApiClient.
 * Calls the external Coffee Shop Database API via REST/HTTP.
 * Handles wrapper response format: { success: true, message: "...", data: [...] }
 */
@Component
public class CoffeeShopApiClientImpl implements CoffeeShopApiClient {

    private static final Logger LOGGER = Logger.getLogger(CoffeeShopApiClientImpl.class.getName());

    private final RestTemplate restTemplate;

    @Value("${coffee-shop.api.base-url:http://localhost:8082}")
    private String apiBaseUrl;


    public CoffeeShopApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Get coffee by name.
     * API response format: { success: true, message: "...", data: [{id, coffeeName, price, quantity, coffeeType}, ...] }
     * Extracts first item from data array.
     */
    @Override
    public Optional<CoffeeShopApiResponse> getCoffeeByName(String coffeeName) {


        try {
            //http://localhost:8080/api/v1/coffeehouse/searchByName?coffeeName=Gedeo
            String url = apiBaseUrl + "/api/v1/coffeehouse/searchByName?coffeeName=" + coffeeName;
            LOGGER.info("Calling external API: " + url);

            // Get response as JsonNode to parse wrapper structure
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response == null) {
                LOGGER.warning("Null response from API for name: " + coffeeName);
                return Optional.empty();
            }

            LOGGER.fine("API response received. Success: " + response.get("success"));

            // Check if request was successful
            if (!response.get("success").asBoolean(false)) {
                LOGGER.warning("API returned success=false for name: " + coffeeName);
                return Optional.empty();
            }

            // Extract data array
            JsonNode dataArray = response.get("data");
            if (dataArray == null || !dataArray.isArray() || dataArray.size() == 0) {
                LOGGER.warning("API data array is empty or missing for name: " + coffeeName);
                return Optional.empty();
            }

            // Map first item to CoffeeShopApiResponse
            JsonNode firstItem = dataArray.get(0);
            CoffeeShopApiResponse dto = new CoffeeShopApiResponse();
            dto.setCoffeeName(firstItem.get("coffeeName").asText());
            dto.setCoffeeType(firstItem.get("coffeeType").asText());
            dto.setPrice(firstItem.get("price").asDouble());
            dto.setQuantitySold(firstItem.get("quantity").asInt());
            dto.setSource("Coffee Shop API - " + firstItem.get("id").asText());

            LOGGER.info("Successfully mapped coffee from API for name: " + coffeeName);
            return Optional.of(dto);

        } catch (RestClientException e) {
            LOGGER.log(Level.WARNING, "HTTP error calling coffee-shop API for name: " + coffeeName, e);
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error parsing coffee-shop API response for name: " + coffeeName, e);
            return Optional.empty();
        }
    }

    /**
     * Get coffee by type.
     * API response format: { success: true, message: "...", data: [{id, coffeeName, price, quantity, coffeeType}, ...] }
     * Extracts first item from data array.
     */
    @Override
    public Optional<CoffeeShopApiResponse> getCoffeeByType(String coffeeType) {
        try {
            String url = apiBaseUrl + "/api/v1/coffeehouse/by-type?type=" + coffeeType;
            LOGGER.info("Calling external API: " + url);

            // Get response as JsonNode to parse wrapper structure
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response == null) {
                LOGGER.warning("Null response from API for type: " + coffeeType);
                return Optional.empty();
            }

            LOGGER.fine("API response received. Success: " + response.get("success"));

            // Check if request was successful
            if (!response.get("success").asBoolean(false)) {
                LOGGER.warning("API returned success=false for type: " + coffeeType);
                return Optional.empty();
            }

            // Extract data array
            JsonNode dataArray = response.get("data");
            if (dataArray == null || !dataArray.isArray() || dataArray.size() == 0) {
                LOGGER.warning("API data array is empty or missing for type: " + coffeeType);
                return Optional.empty();
            }

            // Map first item to CoffeeShopApiResponse
            JsonNode firstItem = dataArray.get(0);
            CoffeeShopApiResponse dto = new CoffeeShopApiResponse();
            dto.setCoffeeName(firstItem.get("coffeeName").asText());
            dto.setCoffeeType(firstItem.get("coffeeType").asText());
            dto.setPrice(firstItem.get("price").asDouble());
            dto.setQuantitySold(firstItem.get("quantity").asInt());
            dto.setSource("Coffee Shop API - " + firstItem.get("id").asText());

            LOGGER.info("Successfully mapped coffee from API for type: " + coffeeType);
            return Optional.of(dto);

        } catch (RestClientException e) {
            LOGGER.log(Level.WARNING, "HTTP error calling coffee-shop API for type: " + coffeeType, e);
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error parsing coffee-shop API response for type: " + coffeeType, e);
            return Optional.empty();
        }
    }
}
