package com.yabe.coffee.account.controller;

import com.yabe.coffee.account.service.CoffeeAccount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coffeeaccount")
public class CoffeeAccountController {

    private final CoffeeAccount coffeeAccount;

    public CoffeeAccountController(CoffeeAccount coffeeAccount) {
        this.coffeeAccount = coffeeAccount;
    }

    @PostMapping("/totalSales")
    public Double saveTotalSalesFromCoffeeHouse() {
        return coffeeAccount.coffeeSale(10, 5.0).doubleValue();

}



}
