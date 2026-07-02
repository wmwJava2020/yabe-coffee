package com.yabe.coffee.account.service;

import com.yabe.coffee.account.entity.Coffee;

import java.math.BigDecimal;

public interface CoffeeAccount {

    Coffee saveCoffee(Coffee coffee);

    BigDecimal coffeeSale(Integer quantity, Double unitPrice);



}
