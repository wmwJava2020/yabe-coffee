package com.yabe.coffee.shop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.yabe.coffee.shop.entity.CoffeeSaleData;
import com.yabe.coffee.shop.respository.CoffeeHouseRepository;
import com.yabe.coffee.shop.service.CoffeeSaleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoffeeSaleServiceImplTest {

    @Mock
    private CoffeeHouseRepository coffeeHouseRepository;

    @InjectMocks
    private CoffeeSaleServiceImpl coffeeSaleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveIndividualSale() {
        Double price = 5.50;
        Integer quantity = 2;
        String coffeeType = "Espresso";
        String coffeeName = "Premium Espresso";

        coffeeSaleService.saveIndividualSale(price, quantity, coffeeType, coffeeName);

        verify(coffeeHouseRepository, times(1)).save(any(CoffeeSaleData.class));
    }

    @Test
    void testSaveIndividualSaleWithCorrectData() {
        Double price = 3.75;
        Integer quantity = 1;
        String coffeeType = "Americano";
        String coffeeName = "Classic Americano";

        coffeeSaleService.saveIndividualSale(price, quantity, coffeeType, coffeeName);

        verify(coffeeHouseRepository).save(argThat(sale ->
                sale.getPrice().equals(price) &&
                sale.getQuantity().equals(quantity) &&
                sale.getCoffeeType().equals(coffeeType) &&
                sale.getCoffeeName().equals(coffeeName)
        ));
    }
}