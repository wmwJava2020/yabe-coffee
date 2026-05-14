package com.yabe.coffeeaccount.service;

import com.yabe.coffeeaccount.entity.CoffeeAccountEntity;
import com.yabe.coffeeaccount.repository.CoffeeAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CoffeeAccountImpl implements CoffeeAccount {

    private final CoffeeAccountRepository coffeeAccountRepository;
    private final RestTemplate restTemplate;

    public CoffeeAccountImpl(CoffeeAccountRepository coffeeAccountRepository, RestTemplate restTemplate) {
        this.coffeeAccountRepository = coffeeAccountRepository;
        this.restTemplate = restTemplate;
    }

    public Double saveTotalSalesFromCoffeeHouse() {

        BigDecimal totalSales = restTemplate.getForObject(
                "http://localhost:8080/api/v1/coffeehouse/total",
                BigDecimal.class
        );


        CoffeeAccountEntity account = new CoffeeAccountEntity();
        account.setCnt(System.currentTimeMillis());
        assert totalSales != null;
        account.setTotalSale(totalSales.doubleValue());
        account.setTotalQuantity(1);
        account.setLastAccessed(LocalDateTime.now());

        coffeeAccountRepository.save(account);

        return account.getTotalSale();

    }



}
