package com.yabe.coffee.account.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.yabe.coffee.account.entity.CoffeeAccountEntity;
import com.yabe.coffee.account.repository.CoffeeAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

class CoffeeAccountImplTest {

    @Mock
    private CoffeeAccountRepository coffeeAccountRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoffeeAccountImpl coffeeAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTotalSalesFromCoffeeHouse_Success() {
        BigDecimal expectedTotalSales = new BigDecimal("1500.50");
        when(restTemplate.getForObject(
                "http://localhost:8080/api/v1/coffeehouse/total",
                BigDecimal.class
        )).thenReturn(expectedTotalSales);

        Double result = coffeeAccountService.saveTotalSalesFromCoffeeHouse();

        assertEquals(1500.50, result);
        verify(coffeeAccountRepository, times(1)).save(any(CoffeeAccountEntity.class));
    }

    @Test
    void testSaveTotalSalesFromCoffeeHouse_VerifyEntityData() {
        BigDecimal expectedTotalSales = new BigDecimal("2500.75");
        when(restTemplate.getForObject(
                "http://localhost:8080/api/v1/coffeehouse/total",
                BigDecimal.class
        )).thenReturn(expectedTotalSales);

        coffeeAccountService.saveTotalSalesFromCoffeeHouse();

        //ArgumentCaptor<CoffeeAccountEntity> captor = ArgumentCaptor.forClass(CoffeeAccountEntity.class);
        //verify(coffeeAccountRepository).save(captor.getValue());

        //CoffeeAccountEntity savedEntity = captor.getValue();
        //assertEquals(2500.75, savedEntity.getTotalSale());
        //assertEquals(1, savedEntity.getTotalQuantity());
        //assertNotNull(savedEntity.getLastAccessed());
    }

    @Test
    void testSaveTotalSalesFromCoffeeHouse_ReturnsCorrectValue() {
        BigDecimal expectedTotalSales = new BigDecimal("999.99");
        when(restTemplate.getForObject(
                "http://localhost:8080/api/v1/coffeehouse/total",
                BigDecimal.class
        )).thenReturn(expectedTotalSales);

        Double result = coffeeAccountService.saveTotalSalesFromCoffeeHouse();

        assertEquals(999.99, result);
    }

    @Test
    void testSaveTotalSalesFromCoffeeHouse_CallsRestTemplate() {
        when(restTemplate.getForObject(
                "http://localhost:8080/api/v1/coffeehouse/total",
                BigDecimal.class
        )).thenReturn(new BigDecimal("100.00"));

        coffeeAccountService.saveTotalSalesFromCoffeeHouse();

        verify(restTemplate, times(1)).getForObject(
                "http://localhost:8080/api/v1/coffeehouse/total",
                BigDecimal.class
        );
    }
}