package com.yabe.coffeeaccount.repository;

import com.yabe.coffeeaccount.entity.CoffeeAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeAccountRepository extends JpaRepository<CoffeeAccountEntity, Long> {
}
