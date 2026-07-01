package com.yabe.coffee_inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO from the external Coffee Shop Database API.
 * Contains price and total quantity sold of a coffee product.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeShopApiResponse {

    private String coffeeName;
    private String coffeeType;
    private Double price;           // Price per unit from the external API
    private Integer quantitySold;   // Total quantity sold from the external API
    private String source;          // Source/origin from API (optional)
}

