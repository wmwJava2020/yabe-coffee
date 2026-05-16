package com.yabe.coffee.shop.respository;

import com.yabe.coffee.shop.entity.CoffeeSaleData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface CoffeeHouseRepository extends JpaRepository<CoffeeSaleData, Long> {
    @Query("SELECT COALESCE(SUM(c.price * c.quantity), 0) FROM CoffeeSaleData c")
    BigDecimal getTotalSalesAmount();
}
