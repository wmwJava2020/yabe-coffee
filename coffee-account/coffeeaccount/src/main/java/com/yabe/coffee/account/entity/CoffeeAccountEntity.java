package com.yabe.coffee.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_account")
@Data
public class CoffeeAccountEntity {

    Long cnt = System.currentTimeMillis();
    @Id
    private Long id = cnt;
    private String coffeeName;
    private Double totalSale;
    private Integer totalQuantity;
    private LocalDateTime lastAccessed;
}
