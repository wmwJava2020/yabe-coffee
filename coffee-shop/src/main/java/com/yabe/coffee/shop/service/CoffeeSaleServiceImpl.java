package com.yabe.coffee.shop.service;

import com.yabe.coffee.shop.entity.CoffeeSaleData;
import com.yabe.coffee.shop.respository.CoffeeHouseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CoffeeSaleServiceImpl implements  CoffeeSaleService {

    Logger log =  Logger.getLogger(CoffeeSaleServiceImpl.class.getName());

    private final CoffeeHouseRepository coffeeHouseRepository;

    public CoffeeSaleServiceImpl(CoffeeHouseRepository coffeeHouseRepository) {
        this.coffeeHouseRepository = coffeeHouseRepository;
    }


    @Override
    public void saveIndividualSale(Double price, Integer quantity, String coffeeType, String coffeeName) {
        CoffeeSaleData coffeeSaleData = new CoffeeSaleData();
        //coffeeSaleData.getId();
        coffeeSaleData.setPrice(price);
        coffeeSaleData.setCoffeeType(coffeeType);
        coffeeSaleData.setCoffeeName(coffeeName);
        coffeeSaleData.setQuantity(quantity);
        log.info("Saving individual sale data: Price - " + price + ", Quantity - " + quantity + "coffeeType - "+ coffeeType + "coffeeName - "+ coffeeName);
        coffeeHouseRepository.save(coffeeSaleData);
    }

    public BigDecimal getTotalSalesAmount() {
        System.out.println("TEST FOR JENKINS TUTORIAL");
        return coffeeHouseRepository.getTotalSalesAmount();
    }

    @Override
    public List<CoffeeSaleData> getCoffeeByName(String coffeeName) {
        log.info("Fetching coffee by name: " + coffeeName);
        return coffeeHouseRepository.findByCoffeeName(coffeeName);
    }

}
