package com.yabe.coffee.shop.service;

import com.yabe.coffee.shop.entity.CoffeeSaleData;
import com.yabe.coffee.shop.respository.CoffeeHouseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Service
public class CoffeeSaleServiceImpl implements  CoffeeSaleService {

    private final CoffeeHouseRepository coffeeHouseRepository;

    public CoffeeSaleServiceImpl(CoffeeHouseRepository coffeeHouseRepository) {
        this.coffeeHouseRepository = coffeeHouseRepository;
    }

    public void saveIndividualSale(CoffeeSaleData coffeeSaleData) {
        coffeeHouseRepository.save(coffeeSaleData);
    }

    @Override
    public void saveIndividualSale(Double price, Integer quantity, String coffeeType, String coffeeName) {
        CoffeeSaleData coffeeSaleData = new CoffeeSaleData();
        //coffeeSaleData.getId();
        coffeeSaleData.setPrice(price);
        coffeeSaleData.setCoffeeType(coffeeType);
        coffeeSaleData.setCoffeeName(coffeeName);
        coffeeSaleData.setQuantity(quantity);
        Logger logger = Logger.getLogger(CoffeeSaleServiceImpl.class.getName());
        logger.info("Saving individual sale data: Price - " + price + ", Quantity - " + quantity + "coffeeType - "+ coffeeType + "coffeeName - "+ coffeeName);
        coffeeHouseRepository.save(coffeeSaleData);
    }

    public BigDecimal getTotalSalesAmount() {
        return coffeeHouseRepository.getTotalSalesAmount();
    }

}
