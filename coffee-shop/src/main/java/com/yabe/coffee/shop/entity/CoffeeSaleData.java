package com.yabe.coffee.shop.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;

@Entity
@Table(name = "tbl_coffee")
@Data
public class CoffeeSaleData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String coffeeName;
    private Double price;
    private Integer quantity;
    private String coffeeType;

}
