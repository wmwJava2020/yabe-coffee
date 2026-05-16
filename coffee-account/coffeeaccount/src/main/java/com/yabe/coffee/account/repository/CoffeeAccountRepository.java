package com.yabe.coffee.account.repository;

import com.yabe.coffee.account.entity.CoffeeAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeAccountRepository extends JpaRepository<CoffeeAccountEntity, Long> {
}
