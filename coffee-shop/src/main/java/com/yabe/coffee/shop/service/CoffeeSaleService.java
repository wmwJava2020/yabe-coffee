package com.yabe.coffee.shop.service;

import com.yabe.coffee.shop.entity.CoffeeSaleData;
import java.util.List;

public interface CoffeeSaleService {
   void saveIndividualSale(Double price, Integer quantity, String coffeeType, String coffeeName);

   List<CoffeeSaleData> getCoffeeByName(String coffeeName);
}
