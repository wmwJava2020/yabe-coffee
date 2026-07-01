package com.yabe.coffee.shop.respository;

import com.yabe.coffee.shop.entity.CoffeeSaleData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CoffeeHouseRepository extends JpaRepository<CoffeeSaleData, Long> {
    @Query("SELECT COALESCE(SUM(c.price * c.quantity), 0) FROM CoffeeSaleData c")
    BigDecimal getTotalSalesAmount();

    List<CoffeeSaleData> findByCoffeeName(String coffeeName);

    Optional<CoffeeSaleData> findByIdAndCoffeeName(Long id, String coffeeName);
}
