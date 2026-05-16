package com.yabe.coffee.shop.controller;

import com.yabe.coffee.shop.dto.ApiResponse;
import com.yabe.coffee.shop.entity.CoffeeSaleData;
import com.yabe.coffee.shop.respository.CoffeeHouseRepository;
import com.yabe.coffee.shop.service.CoffeeSaleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/coffeehouse")
public class CoffeeHouseController {

    Logger logger = Logger.getLogger(CoffeeHouseController.class.getName());
    @Autowired
    private CoffeeSaleServiceImpl coffeeSaleServiceImpl;

    @PostMapping("/individualSale")
    public ResponseEntity<ApiResponse<CoffeeSaleData>> saveIndividualSale(@RequestBody CoffeeSaleData coffeeSaleData) {
        try {
            logger.info("Received individual sale data: Price - " + coffeeSaleData.getPrice() + ", Quantity - " + coffeeSaleData.getQuantity());
            coffeeSaleServiceImpl.saveIndividualSale(coffeeSaleData.getPrice()
                    ,coffeeSaleData.getQuantity()
            ,coffeeSaleData.getCoffeeType()
            ,coffeeSaleData.getCoffeeName());
            return ResponseEntity.ok(new ApiResponse<>(true, "Individual sale data saved successfully.", coffeeSaleData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error saving individual sale data: " + e.getMessage(), null));
        }
    }

    @GetMapping("/total")
    public BigDecimal getTotalSalesAmount() {
        return coffeeSaleServiceImpl.getTotalSalesAmount();
    }



}
