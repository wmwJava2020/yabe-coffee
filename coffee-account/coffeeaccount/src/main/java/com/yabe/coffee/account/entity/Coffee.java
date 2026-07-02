package com.yabe.coffee.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_account")
@Data
public class Coffee {

    Long cnt = System.currentTimeMillis();
    @Id
    private Long id = Long.valueOf(cnt.toString().substring(0,6));
    private String coffeeType;
    private Double unitPrice;
    private Integer quantity;
    private LocalDateTime lastAccessed;

}
