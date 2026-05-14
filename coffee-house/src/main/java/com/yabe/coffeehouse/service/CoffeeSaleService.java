package com.yabe.coffeehouse.service;

public interface CoffeeSaleService {
   void saveIndividualSale(Double price, Integer quantity,String coffeeType,String coffeeName);
}
