package com.yabe.coffee.account.service;

import com.yabe.coffee.account.entity.Coffee;
import com.yabe.coffee.account.repository.CoffeeAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CoffeeAccountImpl implements CoffeeAccount {

    private final CoffeeAccountRepository coffeeRepository;
    private final RestTemplate restTemplate;

    public CoffeeAccountImpl(CoffeeAccountRepository coffeeRepository, RestTemplate restTemplate) {
        this.coffeeRepository = coffeeRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Coffee saveCoffee(Coffee coffee) {
        return coffeeRepository.save(coffee);
    }

    @Override
    public BigDecimal coffeeSale(Integer quantity, Double unitPrice) {
        BigDecimal totalSales = restTemplate.getForObject(
                "http://localhost:8012/api/v1/coffeehouse/total",
                BigDecimal.class
        );

        Coffee coffee = new Coffee();
        coffee.setQuantity(quantity);
        coffee.setUnitPrice(unitPrice);
        coffee.setLastAccessed(LocalDateTime.now());
        coffeeRepository.save(coffee);

        return totalSales;
    }
}
