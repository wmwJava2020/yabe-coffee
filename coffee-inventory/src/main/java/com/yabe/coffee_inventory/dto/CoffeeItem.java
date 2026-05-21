package com.yabe.coffee_inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single coffee item from the external Coffee Shop API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeItem {

    private Long id;

    @JsonProperty("coffeeName")
    private String coffeeName;

    @JsonProperty("coffeeType")
    private String coffeeType;

    private Double price;

    @JsonProperty("quantity")
    private Integer quantity;
}

