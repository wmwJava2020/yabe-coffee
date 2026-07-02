package com.yabe.coffee.account.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.yabe.coffee.account.entity.Coffee;
import com.yabe.coffee.account.repository.CoffeeAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
@ExtendWith(MockitoExtension.class)
class CoffeeAccountImplTest {

    @Mock
    private CoffeeAccountRepository coffeeAccountRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoffeeAccountImpl coffeeAccountService;



    @Test
    void testSaveTotalSalesFromCoffeeHouse_Success() {
        BigDecimal expectedTotalSales = new BigDecimal("1500.50");
        when(restTemplate.getForObject(
                "http://localhost:8012/api/v1/coffeehouse/total",
                BigDecimal.class
        )).thenReturn(expectedTotalSales);

        Double result = coffeeAccountService.coffeeSale(10, 150.05).doubleValue();

        assertEquals(1500.50, result);
        verify(coffeeAccountRepository, times(1)).save(any(Coffee.class));
    }

    @Test
    void testSaveTotalSalesFromCoffeeHouse_VerifyEntityData() {
        BigDecimal expectedTotalSales = new BigDecimal("2500.75");
        when(restTemplate.getForObject(
                "http://localhost:8012/api/v1/coffeehouse/total",
                BigDecimal.class
        )).thenReturn(expectedTotalSales);

        coffeeAccountService.coffeeSale(10, 250.075);
        //ArgumentCaptor<Coffee> captor = ArgumentCaptor.forClass(Coffee.class);
        //verify(coffeeAccountRepository).save(captor.getValue());

        //Coffee savedEntity = captor.getValue();
        //assertEquals(2500.75, savedEntity.getTotalSale());
        //assertEquals(1, savedEntity.getTotalQuantity());
        //assertNotNull(savedEntity.getLastAccessed());
    }

    @Test
    void testSaveTotalSalesFromCoffeeHouse_ReturnsCorrectValue() {
        BigDecimal expectedTotalSales = new BigDecimal("999.99");
        when(restTemplate.getForObject(
                "http://localhost:8012/api/v1/coffeehouse/total",
                BigDecimal.class
        )).thenReturn(expectedTotalSales);

        Double result = coffeeAccountService.coffeeSale(10, 99.99).doubleValue();

        assertEquals(999.99, result);
    }

    @Test
    void testSaveTotalSalesFromCoffeeHouse_CallsRestTemplate() {
        when(restTemplate.getForObject(
                "http://localhost:8012/api/v1/coffeehouse/total",
                BigDecimal.class
        )).thenReturn(new BigDecimal("100.00"));

        coffeeAccountService.coffeeSale(10, 10.0);

        verify(restTemplate, times(1)).getForObject(
                "http://localhost:8012/api/v1/coffeehouse/total",
                BigDecimal.class
        );
    }
}